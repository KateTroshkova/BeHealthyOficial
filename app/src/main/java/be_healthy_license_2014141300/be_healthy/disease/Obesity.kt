package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Obesity():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.obesity)
        description=res.getString(R.string.obesity_description)
        symptoms= mutableListOf(res.getString(R.string.weight_gain),
                res.getString(R.string.dyspnea),
                res.getString(R.string.high_blood_pressure),
                res.getString(R.string.heart_pain),
                res.getString(R.string.joint_pain))
        fillList(activity, treatment, R.array.obesity_treatment)
        fillList(activity, magic, R.array.obesity_magic)
    }
}