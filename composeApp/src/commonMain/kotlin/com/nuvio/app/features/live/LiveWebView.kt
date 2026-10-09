package com.nuvio.app.features.live

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun LiveStreamWebView(
    url: String,
    modifier: Modifier = Modifier,
)
