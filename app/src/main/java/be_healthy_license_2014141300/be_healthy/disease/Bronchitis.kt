package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Bronchitis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.bronchitis)
        description=res.getString(R.string.bronchitis_description)
        symptoms= mutableListOf(res.getString(R.string.cough),
                res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.joint_pain))
        fillList(activity, treatment, R.array.bronchitis_treatment)
        fillList(activity, magic, R.array.bronchitis_magic)
    }
}