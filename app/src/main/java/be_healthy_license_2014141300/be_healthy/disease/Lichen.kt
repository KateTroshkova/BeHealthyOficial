package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Lichen (): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.lichen)
        description=res.getString(R.string.lichen_description)
        symptoms= mutableListOf(res.getString(R.string.skin_spots),
                res.getString(R.string.skin_peeling),
                res.getString(R.string.itching))
        fillList(activity, treatment, R.array.lichen_treatment)
        fillList(activity, magic, R.array.lichen_magic)
    }
}