package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView

class UserTermsDialog():DialogFragment(){

    private var data=""

    constructor(data:String):this(){
        this.data=data
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=AlertDialog.Builder(activity)
        val inflater=LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.user_terms_item, null)
        (view.findViewById(R.id.userterms) as CustomSizeTextView).text=data
        dialog.setView(view)
        dialog.setPositiveButton(activity.resources.getString(R.string.accept), null)
        return dialog.create()
    }
}