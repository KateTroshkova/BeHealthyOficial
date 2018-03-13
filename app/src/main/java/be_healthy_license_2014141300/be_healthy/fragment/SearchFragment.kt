package com.be_healthy_license_2014141300.be_healthy.fragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import be_healthy_license_2014141300.be_healthy.dialog.SearchDialog
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.OptionActivity
import com.be_healthy_license_2014141300.be_healthy.adapter.SimpleListAdapter
import com.be_healthy_license_2014141300.be_healthy.adapter.SpinnerAdapter
import com.be_healthy_license_2014141300.be_healthy.adapter.SymptomsAdapter
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.disease.*
import java.util.*

class SearchFragment : Fragment(), View.OnClickListener, SearchDialog.OnChooseDiseaseListener{

    private lateinit var diseases:MutableList<Disease>
    private var symptomsForSearch = mutableListOf<String>()
    private lateinit var symptomsAdapter: SymptomsAdapter
    private lateinit var chosenSymptomsList:ListView
    private lateinit var instruction:TextView

    //активность без предупреждения обращается в null при смене ориентации
    //все адекватные контексты дают неадекватный результат
    var fuckingEXISTactivity:Activity?=null

    private val receiver=object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.hasExtra(context?.resources?.getString(R.string.param_symptoms_for_search))!!) {
                symptomsForSearch = intent.getStringArrayListExtra(context?.resources?.getString(R.string.param_symptoms_for_search))
                symptomsAdapter = SymptomsAdapter(fuckingEXISTactivity!!, symptomsForSearch)
                chosenSymptomsList.adapter = symptomsAdapter
                if (symptomsForSearch.size>0){
                    instruction.text=""
                }
            }
        }
    }

    override fun onChooseDisease(value: String) {
        if (value !in symptomsForSearch) {
            symptomsForSearch.add(value)
            DB_Operation(activity).saveSymptom(value)
            symptomsAdapter.notifyDataSetChanged()
            instruction.text=""
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content=inflater!!.inflate(R.layout.fragment_search, container, false)

        //initData()
        diseases= mutableListOf(Angina(activity), ARD(activity), Arrhythmia(activity), ARVI(activity),
                Asthma(activity), Caries(activity), Cataract(activity), DryEyeSyndrome(activity), Flu(activity),
                Frontite(activity), Hypertension(activity), Measles(activity), Myocarditis(activity), Myopia(activity),
                Otitis(activity), Otosclerosis(activity), Pharyngitis(activity), Tonsillitis(activity), Sprain(activity),
                BadSleep(activity), Obesity(activity), Flatfoot(activity), BrainConcussion(activity), Intoxication(activity),
                Allergy(activity), Stomatitis(activity), Gastritis(activity), Herpes(activity), Cholecystitis(activity),
                Laryngitis(activity), Osteoarthritis(activity), Atherosclerosis(activity), Bronchitis(activity), Scurvy(activity),
                Hives(activity), NailFungus(activity))
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_read_ready)))
        DB_Operation(activity).readSymptoms()
        fuckingEXISTactivity=activity

        chosenSymptomsList=content.findViewById(R.id.list_for_search) as ListView
        instruction=content.findViewById(R.id.instruction) as TextView

        val button=content.findViewById(R.id.search) as Button
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(activity.application as CustomApplication).size_coef*0.6f)
        button.setOnClickListener(this)

        (content.findViewById(R.id.add_button)).setOnClickListener(this)
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
            }
            if (p0.id==R.id.add_button){
                val dialog=SearchDialog()
                dialog.setListener(this)
                dialog.show(activity.fragmentManager, "")
            }
        }
    }

    private fun prepareList(data:MutableList<Disease>):ArrayList<Disease>{
        val result = arrayListOf<Disease>()
        for(key in data){
            result.add(0, key)
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
}