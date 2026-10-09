package com.nuvio.app.features.live

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun LiveStreamWebView(
    url: String,
    modifier: Modifier,
) {
    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration().apply {
                allowsInlineMediaPlayback = true
            }
            WKWebView().apply {
                setConfiguration(config)
                NSURL(string = url)?.let { loadRequest(NSURLRequest.requestWithURL(it)) }
            }
        },
        update = { webView ->
            val current = webView.URL?.absoluteString
            if (current != url) {
                NSURL(string = url)?.let { webView.loadRequest(NSURLRequest.requestWithURL(it)) }
            }
        },
    )
}
