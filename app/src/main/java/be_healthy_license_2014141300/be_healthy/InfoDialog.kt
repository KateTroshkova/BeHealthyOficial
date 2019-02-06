package be_healthy_license_2014141300.be_healthy

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView

class InfoDialog(var layout:Int): DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(layout, null)
        val ok=view.findViewById<TextView>(R.id.ok)
        ok.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                dismiss()
            }
        })
        dialog.setView(view)
        return dialog.create()
    }
}