package be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class Streptococcus(): Disease() {

    constructor(activity: Activity):this(){
        val res=activity.resources
        name=res.getString(R.string.streptococcus)
        description=res.getString(R.string.streptococcus_description)
        symptoms= mutableListOf(res.getString(R.string.sore_throat),
                res.getString(R.string.enlarged_lymph_nodes),
                res.getString(R.string.common_ailment),
                res.getString(R.string.muscle_pain),
                res.getString(R.string.joint_pain),
                res.getString(R.string.fever),
                res.getString(R.string.chills),
                res.getString(R.string.redness_of_the_skin),
                res.getString(R.string.stomach_ache),
                res.getString(R.string.vomiting),
                res.getString(R.string.diarrhea),
                res.getString(R.string.cough),
                res.getString(R.string.dyspnea),
                res.getString(R.string.headache),
                res.getString(R.string.dizziness),
                res.getString(R.string.sleep_disorders_s),
                res.getString(R.string.disturbance_of_consciousness))
        fillList(activity, treatment, R.array.streptococcus_treatment)
        fillList(activity, magic, R.array.streptococcus_magic)
    }
}