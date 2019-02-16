package be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R

class AppAdapter<T>(var context: Context, var layout:Int, var data:MutableList<T>): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(layout, null)
        view.findViewById<TextView>(R.id.item).text=data[position].toString()
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }
}