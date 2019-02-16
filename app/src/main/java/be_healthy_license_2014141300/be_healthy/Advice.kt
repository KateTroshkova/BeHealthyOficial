package be_healthy_license_2014141300.be_healthy

import android.app.Activity
import com.be_healthy_license_2014141300.be_healthy.R
import java.util.*

class Advice(){

    var advice:String=""

    constructor(activity: Activity):this(){
        val advices=activity.resources.getStringArray(R.array.advice_list)
        val random= Random()
        advice=advices[random.nextInt(advices.size)]
    }

    override fun equals(other: Any?): Boolean =
            if (other !is Advice) false
            else other.advice==advice

    override fun hashCode(): Int {
        return advice.hashCode()
    }
}