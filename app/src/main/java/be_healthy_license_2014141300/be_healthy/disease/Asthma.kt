package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Asthma(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.asthma)
        description=res.getString(R.string.asthma_description)
        symptoms= mutableListOf(res.getString(R.string.dyspnea),
                res.getString(R.string.asphyxia),
                res.getString(R.string.cough),
                res.getString(R.string.rapid_breathing),
                res.getString(R.string.wheezing_when_breathing),
                res.getString(R.string.common_ailment),
                res.getString(R.string.palpitation),
                res.getString(R.string.allergy_s),
                res.getString(R.string.headache),
                res.getString(R.string.dizziness))
        fillList(activity, treatment, R.array.asthma_treatment)
        fillList(activity, magic, R.array.asthma_magic)
    }
}