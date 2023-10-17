package com.example.yaming

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREF_NAME = "MyAppPrefs"
    private const val TOKEN_KEY = "user_token"

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY) // 토큰 키 제거
        editor.apply()
    }
}

