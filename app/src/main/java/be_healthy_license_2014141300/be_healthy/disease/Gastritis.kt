package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Gastritis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.gastritis)
        description=res.getString(R.string.gastritis_description)
        symptoms= mutableListOf(res.getString(R.string.stomach_ache),
                res.getString(R.string.nausea),
                res.getString(R.string.burp),
                res.getString(R.string.unpleasant_taste_in_the_mouth),
                res.getString(R.string.common_ailment),
                res.getString(R.string.fever),
                res.getString(R.string.vomiting))
        fillList(activity, treatment, R.array.gastritis_treatment)
        fillList(activity, magic, R.array.gastritis_magic)
    }
}