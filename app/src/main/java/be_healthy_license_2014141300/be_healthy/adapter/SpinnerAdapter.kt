package com.be_healthy_license_2014141300.be_healthy.adapter

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView

class SpinnerAdapter (var activity: Activity, textViewResourceId: Int, var objects: MutableList<String>)
    : ArrayAdapter<String>(activity, textViewResourceId, objects) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(activity)
        val row = inflater.inflate(R.layout.simple_custom_item, null)
        val text= row.findViewById(R.id.textView2) as TextView
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, text.textSize*(activity.application as CustomApplication).size_coef)
        text.text = objects[position]
        return row
    }
}