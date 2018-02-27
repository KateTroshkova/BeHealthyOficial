package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Hypertension(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.hypertension)
        description=res.getString(R.string.hypertension_description)
        symptoms= mutableListOf(res.getString(R.string.headache),
                res.getString(R.string.dizziness),
                res.getString(R.string.blurred_vision),
                res.getString(R.string.palpitation),
                res.getString(R.string.heart_pain),
                res.getString(R.string.dyspnea))
        fillList(activity, treatment, R.array.hypertension_treatment)
        fillList(activity, magic, R.array.hypertension_magic)
    }
}