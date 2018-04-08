package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Osteochondrosis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.osteochondrosis)
        description=res.getString(R.string.osteochondrosis_description)
        symptoms= mutableListOf(res.getString(R.string.backache),
                res.getString(R.string.crunching_in_the_back),
                res.getString(R.string.mobility_impairment),
                res.getString(R.string.shivers))
        fillList(activity, treatment, R.array.osteochondrosis_treatment)
        fillList(activity, magic, R.array.osteochondrosis_magic)
    }
}