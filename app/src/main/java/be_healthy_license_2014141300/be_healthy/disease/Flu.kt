package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Flu(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.flu)
        description=res.getString(R.string.flu_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.fever),
                res.getString(R.string.chills),
                res.getString(R.string.redness_in_the_throat),
                res.getString(R.string.sore_throat),
                res.getString(R.string.headache),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.nausea),
                res.getString(R.string.loss_of_appetite),
                res.getString(R.string.runny_nose),
                res.getString(R.string.cough),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.lacrimation),
                res.getString(R.string.hoarseness),
                res.getString(R.string.stuffy_ears),
                res.getString(R.string.nosebleeds),
                res.getString(R.string.disturbance_of_consciousness),
                res.getString(R.string.cramps))
        fillList(activity, treatment, R.array.flu_treatment)
        fillList(activity, magic, R.array.flu_magic)
    }
}