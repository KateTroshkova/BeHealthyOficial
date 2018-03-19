package be_healthy_license_2014141300.be_healthy

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.v4.content.LocalBroadcastManager

class UpdateSender(var context:Context) {

    fun send(action:Int, param:Int, value:Parcelable){
        val intent= Intent(context.resources.getString(action))
        intent.putExtra(context.resources.getString(param), value)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun send(action:Int, param:Int, value:ArrayList<String>){
        val intent= Intent(context.resources.getString(action))
        intent.putExtra(context.resources.getString(param), value)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}