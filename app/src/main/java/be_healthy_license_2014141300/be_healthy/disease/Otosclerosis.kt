package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Otosclerosis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.otosclerosis)
        description=res.getString(R.string.otosclerosis_description)
        symptoms= mutableListOf(res.getString(R.string.hearing_loss),
                res.getString(R.string.improved_hearing_in_a_noisy_environment),
                res.getString(R.string.poor_perception_of_speech_when_swallowing_and_chewing),
                res.getString(R.string.noise_in_ears),
                res.getString(R.string.dizziness),
                res.getString(R.string.nausea),
                res.getString(R.string.ear_pain),
                res.getString(R.string.tingling_in_the_ears),
                res.getString(R.string.headache),
                res.getString(R.string.forgetfulness))
        fillList(activity, treatment, R.array.otosclerosis_treatment)
        fillList(activity, magic, R.array.otosclerosis_magic)
    }
}