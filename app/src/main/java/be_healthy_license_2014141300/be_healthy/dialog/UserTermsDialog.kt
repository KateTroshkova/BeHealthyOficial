package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView

class UserTermsDialog:DialogFragment(){

    private var data=""

    fun setData(data:String){
        this.data=data
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=AlertDialog.Builder(activity)
        val inflater=LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.n_dialog_user_terms, null)
        (view.findViewById<TextView>(R.id.textView)).text=data
        val ok=view.findViewById<TextView>(R.id.ok)
        ok.setOnClickListener { dismiss() }
        dialog.setView(view)
        return dialog.create()
    }
}