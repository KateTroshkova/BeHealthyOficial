package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Eczema(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.eczema)
        description=res.getString(R.string.eczema_description)
        symptoms= mutableListOf(res.getString(R.string.redness_of_the_skin),
                res.getString(R.string.itching),
                res.getString(R.string.rash),
                res.getString(R.string.small_bubbles_on_the_skin))
        fillList(activity, treatment, R.array.eczema_treatment)
        fillList(activity, magic, R.array.eczema_magic)
    }
}