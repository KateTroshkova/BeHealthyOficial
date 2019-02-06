package com.be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import android.text.Html
import com.be_healthy_license_2014141300.be_healthy.R.id.textView



class OptionAdapter(var context: Context, var data:MutableList<Disease>, var symptoms:ArrayList<String>): BaseAdapter() {

    override fun getView(position: Int, view: View?, parents: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.n_item_option, null)
        val header=view.findViewById<TextView>(R.id.header)
        header.text=data[position].name
        val symptoms=view.findViewById<TextView>(R.id.subtext)
        var coloredSymptoms=""
        var blackSymptoms=""
        this.symptoms.sort()
        data[position].symptoms.sort()
        for(symptom in this.symptoms){
            if (symptom in data[position].symptoms) {
                coloredSymptoms += "$symptom, "
            }
        }
        for(symptom in data[position].symptoms){
            if (!(symptom in this.symptoms))
            blackSymptoms+= "$symptom, "
        }
        blackSymptoms.dropLast(2)
        val text = "<font color='#388e3c'>$coloredSymptoms</font><font color='#000000'>$blackSymptoms</font>"
        symptoms.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE)
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