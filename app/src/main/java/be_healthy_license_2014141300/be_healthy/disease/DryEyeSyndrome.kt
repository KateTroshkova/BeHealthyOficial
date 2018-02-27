package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class DryEyeSyndrome(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.dry_eye_syndrome)
        description=res.getString(R.string.dry_eye_description)
        symptoms= mutableListOf(res.getString(R.string.foreign_body_sensation_in_the_eye),
                res.getString(R.string.lacrimation),
                res.getString(R.string.dryness_of_eyes),
                res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.burning_eyes),
                res.getString(R.string.photophobia),
                res.getString(R.string.vision_impairment),
                res.getString(R.string.reduction_of_the_tear_meniscus),
                res.getString(R.string.blockage_of_the_tear_film))
        fillList(activity, treatment, R.array.dry_eye_treatment)
        fillList(activity, magic, R.array.dry_eye_magic)
    }
}