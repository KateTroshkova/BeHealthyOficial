package com.be_healthy_license_2014141300.be_healthy.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class SavedDiseaseAdapter(var context: Activity, data:MutableList<String>?): RecyclerView.Adapter<SavedDiseaseAdapter.MyViewHolder>() {

    var data=mutableListOf<Disease>()
    init{
        if (data != null) {
            for(name in data){
                this.data.add(Disease().findDiseaseByName(context, name))
            }
        }
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.name) as TextView
        var description: TextView =view.findViewById(R.id.description) as TextView
        var viewForeground: RelativeLayout = view.findViewById(R.id.view_foreground) as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.saved_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.description.text = data[position].description
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position:Int):Disease{
        return data[position]
    }

    fun removeItem(position: Int) {
        DB_Operation(context).deleteDisease(data[position].name)
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}