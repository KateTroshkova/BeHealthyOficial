package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.*
import android.widget.*
import be_healthy_license_2014141300.be_healthy.dialog.SearchDialog
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.OptionActivity
import com.be_healthy_license_2014141300.be_healthy.adapter.SymptomsAdapter
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import be_healthy_license_2014141300.be_healthy.disease.*
import be_healthy_license_2014141300.be_healthy.slide_helper.SymptomsListHelper
import com.be_healthy_license_2014141300.be_healthy.disease.*
import com.be_healthy_license_2014141300.be_healthy.slide_helper.ListHelper
import java.util.*
import com.kobakei.ratethisapp.RateThisApp
import kotlin.concurrent.fixedRateTimer


class SearchFragment : Fragment(), View.OnClickListener, SearchDialog.OnChooseDiseaseListener, ListHelper.OnSwipeListener{

    private lateinit var diseases:MutableList<Disease>
    private var symptomsForSearch = mutableListOf<String>()
    private lateinit var symptomsAdapter: SymptomsAdapter
    private lateinit var chosenSymptomsList:RecyclerView
    private lateinit var instruction:TextView
    private lateinit var button:Button
    private lateinit var currentActivity:Activity

    //активность без предупреждения обращается в null при смене ориентации
    //все адекватные контексты дают неадекватный результат
    var fuckingEXISTactivity:Activity?=null

    companion object {
        private var fragment:SearchFragment?=null

        fun getInstance(): SearchFragment {
            if (fragment==null){
                fragment=SearchFragment()
            }
            return fragment as SearchFragment
        }
    }

    private val receiver=object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.hasExtra(context?.resources?.getString(R.string.param_symptoms_for_search))!!) {
                symptomsForSearch = intent.getStringArrayListExtra(context?.resources?.getString(R.string.param_symptoms_for_search))
                symptomsAdapter = SymptomsAdapter(symptomsForSearch)
                chosenSymptomsList.adapter = symptomsAdapter
                if (symptomsForSearch.size>0){
                    instruction.text=""
                    button.isClickable=true
                    if (fuckingEXISTactivity!=null) {
                        button.setBackgroundColor(fuckingEXISTactivity!!.resources?.getColor(R.color.colorAccent)!!)
                        button.visibility=View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onChooseDisease(value: String) {
        if (value !in symptomsForSearch) {
            symptomsForSearch.add(value)
            DB_Operation(this.fuckingEXISTactivity!!).saveSymptom(value)
            symptomsAdapter.notifyDataSetChanged()
            instruction.text=""
            button.isClickable=true
            if (fuckingEXISTactivity!=null) {
                button.setBackgroundColor(fuckingEXISTactivity!!.resources?.getColor(R.color.colorAccent)!!)
                button.visibility=View.VISIBLE
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content=inflater!!.inflate(R.layout.fragment_search, container, false)
        fragment=this
        currentActivity=activity

        diseases= StaticDiseaseData.diseases
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_read_ready)))
        DB_Operation(activity).readSymptoms()
        fuckingEXISTactivity=activity

        chosenSymptomsList=content.findViewById<RecyclerView>(R.id.list_for_search)
        val mLayoutManager = LinearLayoutManager(activity)
        chosenSymptomsList.layoutManager = mLayoutManager
        chosenSymptomsList.itemAnimator = DefaultItemAnimator()
        chosenSymptomsList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        //adapter = AdviceAdapter(data)
        //recyclerView.adapter = adapter
        val itemTouchHelperCallback = SymptomsListHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(chosenSymptomsList)
        instruction=content.findViewById<TextView>(R.id.instruction)

        button=content.findViewById<Button>(R.id.search)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(activity.application as CustomApplication).size_coef*0.6f)
        button.setOnClickListener(this)
        button.isClickable=false
        button.setBackgroundColor(activity.resources.getColor(R.color.colorGray))
        button.visibility=View.INVISIBLE

        (content.findViewById<View>(R.id.add_button)).setOnClickListener(this)

        val preferences=activity.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        var rate_time = preferences.getInt(getString(R.string.param_current_rate_time), 0)
        if (rate_time<=0) {
            val config = RateThisApp.Config(42, 3)
            RateThisApp.init(config)
            config.setYesButtonText(R.string.yes_rate)
            config.setNoButtonText(R.string.no_rate)
            config.setCancelButtonText(R.string.blah_rate)
            val editor=preferences.edit()
            editor.putInt(getString(R.string.param_current_rate_time), 0)
            editor.apply()
        }
        RateThisApp.onCreate(currentActivity)
        RateThisApp.showRateDialogIfNeeded(currentActivity)
        RateThisApp.setCallback(object : RateThisApp.Callback {
            override fun onYesClicked() {
                RateThisApp.stopRateDialog(currentActivity)
            }

            override fun onNoClicked() {
                val preferences=activity.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                var rate_time = preferences.getInt(getString(R.string.param_current_rate_time), 0)
                rate_time++
                try {
                    var nextRate = resources.getIntArray(R.array.rate_times_array)[rate_time]
                    val config = RateThisApp.Config(42, nextRate)
                    RateThisApp.init(config)
                    RateThisApp.onCreate(currentActivity)
                    RateThisApp.showRateDialogIfNeeded(currentActivity)
                }
                catch(e:ArrayIndexOutOfBoundsException){
                    RateThisApp.stopRateDialog(currentActivity)
                    return
                }
                val editor=preferences.edit()
                editor.putInt(getString(R.string.param_current_rate_time), rate_time)
                editor.apply()
            }

            override fun onCancelClicked() {
                val preferences=activity.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                var rate_time = preferences.getInt(getString(R.string.param_current_rate_time), 0)
                rate_time++
                try {
                    var nextRate = resources.getIntArray(R.array.rate_times_array)[rate_time]
                    val config = RateThisApp.Config(42, nextRate)
                    RateThisApp.init(config)
                    RateThisApp.onCreate(currentActivity)
                    RateThisApp.showRateDialogIfNeeded(currentActivity)
                }
                catch(e:ArrayIndexOutOfBoundsException){
                    RateThisApp.stopRateDialog(currentActivity)
                    return
                }
                val editor=preferences.edit()
                editor.putInt(getString(R.string.param_current_rate_time), rate_time)
                editor.apply()
            }
        })

        return content
    }

    override fun onClick(p0: View?) {
        if (p0!=null) {
            if (p0.id==R.id.search) {
                DB_Operation(activity).cleanSymptoms()
                instruction.text=activity.resources.getString(R.string.s_instruction)
                val intent = Intent(activity, OptionActivity::class.java)
                intent.putExtra(activity.resources.getString(R.string.param_disease_list), prepareList(sort()))
                startActivity(intent)
                symptomsForSearch.clear()
                symptomsAdapter.notifyDataSetChanged()
                button.isClickable=false
                button.setBackgroundColor(activity.resources.getColor(R.color.colorGray))
                button.visibility=View.INVISIBLE
               // if ((activity as MainActivity).mInterstitialAd.isLoaded()) {
               //     (activity as MainActivity).mInterstitialAd.show()
                //}
            }
            if (p0.id==R.id.add_button){
                val dialog=SearchDialog()
                dialog.setListener(this)
                dialog.show(activity.fragmentManager, "")
            }
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        DB_Operation(activity).deleteSymptom(symptomsForSearch[position])
        symptomsForSearch.removeAt(position)
        symptomsAdapter.notifyDataSetChanged()
        if (symptomsForSearch.isEmpty()){
            instruction.text=activity.resources.getString(R.string.s_instruction)
            button.isClickable=false
            button.setBackgroundColor(activity.resources.getColor(R.color.colorGray))
            button.visibility=View.INVISIBLE
        }
    }

    private fun prepareList(data:MutableList<Disease>):ArrayList<Disease>{
        val result = arrayListOf<Disease>()
        for(key in data){
            //if(result.size<5) {
                result.add(0, key)
           // }
        }
        return result
    }

    private fun sort():MutableList<Disease>{
        var res = mapOf<Disease, Int>()
        for(disease in diseases){
            val count= symptomsForSearch.count { it in disease.symptoms }
            if (count>0) {
                res=res.plus(mapOf(disease to count))
            }
        }
        res=res.toList().sortedBy { (_, value) -> value }.toMap()
        return res.keys.toMutableList()
    }

    /**fun stop(){
        if (fuckingEXISTactivity!=null) {
            LocalBroadcastManager.getInstance(fuckingEXISTactivity).unregisterReceiver(receiver)
        }
    }*/
}