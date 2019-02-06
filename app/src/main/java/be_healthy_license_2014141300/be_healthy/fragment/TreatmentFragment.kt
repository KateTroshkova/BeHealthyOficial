package com.be_healthy_license_2014141300.be_healthy.fragment

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.adapter.TreatmentAdapter
import com.be_healthy_license_2014141300.be_healthy.R

class TreatmentFragment: Fragment() {

    private var data= mutableListOf<String>()

    fun setData(data:MutableList<String>){
        this.data=data
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_treatment, container, false)
        val list=view.findViewById<ListView>(R.id.treatment_list)
        val adapter= TreatmentAdapter(activity, data)
        list.adapter=adapter
        return view
    }
}
