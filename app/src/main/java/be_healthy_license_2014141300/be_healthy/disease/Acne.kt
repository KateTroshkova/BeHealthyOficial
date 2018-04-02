package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease


class Acne (): Disease() {

    constructor(activity: Activity) : this() {
        val res = activity.resources
        name = res.getString(R.string.acne)
        description = res.getString(R.string.acne_description)
        symptoms = mutableListOf(res.getString(R.string.acne_s),
                res.getString(R.string.oily_shine))
        fillList(activity, treatment, R.array.acne_tretment)
        fillList(activity, magic, R.array.acne_magic)
    }
}