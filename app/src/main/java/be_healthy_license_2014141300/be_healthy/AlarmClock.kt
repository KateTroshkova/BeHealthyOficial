package com.be_healthy_license_2014141300.be_healthy

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.be_healthy_license_2014141300.be_healthy.activity.AlarmActivity
import java.util.*

class AlarmClock(var hour:Int=0,
                 var minute:Int=0,
                 var alarm:Int=1,
                 var repeat:Int=0,
                 sun:Int=0,
                 mon:Int=0,
                 tue:Int=0,
                 wen:Int=0,
                 th:Int=0,
                 fr:Int=0,
                 sat:Int=0,
                 var id:Int=0,
                 var ringtone:String?="",
                 var ringtoneName:String?="",
                 var description:String?=" "):Parcelable {

    var week = arrayOf(sun, mon, tue, wen, th, fr, sat)
        get()=field
    private val dayOfWeekNames= arrayOf("'sun'", "'mon'", "'tue'", "'wen'", "'th'", "'fr'", "'sat'")

    protected constructor(dest: android.os.Parcel) : this(
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readInt(),
            dest.readString(),
            dest.readString(),
            dest.readString())

    companion object {
        @JvmField val CREATOR = object : android.os.Parcelable.Creator<AlarmClock> {
            override fun createFromParcel(`in`: android.os.Parcel): AlarmClock {
                return AlarmClock(`in`)
            }

            override fun newArray(size: Int): Array<AlarmClock?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(hour)
        p0?.writeInt(minute)
        p0?.writeInt(alarm)
        p0?.writeInt(repeat)
        for( day in week){
            p0?.writeInt(day)
        }
        p0?.writeInt(id)
        p0?.writeString(ringtone)
        p0?.writeString(ringtoneName)
        p0?.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String =
            hour.toString()+"#"+
                    minute.toString()+"#"+
                    alarm.toString()+"#"+
                    repeat.toString()+"#"+
                    week[0].toString()+"#"+
                    week[1].toString()+"#"+
                    week[2].toString()+"#"+
                    week[3].toString()+"#"+
                    week[4].toString()+"#"+
                    week[5].toString()+"#"+
                    week[6].toString()+"#"+
                    id.toString()+"#"+
                    ringtone+"#"+
                    ringtoneName+"#"+description

    fun decodeFromString(alarm:String):AlarmClock{
        val data=alarm.split("#")
        return AlarmClock(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt(),
                data[4].toInt(), data[5].toInt(), data[6].toInt(), data[7].toInt(), data[8].toInt(), data[9].toInt(), data[10].toInt(),
                data[11].toInt(), data[12], data[13], data[14])
    }

    fun stopAlarm(activity:Activity):Boolean{
        if (repeat==0) {
            cancelAlarm(activity)
            alarm=0
            return true
        }
        return false
    }

    fun updateAlarm(activity:Activity){
        cancelAlarm(activity)
        minute += 5
        if (minute>=60){
            minute -= 60
            hour += 1
        }
        if (hour>=24){
            hour -= 24
        }
        if (repeat==1){
            setRepeatingAlarm(activity)
        }
        else {
            setAlarm(activity)
        }
    }

    fun setAlarm(activity: Activity){
        val am = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmActivity::class.java)
        intent.action = this.toString()
        val pi = PendingIntent.getActivity(activity, id, intent, 0)
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        //am.set(AlarmManager.RTC_WAKEUP, getTime(1, true), pi)
    }

    fun cancelAlarm(activity:Activity){
        val am = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmActivity::class.java)
        intent.action = this.toString()
        if (repeat==0){
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            am.cancel(pi)
        }
        else{
            for(day in week){
                if (day==1){
                    intent.data=Uri.parse("id="+id+"repeat="+dayOfWeekNames[week.indexOf(day)])
                    val pi=PendingIntent.getActivity(activity, id, intent, 0)
                    am.cancel(pi)
                }
            }
        }
    }

    fun setRepeatingAlarm(activity:Activity){
        val intent = Intent(activity, AlarmActivity::class.java)
        intent.action = this.toString()
        for(day in week){
            if (day==1){
                intent.data=Uri.parse("id="+id+"repeat="+dayOfWeekNames[week.indexOf(day)])
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                        AlarmManager.RTC_WAKEUP, getNearestDay(week.indexOf(day)), AlarmManager.INTERVAL_DAY*7, pi)
            }
        }
    }

    private fun getTime(days:Int, exactDay:Boolean):Long{
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        val day = cal.time
        var time=day.time
        if (exactDay) {
            if (day.before(Date(System.currentTimeMillis()))) {
                time += days * AlarmManager.INTERVAL_DAY
            }
        }
        else{
            time += days * AlarmManager.INTERVAL_DAY
        }
        return time
    }

    private fun getNearestDay(index:Int):Long{
        val cal=Calendar.getInstance()
        val day=index+1
        var delay=0
        while(cal.get(Calendar.DAY_OF_WEEK)!=day){
            cal.add(Calendar.DAY_OF_WEEK, 1)
            delay++
        }
        if (delay==0) {
            delay = 7
            return getTime(delay, true)
        }
        else{
            return getTime(delay, false)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AlarmClock) return false
        return this.toString()==other.toString()
    }
}