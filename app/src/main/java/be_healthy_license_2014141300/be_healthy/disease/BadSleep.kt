package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class BadSleep(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.sleep_disorders)
        description=res.getString(R.string.sleep_description)
        symptoms= mutableListOf(res.getString(R.string.feeling_of_insufficiency_of_sleep),
                res.getString(R.string.inability_to_fall_asleep_at_the_usual_time),
                res.getString(R.string.anxiety),
                res.getString(R.string.disturbing_sleep),
                res.getString(R.string.frequent_awakenings),
                res.getString(R.string.no_recovery_after_waking_up),
                res.getString(R.string.drowsiness),
                res.getString(R.string.fatigue))
        fillList(activity, treatment, R.array.sleep_treatment)
        fillList(activity, magic, R.array.sleep_magic)
    }
}