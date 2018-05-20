package com.be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R

class ClockDialog: DialogFragment(){

    private var listener:OnNewAlarmClockListener?=null
    private var position=-1
    private var alarm:AlarmClock?=null

    fun setListener(listener: OnNewAlarmClockListener){
        this.listener=listener
    }

    fun setAlarm(position:Int, alarm:AlarmClock?){
        this.position=position
        this.alarm=alarm
    }

    interface OnNewAlarmClockListener{
        fun OnNewAlarmClock(hour:Int, minute:Int, position:Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.dialog_item, null)
        val timePicker=view.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)
        if (alarm!=null){
            timePicker.currentHour=alarm!!.hour
            timePicker.currentMinute=alarm!!.minute
        }
        dialog.setPositiveButton(activity.resources.getString(R.string.ok)) { _, _ ->
            if (position>=0) {
                listener?.OnNewAlarmClock(timePicker.currentHour, timePicker.currentMinute, position)
            } else{
                listener?.OnNewAlarmClock(timePicker.currentHour, timePicker.currentMinute, 0)
            }
        }
        dialog.setNegativeButton(activity.resources.getString(R.string.cancel), null)
        dialog.setView(view)
        return dialog.create()
    }
}