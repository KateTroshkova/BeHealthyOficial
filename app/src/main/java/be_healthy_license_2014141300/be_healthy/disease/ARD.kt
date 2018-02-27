package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class ARD(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.ard)
        description=res.getString(R.string.ard_description)
        symptoms= mutableListOf(res.getString(R.string.runny_nose),
                res.getString(R.string.cough),
                res.getString(R.string.sore_throat),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.redness_in_the_throat),
                res.getString(R.string.hoarseness),
                res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.loss_of_appetite),
                res.getString(R.string.headache),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.enlarged_lymph_nodes),
                res.getString(R.string.rash))
        fillList(activity, treatment, R.array.ard_treatment)
        fillList(activity, magic, R.array.ard_magic)
    }
}