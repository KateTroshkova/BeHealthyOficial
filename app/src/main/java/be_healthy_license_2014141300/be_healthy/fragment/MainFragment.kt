package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.be_healthy_license_2014141300.be_healthy.Advice
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.adapter.AdviceAdapter
import com.be_healthy_license_2014141300.be_healthy.slide_helper.AdviceListHelper
import com.be_healthy_license_2014141300.be_healthy.slide_helper.ListHelper

class MainFragment : Fragment(), ListHelper.OnSwipeListener {

    private var data = mutableListOf<Advice>()
    private lateinit var adapter: AdviceAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content = inflater!!.inflate(R.layout.fragment_main, container, false)
        createAdviceList()
        val recyclerView = content?.findViewById(R.id.advice_list) as RecyclerView
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        adapter = AdviceAdapter(data)
        recyclerView.adapter = adapter
        val itemTouchHelperCallback = AdviceListHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        return content
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        if (viewHolder is AdviceAdapter.MyViewHolder) {
            adapter.removeItem(viewHolder.getAdapterPosition())
            data.add(getUniqueData())
            adapter.notifyDataSetChanged()
        }
    }

    private fun getUniqueData():Advice{
        var advice=Advice(activity)
        var unique=false
        while(!unique){
            unique=true
            for(i in 0.. data.size-1){
                if (advice==data[i]){
                    unique=false
                }
            }
            if (!unique){
                advice=Advice(activity)
            }
        }
        return advice
    }

    private fun createAdviceList(){
        data.clear()
        data.add(getUniqueData())
        if (activity.resources.configuration.orientation== Configuration.ORIENTATION_LANDSCAPE) {
            data.add(getUniqueData())
            data.add(getUniqueData())
        }
    }
}
