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
                 var sun:Int=0,
                 var mon:Int=0,
                 var tue:Int=0,
                 var wen:Int=0,
                 var th:Int=0,
                 var fr:Int=0,
                 var sat:Int=0,
                 var id:Int=0,
                 var ringtone:String?="",
                 var ringtoneName:String?="",
                 var description:String?=" "):Parcelable {

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
        p0?.writeInt(sun)
        p0?.writeInt(mon)
        p0?.writeInt(tue)
        p0?.writeInt(wen)
        p0?.writeInt(th)
        p0?.writeInt(fr)
        p0?.writeInt(sat)
        p0?.writeInt(id)
        p0?.writeString(ringtone)
        p0?.writeString(ringtoneName)
        p0?.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return hour.toString()+"#"+
                minute.toString()+"#"+
                alarm.toString()+"#"+
                repeat.toString()+"#"+
                sun.toString()+"#"+
                mon.toString()+"#"+
                tue.toString()+"#"+
                wen.toString()+"#"+
                th.toString()+"#"+
                fr.toString()+"#"+sat.toString()+"#"+
                id.toString()+"#"+
                ringtone+"#"+
                ringtoneName+"#"+description
    }

    fun decodeFromString(alarm:String):AlarmClock{
        val data=alarm.split("#")
        return AlarmClock(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt(),
                data[4].toInt(), data[5].toInt(), data[6].toInt(), data[7].toInt(), data[8].toInt(), data[9].toInt(), data[10].toInt(),
                data[11].toInt(), data[12], data[13], data[14])
    }

    fun setAlarm(activity: Activity){
        val am = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmActivity::class.java)
        intent.action = this.toString();
        val pi = PendingIntent.getActivity(activity, id, intent, 0)
        am.set(AlarmManager.RTC_WAKEUP, getTime(1, true), pi)
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
            if (sun==1) {
                intent.data = Uri.parse("id=" + id + "repeat='sun'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (mon==1){
                intent.data = Uri.parse("id=" + id + "repeat='mon'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (tue==1){
                intent.data = Uri.parse("id=" + id + "repeat='tue'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (wen==1){
                intent.data = Uri.parse("id=" + id + "repeat='wen'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (th==1){
                intent.data = Uri.parse("id=" + id + "repeat='th'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (fr==1){
                intent.data = Uri.parse("id=" + id + "repeat='fr'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
            if (sat==1){
                intent.data = Uri.parse("id=" + id + "repeat='sat'")
                val pi = PendingIntent.getActivity(activity, id, intent, 0)
                am.cancel(pi)
            }
        }
    }

    fun setRepeatingAlarm(activity:Activity){
        val intent = Intent(activity, AlarmActivity::class.java)
        intent.action = this.toString()
        if (sun==1) {
            intent.data = Uri.parse("id=" + id + "repeat='sun'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestSunday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (mon==1){
            intent.data = Uri.parse("id=" + id + "repeat='mon'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestMonday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (tue==1){
            intent.data = Uri.parse("id=" + id + "repeat='tue'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestTuesday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (wen==1){
            intent.data = Uri.parse("id=" + id + "repeat='wen'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestWednesday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (th==1){
            intent.data = Uri.parse("id=" + id + "repeat='th'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestThursday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (fr==1){
            intent.data = Uri.parse("id=" + id + "repeat='fr'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestFriday(), AlarmManager.INTERVAL_DAY*7, pi)
        }
        if (sat==1){
            intent.data = Uri.parse("id=" + id + "repeat='sat'")
            val pi = PendingIntent.getActivity(activity, id, intent, 0)
            (activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setRepeating(
                    AlarmManager.RTC_WAKEUP, getNearestSaturday(), AlarmManager.INTERVAL_DAY*7, pi)
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

    private fun getNearestSunday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(7, true)
            }
            Calendar.MONDAY->{
                return getTime(6, false)
            }
            Calendar.TUESDAY->{
                return getTime(5, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(4, false)
            }
            Calendar.THURSDAY->{
                return getTime(3, false)
            }
            Calendar.FRIDAY->{
                return getTime(2, false)
            }
            Calendar.SATURDAY->{
                return getTime(1, false)
            }
        }
        return -1
    }

    private fun getNearestMonday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(1, false)
            }
            Calendar.MONDAY->{
                return getTime(7, true)
            }
            Calendar.TUESDAY->{
                return getTime(6, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(5, false)
            }
            Calendar.THURSDAY->{
                return getTime(4, false)
            }
            Calendar.FRIDAY->{
                return getTime(3, false)
            }
            Calendar.SATURDAY->{
                return getTime(2, false)
            }
        }
        return -1
    }
    private fun getNearestTuesday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(2, false)
            }
            Calendar.MONDAY->{
                return getTime(1, false)
            }
            Calendar.TUESDAY->{
                return getTime(7, true)
            }
            Calendar.WEDNESDAY->{
                return getTime(6, false)
            }
            Calendar.THURSDAY->{
                return getTime(5, false)
            }
            Calendar.FRIDAY->{
                return getTime(4, false)
            }
            Calendar.SATURDAY->{
                return getTime(3, false)
            }
        }
        return -1
    }
    private fun getNearestWednesday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(3, false)
            }
            Calendar.MONDAY->{
                return getTime(2, false)
            }
            Calendar.TUESDAY->{
                return getTime(1, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(7, true)
            }
            Calendar.THURSDAY->{
                return getTime(6, false)
            }
            Calendar.FRIDAY->{
                return getTime(5, false)
            }
            Calendar.SATURDAY->{
                return getTime(4, false)
            }
        }
        return -1
    }
    private fun getNearestThursday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(4, false)
            }
            Calendar.MONDAY->{
                return getTime(3, false)
            }
            Calendar.TUESDAY->{
                return getTime(2, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(1, false)
            }
            Calendar.THURSDAY->{
                return getTime(7, true)
            }
            Calendar.FRIDAY->{
                return getTime(6, false)
            }
            Calendar.SATURDAY->{
                return getTime(5, false)
            }
        }
        return -1
    }
    private fun getNearestFriday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(5, false)
            }
            Calendar.MONDAY->{
                return getTime(4, false)
            }
            Calendar.TUESDAY->{
                return getTime(3, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(2, false)
            }
            Calendar.THURSDAY->{
                return getTime(1, false)
            }
            Calendar.FRIDAY->{
                return getTime(7, true)
            }
            Calendar.SATURDAY->{
                return getTime(6, false)
            }
        }
        return -1
    }
    private fun getNearestSaturday():Long{
        val cal = Calendar.getInstance()
        val day=cal.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY->{
                return getTime(6, false)
            }
            Calendar.MONDAY->{
                return getTime(5, false)
            }
            Calendar.TUESDAY->{
                return getTime(4, false)
            }
            Calendar.WEDNESDAY->{
                return getTime(3, false)
            }
            Calendar.THURSDAY->{
                return getTime(2, false)
            }
            Calendar.FRIDAY->{
                return getTime(1, false)
            }
            Calendar.SATURDAY->{
                return getTime(7, true)
            }
        }
        return -1
    }

}