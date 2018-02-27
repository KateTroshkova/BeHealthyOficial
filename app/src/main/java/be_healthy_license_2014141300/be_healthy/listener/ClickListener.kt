package com.be_healthy_license_2014141300.be_healthy.listener

import android.view.View

interface ClickListener {
    fun onClick(view: android.view.View, position: Int)
    fun onLongClick(view: android.view.View, position: Int)
}