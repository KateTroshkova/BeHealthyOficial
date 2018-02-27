package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class ARVI(): Disease(){

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.arvi)
        description=res.getString(R.string.arvi_description)
        symptoms= mutableListOf(res.getString(R.string.nasal_congestion),
                res.getString(R.string.runny_nose),
                res.getString(R.string.sneezing),
                res.getString(R.string.itching_in_the_nose),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.pain_when_swallowing),
                res.getString(R.string.redness_in_the_throat),
                res.getString(R.string.cough),
                res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.headache),
                res.getString(R.string.drowsiness),
                res.getString(R.string.redness_of_the_eyes),
                res.getString(R.string.burning),
                res.getString(R.string.lacrimation),
                res.getString(R.string.diarrhea))
        fillList(activity, treatment, R.array.arvi_treatment)
        fillList(activity, magic, R.array.arvi_magic)
    }
}