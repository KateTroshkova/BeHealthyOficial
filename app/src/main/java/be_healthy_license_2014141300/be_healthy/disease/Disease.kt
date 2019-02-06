package com.be_healthy_license_2014141300.be_healthy.disease

import android.app.Activity
import android.os.Parcelable
import be_healthy_license_2014141300.be_healthy.disease.*
import com.be_healthy_license_2014141300.be_healthy.R

open class Disease(): Parcelable, Comparable<Disease> {
    override fun compareTo(other: Disease): Int {
        if (name>other.name) return -1
        if (name<other.name) return 1
        return 0
    }

    var name:String = ""
    var description:String=""
    var symptoms = mutableListOf<String>()
    var warning=""
    var treatment = mutableListOf<String>()
    var magic = mutableListOf<String>()

    protected constructor(dest: android.os.Parcel) : this() {
        name=dest.readString()
        description=dest.readString()
        symptoms=decodeList(dest.readString())
        warning=dest.readString()
        treatment=decodeList(dest.readString())
        magic=decodeList(dest.readString())
        symptoms.sort()
    }

    companion object {
        @JvmField val CREATOR = object : Parcelable.Creator<Disease> {
            override fun createFromParcel(`in`: android.os.Parcel): Disease {
                return Disease(`in`)
            }

            override fun newArray(size: Int): Array<Disease?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(dest: android.os.Parcel?, flags: Int) {
        dest?.writeString(name)
        dest?.writeString(description)
        dest?.writeString(codeList(symptoms))
        dest?.writeString(warning)
        dest?.writeString(codeList(treatment))
        dest?.writeString(codeList(magic))
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun codeList(data:MutableList<String>):String{
        var result=""
        for(element in data){
            result+=element+"#"
        }
        result=result.removeSuffix("#")
        return result
    }

    private fun decodeList(data:String):MutableList<String>{
        val result= mutableListOf<String>()
        val array=data.split("#")
        result += array
        return result
    }
}