package com.be_healthy_license_2014141300.be_healthy.fragment

import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.view.*

import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.adapter.SavedAdapter
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.DiseaseActivity
import be_healthy_license_2014141300.be_healthy.database.DBOperation
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import java.lang.NullPointerException

class SavedFragment : Fragment(){

    private var data:ArrayList<String>?=ArrayList()
    private lateinit var adapter: SavedAdapter
    private lateinit var list:ListView

    /**private var receiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                data = intent?.getStringArrayListExtra(activity.resources.getString(R.string.param_saved_list))
                if (data != null && !data?.isEmpty()!!) {
                    adapter= SavedAdapter(context!!, data!!)
                    list.adapter=adapter
                }
            }
            catch(e:NullPointerException){

            }
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_save)))
    }

    override fun onDestroy() {
        super.onDestroy()
        //LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_saved, container, false)
        list=view.findViewById<ListView>(R.id.disease_list)
        adapter = SavedAdapter(activity, this.data!!)
        list.adapter = adapter
        list.setOnItemClickListener { p0, p1, p2, p3 ->
            val intent= Intent(activity, DiseaseActivity::class.java)
            intent.putExtra(activity.resources.getString(R.string.param_disease), StaticDiseaseData().getDisease(adapter.getItem(p2) as String?))
            intent.putExtra(activity.resources.getString(R.string.param_from_saved), true)
            startActivity(intent)
        }
        var disposable = DBOperation(activity).readDisease().subscribe { data->
            try {
                if (data != null && data.isNotEmpty()) {
                    adapter= SavedAdapter(activity.applicationContext, data)
                    list.adapter=adapter
                }
            }
            catch(e:NullPointerException){
            }
        }
        return view
    }

    fun stop(){
        data?.clear()
    }
}
