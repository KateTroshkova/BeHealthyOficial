package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import com.be_healthy_license_2014141300.be_healthy.R
import mehdi.sakout.fancybuttons.FancyButton

class FirstBuyDialog:DialogFragment(){

    private var listener:OnNewAcceptListener?=null

    interface OnNewAcceptListener {
        fun onNewAccept()
    }

    fun setListener(listener:OnNewAcceptListener){
        this.listener=listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        dialog.setCancelable(false)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.n_dialog_new_buy, null)
        val ok=view.findViewById<FancyButton>(R.id.start)
        ok.setOnClickListener {
            listener?.onNewAccept()
            dismiss()
        }
        dialog.setView(view)
        val realDialog = dialog.create()
        realDialog.setCanceledOnTouchOutside(false)
        realDialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                return keyCode == KeyEvent.KEYCODE_BACK
            }
        })
        return realDialog
    }
}