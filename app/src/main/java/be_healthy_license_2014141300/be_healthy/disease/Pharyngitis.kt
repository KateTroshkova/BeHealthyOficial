package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Pharyngitis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.pharyngitis)
        description=res.getString(R.string.pharyngitis_description)
        symptoms= mutableListOf(res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.headache),
                res.getString(R.string.sore_throat),
                res.getString(R.string.pain_when_swallowing),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.dryness_in_the_throat),
                res.getString(R.string.increased_fatigue),
                res.getString(R.string.redness_of_the_posterior_pharyngeal_wall))
        fillList(activity, treatment, R.array.pharyngitis_treatment)
        fillList(activity, magic, R.array.pharyngitis_magic)
    }
}