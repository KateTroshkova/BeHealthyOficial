package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Arrhythmia(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.arrhythmia)
        description=res.getString(R.string.arrhythmia_description)
        symptoms= mutableListOf(res.getString(R.string.impaired_cardiac_function),
                res.getString(R.string.palpitation),
                res.getString(R.string.heartbeat),
                res.getString(R.string.chest_pain),
                res.getString(R.string.dizziness),
                res.getString(R.string.asphyxia),
                res.getString(R.string.faint))
        fillList(activity, treatment, R.array.arrhythmia_treatment)
        fillList(activity, magic, R.array.arrhythmia_magic)
    }
}