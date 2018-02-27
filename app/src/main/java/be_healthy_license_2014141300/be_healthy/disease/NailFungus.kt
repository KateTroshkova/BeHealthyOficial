package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class NailFungus():Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.nail_fungus)
        description=res.getString(R.string.nail_fungus_description)
        symptoms= mutableListOf(res.getString(R.string.thickening_on_the_foot),
                res.getString(R.string.itching),
                res.getString(R.string.redness_of_the_skin),
                res.getString(R.string.burning),
                res.getString(R.string.thickening_of_the_nail),
                res.getString(R.string.brittleness_of_the_nail))
        fillList(activity, treatment, R.array.nail_treatment)
        fillList(activity, magic, R.array.nail_magic)
    }
}