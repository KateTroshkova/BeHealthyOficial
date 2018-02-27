package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Laryngitis():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.laryngitis)
        description=res.getString(R.string.laryngitis_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.fever),
                res.getString(R.string.pain_when_swallowing),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.voice_lost),
                res.getString(R.string.dyspnea),
                res.getString(R.string.cough))
        fillList(activity, treatment, R.array.laryngitis_treatment)
        fillList(activity, magic, R.array.laryngitis_magic)
    }
}