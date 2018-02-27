package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Frontite(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.frontite)
        description=res.getString(R.string.frontite_description)
        symptoms= mutableListOf(res.getString(R.string.headache),
                res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.photophobia),
                res.getString(R.string.lacrimation),
                res.getString(R.string.fever),
                res.getString(R.string.difficulty_breathing_through_the_nose),
                res.getString(R.string.swelling_of_tissues_in_the_corner_of_the_eye),
                res.getString(R.string.swelling_of_the_mucous_membranes_of_the_nose),
                res.getString(R.string.redness_of_the_mucous_membranes_of_the_nose),
                res.getString(R.string.runny_nose))
        fillList(activity, treatment, R.array.frontite_treatment)
        fillList(activity, magic, R.array.frontite_magic)
    }
}