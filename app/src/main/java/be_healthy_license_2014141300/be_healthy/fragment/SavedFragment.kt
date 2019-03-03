package be_healthy_license_2014141300.be_healthy.fragment

import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.view.*

import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.adapter.AppAdapter
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.activity.DiseaseActivity
import be_healthy_license_2014141300.be_healthy.database.DBOperation
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import java.lang.NullPointerException

class SavedFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_saved, container, false)
        val list=view.findViewById<ListView>(R.id.disease_list)
        var adapter = AppAdapter(activity, R.layout.simple_separated_item, arrayListOf<String>())
        list.adapter = adapter
        list.setOnItemClickListener { _, _, position, _ ->
            val intent= Intent(activity, DiseaseActivity::class.java)
            intent.putExtra(activity.resources.getString(R.string.param_disease), StaticDiseaseData().getDisease(adapter.getItem(position) as String?))
            intent.putExtra(activity.resources.getString(R.string.param_from_saved), true)
            startActivity(intent)
        }
        var disposable = DBOperation(activity).readDisease().subscribe { data->
            try {
                if (data != null && data.isNotEmpty()) {
                    adapter= AppAdapter(activity.applicationContext, R.layout.simple_separated_item, data)
                    list.adapter=adapter
                }
            }
            catch(e:NullPointerException){
            }
        }
        return view
    }
}
