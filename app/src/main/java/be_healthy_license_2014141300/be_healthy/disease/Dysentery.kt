package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Dysentery (): Disease() {

    constructor(activity: Activity) : this() {
        val res = activity.resources
        name = res.getString(R.string.dysentery)
        description = res.getString(R.string.dysentery_description)
        symptoms = mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.fever),
                res.getString(R.string.chills),
                res.getString(R.string.loss_of_appetite),
                res.getString(R.string.headache),
                res.getString(R.string.pressure_reduction),
                res.getString(R.string.stomach_ache),
                res.getString(R.string.diarrhea))
        fillList(activity, treatment, R.array.acne_tretment)
        fillList(activity, magic, R.array.acne_magic)
    }
}