package com.nuvio.app

import com.nuvio.app.core.ui.NativeNavigationTab

enum class AppScreenTab {
    Home,
    Search,
    Live,
    Library,
    Settings,
    ;

    companion object {
        fun fromName(name: String): AppScreenTab =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: Home
    }
}

internal fun AppScreenTab.toNativeNavigationTab(): NativeNavigationTab = when (this) {
    AppScreenTab.Home -> NativeNavigationTab.Home
    AppScreenTab.Search -> NativeNavigationTab.Search
    AppScreenTab.Live -> NativeNavigationTab.Home
    AppScreenTab.Library -> NativeNavigationTab.Library
    AppScreenTab.Settings -> NativeNavigationTab.Settings
}

internal fun NativeNavigationTab.toAppScreenTab(): AppScreenTab = when (this) {
    NativeNavigationTab.Home -> AppScreenTab.Home
    NativeNavigationTab.Search -> AppScreenTab.Search
    NativeNavigationTab.Live -> AppScreenTab.Live
    NativeNavigationTab.Library -> AppScreenTab.Library
    NativeNavigationTab.Settings -> AppScreenTab.Settings
}
