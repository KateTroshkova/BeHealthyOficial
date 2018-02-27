package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Scurvy():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.scurvy)
        description=res.getString(R.string.scurvy_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.fatigue),
                res.getString(R.string.dizziness),
                res.getString(R.string.joint_pain),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.bleeding_gums),
                res.getString(R.string.chewing_pain))
        fillList(activity, treatment, R.array.scurvy_treatment)
        fillList(activity, magic, R.array.scurvy_magic)
    }
}