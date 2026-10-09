package com.nuvio.app.features.live

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

/**
 * iOS WKWebView placeholder. Ad-blocking on iOS needs content-blocker
 * rules; full parity with Android is a follow-up.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun LiveStreamWebView(
    url: String,
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                val config = WKWebViewConfiguration()
                WKWebView(frame = platform.CoreGraphics.CGRectZero.readValue(), configuration = config).apply {
                    loadRequest(platform.Foundation.NSURLRequest.requestWithURL(platform.Foundation.NSURL.URLWithString(url)!!))
                }
            },
        )
    }
}
