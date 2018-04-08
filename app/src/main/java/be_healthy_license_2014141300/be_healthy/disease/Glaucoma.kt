package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Glaucoma(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.glaucoma)
        description=res.getString(R.string.glaucoma_description)
        symptoms= mutableListOf(res.getString(R.string.pain_in_the_eyes),
                res.getString(R.string.headache),
                res.getString(R.string.vision_impairment))
        fillList(activity, treatment, R.array.glaucoma_treatment)
        fillList(activity, magic, R.array.glaucoma_magic)
    }
}