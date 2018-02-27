package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Sprain(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.sprain)
        description=res.getString(R.string.sprain_description)
        symptoms= mutableListOf(res.getString(R.string.swelling_of_the_joints),
                res.getString(R.string.pain_in_the_moving_parts_of_the_limbs),
                res.getString(R.string.inflammation_of_ligaments))
        fillList(activity, treatment, R.array.sprain_treatment)
        fillList(activity, magic, R.array.sprain_magic)
    }
}