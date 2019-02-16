package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class InfoDialog: DialogFragment(){

    private var layout:Int=0

    fun setData(layout:Int){
        this.layout=layout
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(layout, null)
        val ok=view.findViewById<TextView>(R.id.ok)
        ok.setOnClickListener { dismiss() }
        dialog.setView(view)
        return dialog.create()
    }
}