package be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class SavedAdapter(var context: Context, var data:ArrayList<String>): BaseAdapter() {

    override fun getView(position: Int, view: View?, parents: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.simple_separated_item, null)
        (view.findViewById<TextView>(R.id.item)).text=data[position]
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