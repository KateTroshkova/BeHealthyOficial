package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Stenocardia(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.stenocardia)
        description=res.getString(R.string.stenocardia_description)
        symptoms= mutableListOf(res.getString(R.string.palpitation),
                res.getString(R.string.high_blood_pressure),
                res.getString(R.string.heart_pain),
                res.getString(R.string.tightness_in_the_chest))
        fillList(activity, treatment, R.array.stenocardia_treatment)
        fillList(activity, magic, R.array.stenocardia_magic)
    }
}