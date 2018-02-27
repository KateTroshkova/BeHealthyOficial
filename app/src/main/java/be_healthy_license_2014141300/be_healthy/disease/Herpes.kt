package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Herpes():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.herpes)
        description=res.getString(R.string.herpes_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.headache),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.fever),
                res.getString(R.string.rash),
                res.getString(R.string.itching))
        fillList(activity, treatment, R.array.herpes_treatment)
        fillList(activity, magic, R.array.herpes_magic)
    }
}