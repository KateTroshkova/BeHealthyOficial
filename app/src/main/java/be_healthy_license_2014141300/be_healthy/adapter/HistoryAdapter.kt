package com.be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.HeartBeat
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.HeartView

class HistoryAdapter(var context: Context, var data:MutableList<HeartBeat>): BaseAdapter() {

    override fun getView(position: Int, view: View?, parents: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.history_item, null)
        (view.findViewById<TextView>(R.id.result_text)).text=data[position].result.toString()
        (view.findViewById<TextView>(R.id.date_text)).text=data[position].date
        (view.findViewById<HeartView>(R.id.heart_item)).update(data[position].line)
        return view
    }

    override fun getItem(p0: Int): Any {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}