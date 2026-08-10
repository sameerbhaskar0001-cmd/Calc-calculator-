package com.example

import android.content.Context
import android.util.Log
import android.webkit.CookieManager as WebKitCookieManager
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.StorageController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SecretBrowserPrivacyHelper {
    private val _cookieState = MutableStateFlow("Active")
    val cookieState: StateFlow<String> = _cookieState.asStateFlow()

    /**
     * Clears website cookies for both GeckoView (primary) and Android System WebView (fallback).
     * This provides consistent cookie deletion without deleting other browser state like bookmarks or history.
     */
    fun clearCookies(context: Context, onResult: (Boolean) -> Unit) {
        clearBrowsingData(
            context = context,
            clearHistory = false,
            clearCookies = true,
            clearCache = false,
            clearSiteData = false,
            viewModel = null,
            onResult = onResult
        )
    }

    /**
     * Clears specified browsing data categories (History, Cookies, Cache, Site Data).
     * Supported categories can be cleared individually or in any combination.
     * Guaranteed to preserve Bookmarks, Downloads, and Vault files.
     */
    fun clearBrowsingData(
        context: Context,
        clearHistory: Boolean,
        clearCookies: Boolean,
        clearCache: Boolean,
        clearSiteData: Boolean,
        viewModel: CalculatorViewModel?,
        onResult: (Boolean) -> Unit
    ) {
        var pendingOperations = 0
        var success = true

        val operationDone = { opSuccess: Boolean ->
            if (!opSuccess) success = false
            pendingOperations--
            if (pendingOperations <= 0) {
                onResult(success)
            }
        }

        // 1. Clear History (if selected)
        if (clearHistory && viewModel != null) {
            try {
                viewModel.clearBrowserHistory()
            } catch (e: Exception) {
                Log.e("PrivacyHelper", "Failed to clear browser history", e)
                success = false
            }
        }

        // 2. Clear GeckoView Data (Cookies, Cache, Site Data)
        var geckoFlags = 0L
        if (clearCookies) {
            geckoFlags = geckoFlags or StorageController.ClearFlags.COOKIES
        }
        if (clearCache) {
            geckoFlags = geckoFlags or StorageController.ClearFlags.NETWORK_CACHE or StorageController.ClearFlags.IMAGE_CACHE
        }
        if (clearSiteData) {
            geckoFlags = geckoFlags or StorageController.ClearFlags.DOM_STORAGES
        }

        if (geckoFlags != 0L) {
            pendingOperations++
            try {
                val runtime = GeckoEngine.getRuntime(context)
                runtime.storageController.clearData(geckoFlags)
                    .then(
                        org.mozilla.geckoview.GeckoResult.OnValueListener {
                            if (clearCookies) {
                                _cookieState.value = "Cleared"
                            }
                            operationDone(true)
                            org.mozilla.geckoview.GeckoResult.fromValue(null)
                        },
                        org.mozilla.geckoview.GeckoResult.OnExceptionListener { throwable ->
                            Log.e("PrivacyHelper", "Failed to clear GeckoView data", throwable)
                            operationDone(false)
                            org.mozilla.geckoview.GeckoResult.fromValue(null)
                        }
                    )
            } catch (e: Exception) {
                Log.e("PrivacyHelper", "GeckoView clearData failed or GeckoView not active", e)
                operationDone(false)
            }
        }

        // 3. Clear WebView Fallback Data
        // WebView Cookies
        if (clearCookies) {
            pendingOperations++
            try {
                val webKitCookieManager = WebKitCookieManager.getInstance()
                webKitCookieManager.removeAllCookies { result ->
                    if (result) {
                        _cookieState.value = "Cleared"
                    }
                    webKitCookieManager.flush()
                    operationDone(result)
                }
            } catch (e: Exception) {
                Log.e("PrivacyHelper", "Failed to clear WebView cookies", e)
                operationDone(false)
            }
        }

        // WebView Cache
        if (clearCache) {
            try {
                if (context.cacheDir.exists()) {
                    fun deleteCacheContents(dir: java.io.File) {
                        dir.listFiles()?.forEach { file ->
                            if (!file.name.equals("WebView", ignoreCase = true) && 
                                !file.name.contains("webview", ignoreCase = true)) {
                                file.deleteRecursively()
                            }
                        }
                    }
                    deleteCacheContents(context.cacheDir)
                }
                if (context.codeCacheDir.exists()) {
                    fun deleteCacheContents(dir: java.io.File) {
                        dir.listFiles()?.forEach { file ->
                            if (!file.name.equals("WebView", ignoreCase = true) && 
                                !file.name.contains("webview", ignoreCase = true)) {
                                file.deleteRecursively()
                            }
                        }
                    }
                    deleteCacheContents(context.codeCacheDir)
                }
            } catch (e: Exception) {
                Log.e("PrivacyHelper", "Failed to clear WebView file caches", e)
            }
        }

        // WebView Site Data
        if (clearSiteData) {
            try {
                android.webkit.WebStorage.getInstance().deleteAllData()
            } catch (e: Exception) {
                Log.e("PrivacyHelper", "Failed to clear WebStorage site data", e)
            }
        }

        // 4. Handle sync completion if no async operations were triggered
        if (pendingOperations == 0) {
            onResult(success)
        }
    }

    /**
     * Marks the cookie state as active when a new navigation or session begins.
     */
    fun markActive() {
        _cookieState.value = "Active"
    }
}
