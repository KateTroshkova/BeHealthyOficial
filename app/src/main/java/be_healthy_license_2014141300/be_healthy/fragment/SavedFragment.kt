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

import android.widget.ImageView
import com.be_healthy_license_2014141300.be_healthy.listener.ClickListener
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.listener.RecyclerTouchListener
import com.be_healthy_license_2014141300.be_healthy.activity.DiseaseActivity
import com.be_healthy_license_2014141300.be_healthy.slide_helper.ListHelper
import com.be_healthy_license_2014141300.be_healthy.adapter.SavedDiseaseAdapter
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.slide_helper.SavedListHelper
import java.lang.NullPointerException

class SavedFragment : Fragment(), ListHelper.OnSwipeListener {

    private var data:ArrayList<String>?=ArrayList()
    private lateinit var adapter: SavedDiseaseAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var image: ImageView

    private var receiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                data = intent?.getStringArrayListExtra(activity.resources.getString(R.string.param_saved_list))
                if (data != null && !data?.isEmpty()!!) {
                    adapter = SavedDiseaseAdapter(activity, data)
                    recyclerView.adapter = adapter
                    image.visibility = View.INVISIBLE
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
        DB_Operation(activity).readDisease()
        val view = inflater!!.inflate(R.layout.fragment_saved, container, false)
        recyclerView = view?.findViewById(R.id.disease_list) as RecyclerView
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = SavedDiseaseAdapter(activity, data)
        recyclerView.adapter = adapter
        recyclerView.addOnItemTouchListener(RecyclerTouchListener(activity, recyclerView, object : ClickListener {

            override fun onClick(view: View, position: Int) {
                val intent= Intent(activity, DiseaseActivity::class.java)
                intent.putExtra(activity.resources.getString(R.string.param_disease), adapter.getItem(position))
                startActivity(intent)
            }

            override fun onLongClick(view: View, position: Int) {
            }

        }))
        val itemTouchHelperCallback = SavedListHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        image=view.findViewById(R.id.empty_icon) as ImageView

        return view
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is SavedDiseaseAdapter.MyViewHolder) {
            adapter.removeItem(viewHolder.getAdapterPosition())
            adapter.notifyDataSetChanged()
            if (adapter.itemCount ==0){
                image.visibility = View.VISIBLE
            }
        }
    }
}
