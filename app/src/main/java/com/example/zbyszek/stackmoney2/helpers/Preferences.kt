package com.example.zbyszek.stackmoney2.helpers

import android.content.Context

class Preferences {
    companion object {
        val userIdKey = "com.example.app.userId"

        fun setUserId(id: Long, context: Context) {
            val prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE)
            prefs.edit().putLong(userIdKey, id).apply()
        }

        fun resetUserId(context: Context) {
            val prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE)
            prefs.edit().remove(userIdKey)
        }

        fun getUserId(context: Context): Long {
            val prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE)
            return prefs.getLong(userIdKey, -1)
        }
    }
}