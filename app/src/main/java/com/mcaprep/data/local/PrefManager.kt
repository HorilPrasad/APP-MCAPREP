package com.mcaprep.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class PrefManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val PREF_IS_LOGGED_IN = "pref_is_logged_in"
        private const val PREF_AUTH_TOKEN = "pref_auth_token"
        private const val PREF_USERNAME = "pref_username"
        private const val PREF_USER_ID = "pref_user_id"
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(PREF_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit() { putBoolean(PREF_IS_LOGGED_IN, isLoggedIn) }
    }

    fun loggedOut() {
        sharedPreferences.edit() { clear() }
    }

    fun setAuthToken(token: String) {
        sharedPreferences.edit() { putString(PREF_AUTH_TOKEN, token) }
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(PREF_AUTH_TOKEN, null)
    }

    fun setUsername(username: String) {
        sharedPreferences.edit() { putString(PREF_USERNAME, username) }
    }

    fun getUsername(): String {
        return sharedPreferences.getString(PREF_USERNAME, "") ?: ""
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit() { putString(PREF_USER_ID, userId) }
    }

    fun getUserId(): String {
        return sharedPreferences.getString(PREF_USER_ID, "") ?: ""
    }
}