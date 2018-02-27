package com.be_healthy_license_2014141300.be_healthy

import android.os.Parcel
import android.os.Parcelable

data class HeartBeat(var date:String, var line:String, var result:Int): Parcelable {

    companion object {
        @JvmField val CREATOR = object : android.os.Parcelable.Creator<HeartBeat> {
            override fun createFromParcel(`in`: android.os.Parcel): HeartBeat {
                return HeartBeat(`in`)
            }

            override fun newArray(size: Int): Array<HeartBeat?> {
                return arrayOfNulls(size)
            }
        }
    }

    protected constructor(dest: android.os.Parcel) : this(
            dest.readString(),
            dest.readString(),
            dest.readInt())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeString(date)
        dest?.writeString(line)
        dest?.writeInt(result)
    }

    override fun describeContents(): Int {
        return 0
    }
}