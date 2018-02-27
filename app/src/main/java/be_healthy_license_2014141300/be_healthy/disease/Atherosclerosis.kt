package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Atherosclerosis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.atherosclerosis)
        description=res.getString(R.string.atherosclerosis_description)
        symptoms= mutableListOf(res.getString(R.string.chest_pain),
                res.getString(R.string.difficulty_in_swallowing),
                res.getString(R.string.hoarseness),
                res.getString(R.string.high_blood_pressure),
                res.getString(R.string.stomach_ache),
                res.getString(R.string.limp),
                res.getString(R.string.abnormality_of_the_legs),
                res.getString(R.string.pallor),
                res.getString(R.string.dizziness),
                res.getString(R.string.noise_in_ears),
                res.getString(R.string.forgetfulness),
                res.getString(R.string.pain_in_the_legs),
                res.getString(R.string.numbness_of_the_legs),
                res.getString(R.string.convulsions))
        fillList(activity, treatment, R.array.atherosclerosis_treatment)
        fillList(activity, magic, R.array.atherosclerosis_magic)
    }
}