package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Conjunctivitis(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.conjunctivitis)
        description=res.getString(R.string.conjunctivitis_description)
        symptoms= mutableListOf(res.getString(R.string.swelling_of_the_eyes),
                res.getString(R.string.redness_of_the_eyes),
                res.getString(R.string.lacrimation),
                res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.burning_eyes),
                res.getString(R.string.photophobia))
        fillList(activity, treatment, R.array.conjunctivitis_treatment)
        fillList(activity, magic, R.array.conjunctivitis_magic)
    }
}