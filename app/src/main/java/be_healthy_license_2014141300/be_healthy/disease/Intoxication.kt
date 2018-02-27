package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Intoxication():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.intoxication)
        description=res.getString(R.string.intoxication_description)
        symptoms= mutableListOf(res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.loss_of_appetite),
                res.getString(R.string.stomach_ache),
                res.getString(R.string.diarrhea),
                res.getString(R.string.bloating),
                res.getString(R.string.nausea),
                res.getString(R.string.sweating),
                res.getString(R.string.pressure_reduction))
        fillList(activity, treatment, R.array.intoxication_treatment)
        fillList(activity, magic, R.array.intoxication_magic)
    }
}