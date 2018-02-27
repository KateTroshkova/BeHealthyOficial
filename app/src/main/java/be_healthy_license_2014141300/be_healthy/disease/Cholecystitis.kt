package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Cholecystitis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.cholecystitis)
        description=res.getString(R.string.cholecystitis_description)
        symptoms= mutableListOf(res.getString(R.string.pain_in_the_right_hypochondrium),
                res.getString(R.string.nausea),
                res.getString(R.string.burp),
                res.getString(R.string.unpleasant_taste_in_the_mouth))
        fillList(activity, treatment, R.array.cholecystitis_treatment)
        fillList(activity, magic, R.array.cholecystitis_magic)
    }
}