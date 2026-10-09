package com.nuvio.app.features.live

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * LIVE tab: Smartcric live cricket inside the app,
 * with full ad blocking (network + DOM level).
 */
@Composable
internal fun LiveScreen(
    modifier: Modifier = Modifier,
) {
    LiveStreamWebView(
        url = SMARTCRIC_URL,
        modifier = modifier.fillMaxSize(),
    )
}
