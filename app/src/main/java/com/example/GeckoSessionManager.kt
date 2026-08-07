package com.example

import android.content.Context
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings
import java.util.concurrent.ConcurrentHashMap

/**
 * Manager responsible for handling the lifecycle, instantiation, destruction,
 * and retrieval of GeckoView sessions. This prepares the architecture for
 * future multi-tab support by indexing sessions by tab ID.
 */
object GeckoSessionManager {
    // Thread-safe map holding the active GeckoSession for each tab ID
    private val activeSessions = ConcurrentHashMap<String, GeckoSession>()

    /**
     * Creates a new GeckoSession or returns an existing one for the specified tabId.
     */
    fun getOrCreateSession(
        context: Context,
        tabId: String,
        initialUrl: String,
        isDesktopMode: Boolean,
        onUpdate: ((TabState) -> TabState) -> Unit
    ): GeckoSession {
        // Return existing session if already created
        activeSessions[tabId]?.let { return it }

        // Setup GeckoSession settings (Desktop vs Mobile mode user agent)
        val settings = GeckoSessionSettings.Builder()
            .userAgentMode(
                if (isDesktopMode) GeckoSessionSettings.USER_AGENT_MODE_DESKTOP 
                else GeckoSessionSettings.USER_AGENT_MODE_MOBILE
            )
            .build()
        
        val session = GeckoSession(settings)

        // Navigation delegate to track URL shifts and navigation state
        session.navigationDelegate = object : GeckoSession.NavigationDelegate {
            override fun onLocationChange(
                s: GeckoSession, 
                url: String?, 
                perms: List<GeckoSession.PermissionDelegate.ContentPermission>
            ) {
                onUpdate { tab ->
                    tab.copy(url = url ?: "")
                }
            }

            override fun onCanGoBack(s: GeckoSession, canGoBack: Boolean) {
                onUpdate { tab ->
                    tab.copy(canGoBack = canGoBack)
                }
            }

            override fun onCanGoForward(s: GeckoSession, canGoForward: Boolean) {
                onUpdate { tab ->
                    tab.copy(canGoForward = canGoForward)
                }
            }

            override fun onLoadError(
                s: GeckoSession,
                uri: String?,
                error: org.mozilla.geckoview.WebRequestError
            ): org.mozilla.geckoview.GeckoResult<String>? {
                val failingUrl = uri ?: ""
                val html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Failed to load page</title>
                        <style>
                            body { font-family: -apple-system, sans-serif; padding: 24px; text-align: center; color: #333; background-color: #f7f9fa; }
                            .container { max-width: 400px; margin: 50px auto; background: white; padding: 30px; border-radius: 24px; box-shadow: 0 4px 12px rgba(0,0,0,0.05); border: 1.5px solid #eee; }
                            h1 { color: #d9534f; font-size: 20px; margin-top: 0; }
                            p { color: #666; font-size: 14px; line-height: 1.5; }
                            .btn { display: inline-block; background-color: #6200EE; color: white; padding: 10px 20px; border-radius: 20px; text-decoration: none; font-weight: bold; margin-top: 15px; font-size: 14px; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <h1>Unable to connect</h1>
                            <p>We can't load the page. Check your internet connection or the URL address you entered.</p>
                            <p style="font-size: 12px; color: #999; word-break: break-all;">Error: ${error.localizedMessage ?: "Unknown Error"}</p>
                            <a class="btn" href="$failingUrl">Try Again</a>
                        </div>
                    </body>
                    </html>
                """.trimIndent()
                val base64 = android.util.Base64.encodeToString(html.toByteArray(), android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP)
                return org.mozilla.geckoview.GeckoResult.fromValue("data:text/html;base64,$base64")
            }
        }

        // Progress delegate to track page load loading and progress states
        session.progressDelegate = object : GeckoSession.ProgressDelegate {
            override fun onProgressChange(s: GeckoSession, progress: Int) {
                onUpdate { tab ->
                    tab.copy(progress = progress)
                }
            }

            override fun onPageStart(s: GeckoSession, url: String) {
                onUpdate { tab ->
                    tab.copy(isLoading = true, progress = 0)
                }
            }

            override fun onPageStop(s: GeckoSession, success: Boolean) {
                onUpdate { tab ->
                    val finalTitle = if (tab.title == "New Tab" || tab.title.isEmpty() || tab.title == "about:blank") {
                        try {
                            val host = java.net.URL(tab.url).host.removePrefix("www.")
                            if (host.isNotEmpty()) host else "New Tab"
                        } catch (e: Exception) {
                            "New Tab"
                        }
                    } else {
                        tab.title
                    }
                    tab.copy(isLoading = false, progress = 100, title = finalTitle)
                }
            }
        }

        // Content delegate to handle page title updates
        session.contentDelegate = object : GeckoSession.ContentDelegate {
            override fun onTitleChange(s: GeckoSession, title: String?) {
                onUpdate { tab ->
                    val cleanTitle = if (!title.isNullOrEmpty() && title != "about:blank") {
                        title
                    } else {
                        try {
                            val host = java.net.URL(tab.url).host.removePrefix("www.")
                            if (host.isNotEmpty()) host else "New Tab"
                        } catch (e: Exception) {
                            "New Tab"
                        }
                    }
                    tab.copy(title = cleanTitle)
                }
            }
        }

        // Open the session within the global GeckoRuntime
        val runtime = GeckoEngine.getRuntime(context)
        session.open(runtime)

        // Load the initial URI if appropriate
        if (initialUrl != "home" && initialUrl != "about:blank" && initialUrl.isNotEmpty()) {
            session.loadUri(initialUrl)
        }

        // Track and cache the session
        activeSessions[tabId] = session
        return session
    }

    /**
     * Retrieve an active session for the specified tab ID.
     */
    fun getSession(tabId: String): GeckoSession? {
        return activeSessions[tabId]
    }

    /**
     * Check if a session exists for the specified tab ID.
     */
    fun hasSession(tabId: String): Boolean {
        return activeSessions.containsKey(tabId)
    }

    /**
     * Safely closes and removes the session associated with the given tab ID.
     */
    fun removeAndDestroySession(tabId: String) {
        val session = activeSessions.remove(tabId)
        if (session != null) {
            try {
                session.close()
            } catch (e: Exception) {
                // Ignore safe destruction errors
            }
        }
    }

    /**
     * Safely destroys and closes all active sessions (for clean shutdown on browser exit).
     */
    fun destroyAllSessions() {
        val iterator = activeSessions.keys.iterator()
        while (iterator.hasNext()) {
            val tabId = iterator.next()
            val session = activeSessions[tabId]
            if (session != null) {
                try {
                    session.close()
                } catch (e: Exception) {
                    // Ignore safe destruction errors
                }
            }
            iterator.remove()
        }
    }

    /**
     * Returns an immutable copy of the active sessions (for suspension or tracking).
     */
    fun getActiveSessions(): Map<String, GeckoSession> {
        return activeSessions.toMap()
    }
}
