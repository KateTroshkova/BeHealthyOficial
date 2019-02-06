package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import com.be_healthy_license_2014141300.be_healthy.R
import mehdi.sakout.fancybuttons.FancyButton
import android.net.ConnectivityManager
import android.content.IntentFilter



class ConnectionDialog(): DialogFragment(){

    private var receiver=object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork = cm.activeNetworkInfo;
            var isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting
            if(isConnected){
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        val dialog= AlertDialog.Builder(activity)
        dialog.setCancelable(false)
        dialog.setMessage("Необходимо подключение к интернету")
        var realDialog = dialog.create()
        realDialog.setCanceledOnTouchOutside(false)
        realDialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                return keyCode == KeyEvent.KEYCODE_BACK
            }
        });
        return realDialog
    }
}