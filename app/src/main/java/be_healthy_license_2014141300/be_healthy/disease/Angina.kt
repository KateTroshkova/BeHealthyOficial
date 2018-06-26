package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R

class Angina(): Disease(){

    constructor(activity: Activity) : this() {
        val res=activity.resources
        name=res.getString(R.string.angina)
        description=res.getString(R.string.angina_description)
        symptoms=mutableListOf(res.getString(R.string.tickle_in_the_throat),
                res.getString(R.string.sore_throat),
                res.getString(R.string.common_ailment),
                res.getString(R.string.headache),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.fever),
                res.getString(R.string.chills),
                res.getString(R.string.tenderness_of_lymph_nodes),
                res.getString(R.string.enlargement_of_palatine_tonsils),
                res.getString(R.string.plaque_on_the_tonsils))
        fillList(activity, treatment, R.array.angina_treatment)
        fillList(activity, magic, R.array.angina_magic)
    }
}