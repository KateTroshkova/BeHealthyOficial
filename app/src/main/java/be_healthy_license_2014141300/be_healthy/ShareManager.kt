package be_healthy_license_2014141300.be_healthy

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ShareManager(var context:Context) {

    fun saveUrl(){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("url", "https://play.google.com/store/apps/details?id=be_healthy_license_2014141300.be_healthy")
        clipboard.primaryClip = clip
    }
}