package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Measles(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.measles)
        description=res.getString(R.string.myopia_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.headache),
                res.getString(R.string.fever),
                res.getString(R.string.runny_nose),
                res.getString(R.string.cough),
                res.getString(R.string.enlarged_lymph_nodes),
                res.getString(R.string.pain_when_swallowing),
                res.getString(R.string.inflammation_of_the_mucous_membranes_of_the_eyes))
        fillList(activity, treatment, R.array.measles_treatment)
        fillList(activity, magic, R.array.measles_magic)
    }
}