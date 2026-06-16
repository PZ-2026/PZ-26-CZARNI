package com.example.magazyn.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.magazyn.BuildConfig

object AppSettings {
    private const val PREFS_NAME = "app_settings"
    const val KEY_DARK_MODE = "dark_mode"
    const val KEY_AUTO_LOGIN = "auto_login"
    const val KEY_NOTIFICATIONS = "notifications"
    const val KEY_BACKEND_URL = "backend_url"

    val DEFAULT_BACKEND_URL: String = BuildConfig.BACKEND_URL

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isDarkMode(context: Context): Boolean = prefs(context).getBoolean(KEY_DARK_MODE, false)
    fun setDarkMode(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun isAutoLogin(context: Context): Boolean = prefs(context).getBoolean(KEY_AUTO_LOGIN, true)
    fun setAutoLogin(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_AUTO_LOGIN, enabled).apply()
    }

    fun isNotificationsEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_NOTIFICATIONS, true)

    fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_NOTIFICATIONS, enabled).apply()
    }

    fun normalizeBackendUrl(url: String): String {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return DEFAULT_BACKEND_URL

        val withScheme = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            trimmed
        } else {
            "http://$trimmed"
        }

        val withoutTrailingSlash = withScheme.trimEnd('/')
        val portPattern = Regex(""":\d+($|/)""")
        val withPort = if (portPattern.containsMatchIn(withoutTrailingSlash.removePrefix("http://").removePrefix("https://"))) {
            withoutTrailingSlash
        } else {
            "$withoutTrailingSlash:8080"
        }

        return "$withPort/"
    }

    fun getBackendUrl(context: Context): String =
        normalizeBackendUrl(prefs(context).getString(KEY_BACKEND_URL, DEFAULT_BACKEND_URL) ?: DEFAULT_BACKEND_URL)

    fun setBackendUrl(context: Context, url: String) {
        prefs(context).edit().putString(KEY_BACKEND_URL, normalizeBackendUrl(url)).apply()
    }
}
