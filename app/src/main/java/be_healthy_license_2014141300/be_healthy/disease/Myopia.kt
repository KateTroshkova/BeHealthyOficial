package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Myopia(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.myopia)
        description=res.getString(R.string.myopia_description)
        symptoms= mutableListOf(res.getString(R.string.vision_impairment),
                res.getString(R.string.blurriness_of_contours_of_objects),
                res.getString(R.string.good_near_vision),
                res.getString(R.string.violation_of_the_twilight_vision),
                res.getString(R.string.softly_pronounced_exophthalmia),
                res.getString(R.string.spots_before_eyes),
                res.getString(R.string.flash_before_eyes),
                res.getString(R.string.rapid_eye_fatigue),
                res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.constant_desire_to_rub_eyes),
                res.getString(R.string.constant_eye_strain))
        fillList(activity, treatment, R.array.myopia_treatment)
        fillList(activity, magic, R.array.myopia_magic)
    }
}