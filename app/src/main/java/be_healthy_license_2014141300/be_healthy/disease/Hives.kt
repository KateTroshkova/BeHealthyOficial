package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Hives():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.hives)
        description=res.getString(R.string.hives_description)
        symptoms= mutableListOf(res.getString(R.string.redness_of_the_skin),
                res.getString(R.string.rash),
                res.getString(R.string.itching),
                res.getString(R.string.blisters),
                res.getString(R.string.swelling_of_the_skin))
        fillList(activity, treatment, R.array.hives_treatment)
        fillList(activity, magic, R.array.hives_magic)
    }
}