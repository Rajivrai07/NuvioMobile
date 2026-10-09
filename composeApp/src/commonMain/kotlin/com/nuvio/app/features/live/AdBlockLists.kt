package com.nuvio.app.features.live

/**
 * Ad-block lists for the LIVE WebView.
 * Blocks known ad/tracker/popup domains at network level.
 */
internal object AdBlockLists {
    val blockedDomains = listOf(
        // Major ad networks
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "google-analytics.com",
        "adservice.google.com",
        "pagead2.googlesyndication.com",
        // Popup/popunder networks (common on streaming sites)
        "popads.net",
        "popcash.net",
        "adcash.com",
        "propellerads.com",
        "adsterra.com",
        "exoclick.com",
        "hilltopads.net",
        "clickadu.com",
        "mgid.com",
        "revcontent.com",
        "outbrain.com",
        "taboola.com",
        // Misc trackers
        "facebook.net",
        "hotjar.com",
        "criteo.com",
    )

    fun isAdUrl(url: String): Boolean {
        val lower = url.lowercase()
        return blockedDomains.any { lower.contains(it) }
    }
}
