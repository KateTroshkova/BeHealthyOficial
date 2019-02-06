package com.be_healthy_license_2014141300.be_healthy.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.ArrayAdapter

import android.widget.ImageView
import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.adapter.SavedAdapter
import com.be_healthy_license_2014141300.be_healthy.listener.ClickListener
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.listener.RecyclerTouchListener
import com.be_healthy_license_2014141300.be_healthy.activity.DiseaseActivity
import com.be_healthy_license_2014141300.be_healthy.slide_helper.ListHelper
import com.be_healthy_license_2014141300.be_healthy.adapter.SavedDiseaseAdapter
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import com.be_healthy_license_2014141300.be_healthy.slide_helper.SavedListHelper
import java.lang.NullPointerException

class SavedFragment : Fragment(){

    private var data:ArrayList<String>?=ArrayList()
    private lateinit var adapter: SavedAdapter
    private lateinit var list:ListView

    private var receiver=object: BroadcastReceiver(){
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_save)))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(receiver)
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
        DB_Operation(activity).readDisease()
        return view
    }

    fun stop(){
        data?.clear()
    }
}
