package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Activity
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.be_healthy_license_2014141300.be_healthy.AlarmClock
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.adapter.AlarmClockAdapter
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.dialog.ClockDialog

class AlarmFragment : Fragment(), View.OnClickListener, ClockDialog.OnNewAlarmClockListener {

    private var alarms= arrayListOf<AlarmClock>()
    private lateinit var list: ListView
    private lateinit var adapter: AlarmClockAdapter
    private lateinit var existActivity:Activity

    private var receiver=object: BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            alarms = p1?.getParcelableArrayListExtra<AlarmClock>(p0?.resources?.getString(R.string.param_alarm))!!
            adapter = AlarmClockAdapter(existActivity, alarms)
            list.adapter=adapter
        }
    }

    private var idReceiver=object: BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val alarm=p1?.getParcelableExtra<AlarmClock>(p0?.resources?.getString(R.string.param_id))
            if (alarm != null) {
                alarms.add(alarm)
                alarm.setAlarm(existActivity)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private var deleteReceiver=object: BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            alarms= p1?.getParcelableArrayListExtra(existActivity.resources.getString(R.string.param_id))!!
            adapter.notifyDataSetChanged()
        }
    }

    private var updateReceiver=object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val newAlarm=p1?.getParcelableExtra<AlarmClock>(p0?.resources?.getString(R.string.param_alarm))
            for(i in 0 until alarms.size){
                if (alarms[i].id==newAlarm?.id){
                    alarms.removeAt(i)
                    alarms.add(i, newAlarm)
                }
            }
            adapter.notifyDataSetChanged()
        }

    }

    override fun OnNewAlarmClock(hour: Int, minute: Int, position:Int) {
        DB_Operation(existActivity).saveAlarm(AlarmClock(hour, minute))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_alarm, container, false)
        existActivity=activity
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_alarm_ready)))
        LocalBroadcastManager.getInstance(activity).registerReceiver(idReceiver, IntentFilter(activity.resources.getString(R.string.action_id_back)))
        LocalBroadcastManager.getInstance(activity).registerReceiver(deleteReceiver, IntentFilter(activity.resources.getString(R.string.action_alarm_delete)))
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateReceiver, IntentFilter(activity.resources.getString(R.string.action_alarm_update)))
        DB_Operation(activity).readAlarm()
        val button=view.findViewById(R.id.fab) as Button
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(activity.application as CustomApplication).size_coef*0.6f)
        button.setOnClickListener(this)
        list=view.findViewById(R.id.list) as ListView
        return view
    }

    override fun onClick(p0: View?) {
        val dialog =ClockDialog()
        dialog.setListener(this)
        dialog.show(existActivity.fragmentManager, "clock")
    }
}

