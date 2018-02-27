package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Otitis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.otitis)
        description=res.getString(R.string.otitis_description)
        symptoms= mutableListOf(res.getString(R.string.ear_pain),
                res.getString(R.string.redness_of_the_ear),
                res.getString(R.string.swelling_of_the_ear),
                res.getString(R.string.pain_when_opening_mouth),
                res.getString(R.string.stuffy_ears),
                res.getString(R.string.hearing_loss),
                res.getString(R.string.noise_in_ears),
                res.getString(R.string.dizziness))
        fillList(activity, treatment, R.array.otitis_treatment)
        fillList(activity, magic, R.array.otitis_magic)
    }
}