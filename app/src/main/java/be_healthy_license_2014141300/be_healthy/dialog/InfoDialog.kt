package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class InfoDialog: DialogFragment(){

    private var data=""
    private var header=""

    fun setData(header:String, data:String){
        this.data=data
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.user_terms_item, null)
        (view.findViewById<TextView>(R.id.userterms)).text=data
        dialog.setTitle(header)
        dialog.setView(view)
        dialog.setPositiveButton(activity.resources.getString(R.string.ok), null)
        return dialog.create()
    }
}