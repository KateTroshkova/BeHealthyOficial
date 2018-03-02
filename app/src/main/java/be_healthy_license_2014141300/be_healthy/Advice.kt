package com.be_healthy_license_2014141300.be_healthy

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Advice():Parcelable {

    var advice:String=""

    constructor(activity: Activity):this(){
        val advices=activity.resources.getStringArray(R.array.advice_list)
        val random= Random()
        advice=advices[random.nextInt(advices.size)]
    }

    protected constructor(parcel: Parcel) : this() {
        advice=parcel.readString()
    }

    companion object {
        @JvmField val CREATOR = object : android.os.Parcelable.Creator<Advice> {
            override fun createFromParcel(`in`: android.os.Parcel): Advice {
                return Advice(`in`)
            }

            override fun newArray(size: Int): Array<Advice?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(advice)
    }

    override fun describeContents(): Int=0

    override fun equals(other: Any?): Boolean =
            if (other !is Advice) false
            else other.advice==advice
}