package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Stomatitis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.stomatitis)
        description=res.getString(R.string.stomatitis_description)
        symptoms= mutableListOf(res.getString(R.string.formation_of_sores_in_the_mouth),
                res.getString(R.string.burning),
                res.getString(R.string.pain_while_taking_acidic_food),
                res.getString(R.string.pain_while_taking_cold_foods),
                res.getString(R.string.pain_while_taking_sweet_food),
                res.getString(R.string.common_ailment))
        fillList(activity, treatment, R.array.stomatitis_treatment)
        fillList(activity, magic, R.array.stomatitis_magic)
    }
}