package com.nuvio.app.features.live

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.io.ByteArrayInputStream

/**
 * Android WebView with full ad blocking:
 * 1. Network level: blocks requests to known ad/tracker/popup domains.
 * 2. DOM level: injected JS removes ad elements, kills popups, and
 *    watches for new ad nodes via MutationObserver.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
internal actual fun LiveStreamWebView(
    url: String,
    modifier: Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                // Block popups from opening new windows; keep everything in this view.
                settings.setSupportMultipleWindows(false)
                settings.javaScriptCanOpenWindowsAutomatically = false

                webViewClient = object : WebViewClient() {
                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?,
                    ): WebResourceResponse? {
                        val reqUrl = request?.url?.toString().orEmpty()
                        if (AdBlockLists.isAdUrl(reqUrl)) {
                            return WebResourceResponse(
                                "text/plain", "utf-8", 200, "OK",
                                emptyMap(), ByteArrayInputStream(ByteArray(0)),
                            )
                        }
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        view?.evaluateJavascript(AD_CLEANUP_JS, null)
                    }
                }
                webChromeClient = WebChromeClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
    )
}

/**
 * Removes ad overlays, banners, iframes and hijacks popup attempts.
 * Re-runs on DOM mutations so dynamically injected ads are cleaned too.
 */
private const val AD_CLEANUP_JS = """(function() {
  // Kill popup attempts
  window.open = function() { return null; };
  // Remove common ad containers
  var selectors = [
    'iframe[src*="doubleclick"]', 'iframe[src*="googlesyndication"]',
    'iframe[src*="adsterra"]', 'iframe[src*="popads"]', 'iframe[src*="popcash"]',
    'iframe[src*="propellerads"]', 'iframe[src*="exoclick"]', 'iframe[src*="hilltopads"]',
    'div[id*="banner-ad"]', 'div[class*="banner-ad"]', 'div[id*="popup"]',
    'div[class*="popup-overlay"]', '.ad-container', '#ad-container', '.advertisement'
  ];
  function clean() {
    try {
      selectors.forEach(function(s) {
        document.querySelectorAll(s).forEach(function(el) { el.remove(); });
      });
    } catch (e) {}
  }
  clean();
  try {
    new MutationObserver(clean).observe(document.documentElement, { childList: true, subtree: true });
  } catch (e) {}
  // Auto-retry a few times for late-loading ads
  var n = 0;
  var t = setInterval(function() { clean(); if (++n > 10) clearInterval(t); }, 1000);
})();"""
