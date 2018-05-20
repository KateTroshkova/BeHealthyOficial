package com.be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class SymptomsAdapter(var data:MutableList<String>): RecyclerView.Adapter<SymptomsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name)
        var viewForeground: RelativeLayout = view.findViewById(R.id.view_foreground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.symptom_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val item = data!![position]
        holder.name.text = data[position]
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}