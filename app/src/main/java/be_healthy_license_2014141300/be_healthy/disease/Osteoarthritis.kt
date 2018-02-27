package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Osteoarthritis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.osteoarthritis)
        description=res.getString(R.string.osteoarthritis_description)
        symptoms= mutableListOf(res.getString(R.string.joint_pain),
                res.getString(R.string.deformation_of_joints),
                res.getString(R.string.swelling_of_the_joints))
        fillList(activity, treatment, R.array.osteoarthritis_treatment)
        fillList(activity, magic, R.array.osteoarthritis_magic)
    }
}