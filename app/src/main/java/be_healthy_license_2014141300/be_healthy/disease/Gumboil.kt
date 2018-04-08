package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Gumboil(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.gumboil)
        description=res.getString(R.string.gumboil_description)
        symptoms= mutableListOf(res.getString(R.string.gum_disease),
                res.getString(R.string.pain_in_the_gums),
                res.getString(R.string.fever))
        fillList(activity, treatment, R.array.gumboil_treatment)
        fillList(activity, magic, R.array.gumboil_magic)
    }
}