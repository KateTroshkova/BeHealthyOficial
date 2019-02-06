package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.be_healthy_license_2014141300.be_healthy.R
import mehdi.sakout.fancybuttons.FancyButton

class RateAppDialog(var rateListener:OnRateListener, var laterListener:OnLaterListener): DialogFragment(){

    interface OnRateListener {
        fun onRate()
    }

    interface OnLaterListener{
        fun onLater()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.n_dialog_rate, null)
        val ok=view.findViewById<FancyButton>(R.id.rate)
        val later=view.findViewById<FancyButton>(R.id.later)
        val never=view.findViewById<FancyButton>(R.id.never)
        ok.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                rateListener.onRate()
            }
        })
        later.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                laterListener.onLater()
                dismiss()
            }
        })
        never.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                laterListener.onLater()
                dismiss()
            }
        })
        dialog.setView(view)
        return dialog.create()
    }
}