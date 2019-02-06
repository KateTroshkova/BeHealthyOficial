package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class IMBDialog: DialogFragment(){

    private var result=""
    private var diagnos=""
    private var advice=""

    fun setData(result:String, diagnos:String, advice:String){
        this.result=result
        this.diagnos=diagnos
        this.advice=advice
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.n_dialog_imb, null)
        (view.findViewById<TextView>(R.id.textView12)).text=result
        (view.findViewById<TextView>(R.id.textView13)).text=diagnos
        (view.findViewById<TextView>(R.id.textView11)).text=advice
        (view.findViewById<TextView>(R.id.ok)).setOnClickListener {
            dismiss()
        }
        dialog.setView(view)
        return dialog.create()
    }
}