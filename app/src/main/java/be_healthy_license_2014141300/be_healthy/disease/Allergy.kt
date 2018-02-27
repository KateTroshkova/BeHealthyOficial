package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Allergy():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.allergy)
        description=res.getString(R.string.allergy_description)
        symptoms= mutableListOf(res.getString(R.string.sneezing),
                res.getString(R.string.runny_nose),
                res.getString(R.string.redness_of_the_eyes),
                res.getString(R.string.lacrimation),
                res.getString(R.string.cough),
                res.getString(R.string.dyspnea),
                res.getString(R.string.asphyxia),
                res.getString(R.string.rash))
        fillList(activity, treatment, R.array.allergy_treatment)
        fillList(activity, magic, R.array.allergy_magic)
    }
}