package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Flatfoot():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.flatfoot)
        description=res.getString(R.string.flatfoot_description)
        symptoms= mutableListOf(res.getString(R.string.foot_fatigue),
                res.getString(R.string.pain_in_the_foot),
                res.getString(R.string.pain_in_the_legs),
                res.getString(R.string.heaviness_in_the_legs),
                res.getString(R.string.cramps_in_the_ankles),
                res.getString(R.string.changing_the_size_of_the_foot),
                res.getString(R.string.inconvenience_in_walking),
                res.getString(R.string.puffiness_of_the_foot))
        fillList(activity, treatment, R.array.flatfoot_treatment)
        fillList(activity, magic, R.array.flatfoot_magic)
    }
}