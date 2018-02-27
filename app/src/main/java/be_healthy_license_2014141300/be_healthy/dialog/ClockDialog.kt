package com.be_healthy_license_2014141300.be_healthy.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.R

class ClockDialog(var activity: Context, var listener: OnNewAlarmClockListener, var position:Int, var alarm: AlarmClock?): DialogFragment(){

    interface OnNewAlarmClockListener{
        fun OnNewAlarmClock(hour:Int, minute:Int, position:Int)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog= AlertDialog.Builder(activity)
        val inflater= LayoutInflater.from(activity)
        val view=inflater.inflate(R.layout.dialog_item, null)
        val timePicker=view.findViewById(R.id.timePicker) as TimePicker
        timePicker.setIs24HourView(true)
        if (alarm!=null){
            timePicker.currentHour=alarm!!.hour
            timePicker.currentMinute=alarm!!.minute
        }
        dialog.setPositiveButton(activity.resources.getString(R.string.ok)) { _, _ ->
            if (position>=0) {
                listener.OnNewAlarmClock(timePicker.currentHour, timePicker.currentMinute, position)
            } else{
                listener.OnNewAlarmClock(timePicker.currentHour, timePicker.currentMinute, 0)
            }
        }
        dialog.setNegativeButton(activity.resources.getString(R.string.cancel), null)
        dialog.setView(view)
        return dialog.create()
    }
}