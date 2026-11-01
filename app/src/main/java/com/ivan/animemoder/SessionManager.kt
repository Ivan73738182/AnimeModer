package com.ivan.animemoder

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("AnimeModerPrefs", Context.MODE_PRIVATE)

    fun saveLogin() {
        prefs.edit().putBoolean("is_logged_in", true).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
