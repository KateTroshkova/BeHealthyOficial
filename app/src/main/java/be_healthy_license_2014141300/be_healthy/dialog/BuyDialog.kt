package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import mehdi.sakout.fancybuttons.FancyButton

class BuyDialog(var listener:OnAcceptListener):DialogFragment(){

    interface OnAcceptListener {
        fun OnAcceptMonth()
        fun OnAcceptYear()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        dialog.setCancelable(false)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.n_dialog_buy, null)
        val ok=view.findViewById<FancyButton>(R.id.start)
        ok.setOnClickListener {
            listener.OnAcceptMonth()
            dismiss()
        }
        val year=view.findViewById<FancyButton>(R.id.year)
        year.setOnClickListener {
            listener.OnAcceptYear()
            dismiss()
        }
        dialog.setView(view)
        var realDialog = dialog.create()
        realDialog.setCanceledOnTouchOutside(false)
        realDialog.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                // Prevent dialog close on back press button
                return keyCode == KeyEvent.KEYCODE_BACK
            }
        });
        return realDialog
    }

}