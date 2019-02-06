package be_healthy_license_2014141300.be_healthy.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class SearchAdapter(var context: Context, var data:MutableList<String>, var listener:OnChosenSymptomsChangeListener): BaseAdapter() {

    private var chosen= mutableListOf<String>()
    var currentSubstring=""

    interface OnChosenSymptomsChangeListener{
        fun onChosenSymptomsChange(chosen:MutableList<String>)
    }

    override fun getView(position: Int, view: View?, parents: ViewGroup?): View {
        val inflater= LayoutInflater.from(context)
        val view=inflater.inflate(R.layout.n_item_symptom, null)
        val text=view.findViewById<TextView>(R.id.symptom_name)
        text.text=data[position].replaceFirst(data[position][0], data[position][0].toUpperCase())
        var checkBox=view.findViewById<CheckBox>(R.id.checkBox)
        if (chosen.contains(data[position])){
            checkBox.isChecked=true
            text.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
        checkBox.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                checkBox.isChecked=!checkBox.isChecked
                checkBox.isChecked=!checkBox.isChecked
                if (checkBox.isChecked){
                    chosen.add(data[position])
                    upElement(position)
                }
                else{
                    var dataToRemove=data[position]
                    chosen.remove(dataToRemove)
                    if (currentSubstring in dataToRemove) {
                        downElement(position)
                    }
                }
                listener.onChosenSymptomsChange(chosen)
                notifyDataSetChanged()
            }
        })
        text.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                checkBox.isChecked=!checkBox.isChecked
                if (checkBox.isChecked){
                    chosen.add(data[position])
                    upElement(position)
                }
                else{
                    var dataToRemove=data[position]
                    chosen.remove(dataToRemove)
                    if (currentSubstring in dataToRemove) {
                        downElement(position)
                    }
                }
                listener.onChosenSymptomsChange(chosen)
                notifyDataSetChanged()
            }

        })
        return view
    }

    fun upElement(position:Int){
        if (chosen.contains(data[position])){
            if (position-1>=0 && (!chosen.contains(data[position-1]) || data[position-1]>data[position])){
                val temp=data[position]
                data[position]=data[position-1]
                data[position-1]=temp
                upElement(position-1)
            }
        }
    }

    fun downElement(position:Int){
        if (position+1<data.size && (chosen.contains(data[position+1]) || data[position+1]<data[position])){
            val temp=data[position]
            data[position]=data[position+1]
            data[position+1]=temp
            downElement(position+1)
        }
    }

    fun clear(){
        chosen.clear()
        currentSubstring=""
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