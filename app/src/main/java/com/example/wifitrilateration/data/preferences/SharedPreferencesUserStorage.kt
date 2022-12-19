package com.example.wifitrilateration.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.wifitrilateration.domain.entity.TokenPair
import javax.inject.Inject

open class SharedPreferencesUserStorage @Inject constructor(context: Context) {
    private val userPref: SharedPreferences

    init {
        userPref = context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    }

    fun getTokenPair(): TokenPair? {
        val expiresAt = getExpiresAt()
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        val userId = getUserId()
        val displayName = getDisplayName()
        val role = getRole()
        val group = getGroup()
        if (expiresAt != null && accessToken != null && refreshToken != null
            && userId != null && displayName != null && role != null && group != null
        ) {
            return TokenPair(accessToken, refreshToken, expiresAt, userId, displayName,role, group)
        } else {
            return null
        }
    }

    fun setTokenPair(tokenPair: TokenPair) {
        userPref.edit().putString(PREF_KEY_ACCESS_TOKEN, tokenPair.accessToken).apply()
        userPref.edit().putString(PREF_KEY_REFRESH_TOKEN, tokenPair.refreshToken).apply()
        userPref.edit().putLong(PREF_KEY_EXPIRES_AT, tokenPair.expiresAt).apply()
        userPref.edit().putLong(PREF_KEY_ID, tokenPair.userId).apply()
        userPref.edit().putString(PREF_KEY_DISPLAY_NAME, tokenPair.displayName).apply()
        userPref.edit().putString(PREF_KEY_ROLE, tokenPair.role).apply()
        userPref.edit().putString(PREF_KEY_GROUP, tokenPair.group).apply()
    }

    fun clear() {
        userPref.edit().remove(PREF_KEY_ACCESS_TOKEN).apply()
        userPref.edit().remove(PREF_KEY_REFRESH_TOKEN).apply()
        userPref.edit().remove(PREF_KEY_EXPIRES_AT).apply()
        userPref.edit().remove(PREF_KEY_ID).apply()
        userPref.edit().remove(PREF_KEY_DISPLAY_NAME).apply()
        userPref.edit().remove(PREF_KEY_ROLE).apply()
        userPref.edit().remove(PREF_KEY_GROUP).apply()
    }

    fun getExpiresAt(): Long? {
        val expiresAt = userPref.getLong(PREF_KEY_EXPIRES_AT, 0)
        if (expiresAt < System.currentTimeMillis()) {
            clear()
            return null
        }
        return expiresAt
    }

    fun getRefreshToken(): String? {
        val refresh = userPref.getString(PREF_KEY_REFRESH_TOKEN, "")
        return if (refresh != "") refresh else null
    }

    fun getAccessToken(): String? {
        val access = userPref.getString(PREF_KEY_ACCESS_TOKEN, "")
        return if (access != "") access else null
    }

    fun getUserId(): Long {
        return userPref.getLong(PREF_KEY_ID, 0)
    }
    fun getDisplayName(): String? {
        val displayName = userPref.getString(PREF_KEY_DISPLAY_NAME, "")
        return if (displayName != "") displayName else null
    }
    fun getRole(): String? {
        val role = userPref.getString(PREF_KEY_ROLE, "")
        return if (role != "") role else null
    }
    fun getGroup(): String? {
        val group = userPref.getString(PREF_KEY_GROUP, "")
        return if (group != "") group else null
    }


    companion object {
        private const val PREF_PACKAGE_NAME = "com.glebalekseevjk.wifitrilateration"
        private val PREF_KEY_ACCESS_TOKEN = "access_token"
        private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
        private val PREF_KEY_GROUP = "group"
        private val PREF_KEY_ROLE = "role"
        private val PREF_KEY_EXPIRES_AT = "expires_at"
        private val PREF_KEY_ID = "id"
        private val PREF_KEY_DISPLAY_NAME = "display_name"
    }
}