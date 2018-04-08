package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Depression (): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.depression)
        description=res.getString(R.string.depression_description)
        symptoms= mutableListOf(res.getString(R.string.anxiety),
                res.getString(R.string.increased_fatigue),
                res.getString(R.string.loss_of_appetite),
                res.getString(R.string.sleep_disorders_s),
                res.getString(R.string.decreased_self_esteem),
                res.getString(R.string.estrangement),
                res.getString(R.string.indifference),
                res.getString(R.string.attention))
        fillList(activity, treatment, R.array.depression_treatment)
        fillList(activity, magic, R.array.depression_magic)
    }
}