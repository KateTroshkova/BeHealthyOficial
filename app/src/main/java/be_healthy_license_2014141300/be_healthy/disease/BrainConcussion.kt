package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class BrainConcussion():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.brain_concussion)
        description=res.getString(R.string.brain_description)
        symptoms= mutableListOf(res.getString(R.string.headache),
                res.getString(R.string.nausea),
                res.getString(R.string.dizziness),
                res.getString(R.string.common_ailment),
                res.getString(R.string.noise_in_ears),
                res.getString(R.string.sweating),
                res.getString(R.string.sleep_disorders))
        fillList(activity, treatment, R.array.brain_treatment)
        fillList(activity, magic, R.array.brain_magic)
    }
}