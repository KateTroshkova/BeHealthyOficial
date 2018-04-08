package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Mononucleosis (): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.mononucleosis)
        description=res.getString(R.string.mononucleosis_description)
        symptoms= mutableListOf(res.getString(R.string.common_ailment),
                res.getString(R.string.fever),
                res.getString(R.string.headache),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.joint_pain),
                res.getString(R.string.sore_throat),
                res.getString(R.string.enlarged_lymph_nodes),
                res.getString(R.string.cough),
                res.getString(R.string.rash),
                res.getString(R.string.stomach_ache))
        fillList(activity, treatment, R.array.mononucleosis_treatment)
        fillList(activity, magic, R.array.mononucleosis_magic)
    }
}