package be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.R

class DeleteDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog=AlertDialog.Builder(activity)
        dialog.setPositiveButton(activity.resources.getString(R.string.yes)) { p0, p1 ->
            DB_Operation(activity).clearHistory()
            Toast.makeText(activity, R.string.deleted, Toast.LENGTH_SHORT).show()
        }
        dialog.setNegativeButton(activity.resources.getString(R.string.no), null)
        dialog.setMessage(R.string.sure_delete)
        return dialog.create()
    }
}