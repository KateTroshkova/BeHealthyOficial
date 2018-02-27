package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Caries(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.caries)
        description=res.getString(R.string.caries_description)
        symptoms= mutableListOf(res.getString(R.string.dental_plaque),
                res.getString(R.string.pain_while_taking_acidic_food),
                res.getString(R.string.pain_while_taking_cold_foods),
                res.getString(R.string.pain_while_taking_sweet_food))
        fillList(activity, treatment, R.array.caries_treatment)
        fillList(activity, magic, R.array.caries_magic)
    }
}