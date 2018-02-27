package com.be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class OptionAdapter(var context: Context, var data:MutableList<Disease>): BaseAdapter() {

    override fun getView(position: Int, view: View?, parents: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.option_item, null)
        (view.findViewById(R.id.name) as TextView).text=data[position].name
        var symptoms=""
        for(symptom in data[position].symptoms){
            symptoms+=symptom+","
        }
        symptoms=symptoms.removeSuffix(",")
        val subtext=view.findViewById(R.id.description) as TextView
        subtext.text=symptoms
        subtext.setTypeface(subtext.typeface, Typeface.ITALIC)
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}