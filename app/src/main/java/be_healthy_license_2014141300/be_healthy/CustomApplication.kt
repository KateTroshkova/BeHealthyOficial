package com.be_healthy_license_2014141300.be_healthy

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class CustomApplication: Application() {

    var size_coef=1f

    override fun onCreate() {
        super.onCreate()
        val settings=getSharedPreferences(applicationContext.resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        size_coef=settings.getFloat(applicationContext.resources.getString(R.string.param_size), 1f)
    }
}