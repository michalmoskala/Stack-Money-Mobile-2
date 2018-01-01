package com.example.zbyszek.stackmoney2.model

import android.content.Context

class Preferences {
    companion object {
        fun setUserId(id: Long, context: Context) {
            val prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE)
            val userIdKey = "com.example.app.userId"
            prefs.edit().putLong(userIdKey, id).apply()
        }

        fun getUserId(context: Context): Long {
            val prefs = context.getSharedPreferences("com.example.app", Context.MODE_PRIVATE)
            val userIdKey = "com.example.app.userId"
            return prefs.getLong(userIdKey, -1)
        }
    }
}