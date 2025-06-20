package com.pika.pintulogika.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences("onboarding_pref", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean("IsFirstTimeLaunch", isFirstTime)
        editor.apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean("IsFirstTimeLaunch", true) // default TRUE
    }
}

