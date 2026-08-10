package com.example

import android.net.Uri
import android.util.Log
import java.net.URI

/**
 * Isolated architecture for Secret Browser Tracking Protection.
 * Performs entirely local database/rule-based lookup for tracker blocking.
 * Guaranteed local decisions without any external telemetry or DNS queries.
 */
object SecretBrowserTrackingProtection {
    private const val TAG = "TrackingProtection"
    
    // Dynamic preference hooks wired by ViewModel to isolate UI, state, and engine
    var isGlobalEnabled: () -> Boolean = { true }
    var getSiteOverride: (String) -> Boolean? = { null }
    var onTrackerBlocked: ((tabId: String?, currentSiteUrl: String?) -> Unit)? = null

    // 1. Advertising Trackers
    val ADVERTISING_TRACKERS = setOf(
        "doubleclick.net",
        "googleadservices.com",
        "adservice.google.com",
        "adnxs.com",
        "pubmatic.com",
        "rubiconproject.com",
        "adsrvr.org",
        "taboola.com",
        "outbrain.com",
        "adcolony.com",
        "applovin.com",
        "unityads.unity3d.com",
        "amazon-adsystem.com",
        "adsystem.com",
        "popads.net",
        "clickasegura.com",
        "quantserve.com",
        "exponential.com",
        "yieldmanager.com"
    )

    // 2. Analytics Trackers
    val ANALYTICS_TRACKERS = setOf(
        "google-analytics.com",
        "analytics.google.com",
        "hotjar.com",
        "mixpanel.com",
        "amplitude.com",
        "segment.io",
        "optimizely.com",
        "statcounter.com",
        "googletagmanager.com",
        "scorecardresearch.com",
        "chartbeat.com",
        "crazyegg.com",
        "newrelic.com",
        "intercom.io"
    )

    // 3. Social Trackers
    val SOCIAL_TRACKERS = setOf(
        "connect.facebook.net",
        "platform.twitter.com",
        "platform.instagram.com",
        "pinterest.com/js",
        "snapchat.com/sdk",
        "linkedin.com/count"
    )

    // 4. Known Tracking Domains/Scripts
    val KNOWN_TRACKING_DOMAINS = setOf(
        "criteo.com",
        "ads-twitter.com",
        "tracker",
        "pixel.facebook.com",
        "analytics",
        "telemetry",
        "trackers",
        "ads.youtube.com",
        "metrics",
        "beacon"
    )

    /**
     * Evaluates whether a given request URL should be blocked as a tracker.
     * Implements strict fail-open behavior: any error during matching or parsing
     * will default to ALLOW (returning false).
     *
     * Rules:
     * - Main-frame navigation -> ALLOW
     * - Internal browser resources (about:, file:, data:, blob:) -> ALLOW
     * - Match known tracker -> BLOCK
     * - Normal request -> ALLOW
     */
    fun shouldBlock(url: String?, isMainFrame: Boolean, currentSiteUrl: String? = null): Boolean {
        if (url.isNullOrBlank()) {
            return false
        }

        // 1. Fail-open safety check: main-frame navigations must always be allowed
        if (isMainFrame) {
            return false
        }

        // Check if tracking protection is active globally or via per-site override
        val currentHost = currentSiteUrl?.let { extractHost(it) }
        val isProtectionActive = if (currentHost != null) {
            val override = getSiteOverride(currentHost)
            if (override != null) {
                override
            } else {
                isGlobalEnabled()
            }
        } else {
            isGlobalEnabled()
        }

        if (!isProtectionActive) {
            return false
        }

        try {
            // 2. Allow internal browser resources and local/secure assets
            val lowerUrl = url.trim().lowercase()
            if (lowerUrl.startsWith("about:") ||
                lowerUrl.startsWith("file:") ||
                lowerUrl.startsWith("data:") ||
                lowerUrl.startsWith("blob:") ||
                lowerUrl.startsWith("chrome:") ||
                lowerUrl.startsWith("resource:") ||
                lowerUrl.startsWith("android-app:")
            ) {
                return false
            }

            // 3. Extract host for matching
            val host = extractHost(url) ?: return false

            // 4. Match host against categorized blocklists (Exact domain or subdomain matching)
            if (isTrackerHost(host)) {
                Log.d(TAG, "BLOCKED tracker request (host match): $host (URL: $url)")
                return true
            }

            // 5. Additional fallback: path/substring checking for generic tracker footprints in resource names
            if (containsTrackingFootprint(lowerUrl)) {
                Log.d(TAG, "BLOCKED tracker request (substring footprint match): $url")
                return true
            }

        } catch (e: Exception) {
            // Guarantee Fail-open: log error, do not crash, and allow request to continue
            Log.e(TAG, "Exception in shouldBlock evaluating $url. Defaulting to ALLOW.", e)
            return false
        }

        return false
    }

    /**
     * Safely extracts the host/domain name from a URL string.
     */
    fun extractHost(url: String): String? {
        return try {
            val uri = Uri.parse(url)
            val host = uri.host
            if (!host.isNullOrBlank()) {
                host.lowercase()
            } else {
                // Try java.net.URI fallback
                val javaUri = URI(url)
                javaUri.host?.lowercase()
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Checks if the given host matches any of our categorized tracking domains (exact or subdomain).
     */
    private fun isTrackerHost(host: String): Boolean {
        // Iterate through all categories
        val allCategories = listOf(
            ADVERTISING_TRACKERS,
            ANALYTICS_TRACKERS,
            SOCIAL_TRACKERS,
            KNOWN_TRACKING_DOMAINS
        )

        for (category in allCategories) {
            for (blockedDomain in category) {
                if (host == blockedDomain || host.endsWith(".$blockedDomain")) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Fallback substring analysis to catch tracking scripts/pixels in URL structures.
     */
    private fun containsTrackingFootprint(lowerUrl: String): Boolean {
        // Look for common patterns specifically inside query parameters or path segments, avoiding main domain false positives
        if (lowerUrl.contains("/fbevents.js") ||
            lowerUrl.contains("/gtm.js") ||
            lowerUrl.contains("/ga.js") ||
            lowerUrl.contains("/analytics.js") ||
            lowerUrl.contains("ads.doubleclick.net") ||
            lowerUrl.contains("googleadservices.com/pagead")
        ) {
            return true
        }
        return false
    }
}
