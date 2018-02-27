package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Tonsillitis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.tonsillitis)
        description=res.getString(R.string.tonsillitis_description)
        symptoms= mutableListOf(res.getString(R.string.fever),
                res.getString(R.string.common_ailment),
                res.getString(R.string.headache),
                res.getString(R.string.sore_throat),
                res.getString(R.string.pain_when_swallowing),
                res.getString(R.string.tenderness_of_lymph_nodes),
                res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.bad_breath),
                res.getString(R.string.expectoration_of_purulent_tubes))
        fillList(activity, treatment, R.array.tonsillitis_treatment)
        fillList(activity, magic, R.array.tonsillitis_magic)
    }
}