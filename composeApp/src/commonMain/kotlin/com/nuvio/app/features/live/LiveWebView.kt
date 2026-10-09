package com.nuvio.app.features.live

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Smartcric live cricket site, shown inside the app. */
internal const val SMARTCRIC_URL = "https://smartcric.is/"

@Composable
internal expect fun LiveStreamWebView(
    url: String,
    modifier: Modifier,
)
