package com.be_healthy_license_2014141300.be_healthy.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.be_healthy_license_2014141300.be_healthy.HeartBeat
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.adapter.HistoryAdapter
import be_healthy_license_2014141300.be_healthy.database.DB_Operation

class HistoryFragment : Fragment() {

    private var data= mutableListOf<HeartBeat>()
    private var content: View?=null

    private val receiver=object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (activity !=null && intent?.hasExtra(resources.getString(R.string.param_heartbeat))!!) {
                data = intent.getParcelableArrayListExtra<HeartBeat>(resources.getString(R.string.param_heartbeat))
                val list=content?.findViewById(R.id.history_list) as ListView
                val adapter= HistoryAdapter(activity, data)
                list.adapter=adapter
            }
        }
    }

    private val updateReceiver=object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            read(p0)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        content = inflater?.inflate(R.layout.fragment_history, container, false)
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_read_ready)))
        LocalBroadcastManager.getInstance(activity).registerReceiver(updateReceiver, IntentFilter(activity.resources.getString(R.string.action_update_history)))
        read()
        return content
    }

    private fun read(context: Context?=null){
        if (context==null) {
            DB_Operation(activity).readHearBeat()
        }
        else{
            DB_Operation(context).readHearBeat()
        }
    }
}
