package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Myocarditis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.myocarditis)
        description=res.getString(R.string.myocarditis_description)
        symptoms= mutableListOf(res.getString(R.string.dyspnea),
                res.getString(R.string.palpitation),
                res.getString(R.string.heart_pain),
                res.getString(R.string.fever))
        fillList(activity, treatment, R.array.myocarditis_treatment)
        fillList(activity, magic, R.array.myocarditis_magic)
    }
}