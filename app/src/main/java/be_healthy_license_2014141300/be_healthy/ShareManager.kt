package com.be_healthy_license_2014141300.be_healthy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ShareManager(var context:Context) {

    fun saveUrl(){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("url = ", "google play uri")
        clipboard.primaryClip = clip
    }
}