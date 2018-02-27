package com.be_healthy_license_2014141300.be_healthy.adapter

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView

class SimpleListAdapter(var activity: Activity, var data:MutableList<String>): BaseAdapter() {
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = LayoutInflater.from(activity)
        val row = inflater.inflate(R.layout.simple_custom_item, null)
        val text= row.findViewById(R.id.textView2) as TextView
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, text.textSize*(activity.application as CustomApplication).size_coef)
        text.text = data[position]
        return row
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