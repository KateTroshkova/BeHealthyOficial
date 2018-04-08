package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Migraine(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.migraine)
        description=res.getString(R.string.migraine_description)
        symptoms= mutableListOf(res.getString(R.string.headache),
                res.getString(R.string.nausea),
                res.getString(R.string.vomiting),
                res.getString(R.string.photophobia),
                res.getString(R.string.intolerance_to_loud_sounds))
        fillList(activity, treatment, R.array.migraine_treatment)
        fillList(activity, magic, R.array.migraine_magic)
    }
}