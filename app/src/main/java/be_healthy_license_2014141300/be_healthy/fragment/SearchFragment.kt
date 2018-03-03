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
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.OptionActivity
import com.be_healthy_license_2014141300.be_healthy.adapter.SimpleListAdapter
import com.be_healthy_license_2014141300.be_healthy.adapter.SpinnerAdapter
import com.be_healthy_license_2014141300.be_healthy.adapter.SymptomsAdapter
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.disease.*
import java.util.*

class SearchFragment : Fragment(), AdapterView.OnItemSelectedListener,
        View.OnClickListener,
        AdapterView.OnItemClickListener,
        ViewTreeObserver.OnGlobalLayoutListener{

    private var diseases=mutableListOf<Disease>()
    private var allSymptoms = mutableListOf<String>()
    private var symptomsForSearch = mutableListOf<String>()
    private var currentSymptoms = mutableListOf<String>()
    private lateinit var adapter: SimpleListAdapter
    private lateinit var symptomsAdapter: SymptomsAdapter
    private lateinit var chosenSymptomsList:ListView
    private lateinit var spinner: Spinner
    private lateinit var text: EditText

    //активность без предупреждения обращается в null при смене ориентации
    //все адекватные контексты дают неадекватный результат
    var fuckingEXISTactivity:Activity?=null

    private val receiver=object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.hasExtra(context?.resources?.getString(R.string.param_symptoms_for_search))!!) {
                symptomsForSearch = intent.getStringArrayListExtra(context?.resources?.getString(R.string.param_symptoms_for_search))
                symptomsAdapter = SymptomsAdapter(fuckingEXISTactivity!!, symptomsForSearch)
                chosenSymptomsList.adapter = symptomsAdapter
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val content=inflater!!.inflate(R.layout.fragment_search, container, false)

        initData()
        LocalBroadcastManager.getInstance(activity).registerReceiver(receiver, IntentFilter(activity.resources.getString(R.string.action_read_ready)))
        DB_Operation(activity).readSymptoms()
        fuckingEXISTactivity=activity

        chosenSymptomsList=content.findViewById(R.id.list_for_search) as ListView

        val list = content.findViewById(R.id.all_symptoms_list)  as ListView
        adapter= SimpleListAdapter(activity, currentSymptoms)
        list.adapter = adapter
        list.onItemClickListener = this

        spinner = content.findViewById(R.id.all_symptoms_spinner) as Spinner
        val spinnerAdapter= SpinnerAdapter(activity, R.layout.simple_custom_item, allSymptoms)
        //spinnerAdapter.setDropDownViewResource(R.layout.simple_custom_item)
        spinner.adapter=spinnerAdapter
        spinner.onItemSelectedListener = this

        text=content.findViewById(R.id.symptom_text) as EditText
        text.addTextChangedListener(watcher)
        content.viewTreeObserver.addOnGlobalLayoutListener(this)

        val button=content.findViewById(R.id.search) as Button
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(activity.application as CustomApplication).size_coef*0.6f)
        button.setOnClickListener(this)

        return content
    }

    override fun onClick(p0: View?) {
        DB_Operation(activity).cleanSymptoms()
        val extra = arrayListOf<Disease>()
        val sortedDiseases=sort()
        for(key in sortedDiseases){
            extra.add(0, key)
        }
        val intent= Intent(activity, OptionActivity::class.java)
        intent.putExtra(activity.resources.getString(R.string.param_disease_list), extra)
        startActivity(intent)
        currentSymptoms.clear()
        symptomsForSearch.clear()
        symptomsAdapter.notifyDataSetChanged()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        if (position>0 && allSymptoms[position] !in symptomsForSearch) {
            symptomsForSearch.add(allSymptoms[position])
            symptomsAdapter.notifyDataSetChanged()
            DB_Operation(activity).saveSymptom(allSymptoms[position])
        }
        spinner.setSelection(0)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (currentSymptoms[position] !in symptomsForSearch) {
            symptomsForSearch.add(currentSymptoms[position])
            DB_Operation(activity).saveSymptom(currentSymptoms[position])
        }
        symptomsAdapter.notifyDataSetChanged()
        hideKeyboard()
    }

    var watcher=object: TextWatcher {

        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
            spinner.visibility= View.INVISIBLE
            currentSymptoms.clear()
            for(symptom in allSymptoms){
                if (symptom != resources.getString(R.string.spinner_hint)) {
                    currentSymptoms.add(symptom)
                }
            }
        }

        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            currentSymptoms.clear()
            val subtext:String=this@SearchFragment.text.text?.toString()!!
            for(symptom in allSymptoms) {
                if (subtext in symptom && symptom!=resources.getString(R.string.spinner_hint)) {
                    currentSymptoms.add(symptom)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onGlobalLayout() {
        if (view !=null) {
            if (!isKeyboardOpen()) {
                text.text?.clear()
                currentSymptoms.clear()
                adapter.notifyDataSetChanged()
                spinner.visibility = View.VISIBLE
            }
        }
    }

    private fun sort():MutableList<Disease>{
        var res = mapOf<Disease, Int>()
        for(disease in diseases){
            var count=0
            for(symptom in symptomsForSearch){
                if (symptom in disease.symptoms){
                    count++
                }
            }
            if (count>0) {
                res=res.plus(mapOf(disease to count))
            }
        }
        res=res.toList().sortedBy { (_, value) -> value }.toMap()
        return res.keys.toMutableList()
    }

    private fun initData(){
        allSymptoms.clear()
        diseases = mutableListOf(Angina(activity), ARD(activity), Arrhythmia(activity), ARVI(activity),
                Asthma(activity), Caries(activity), Cataract(activity), DryEyeSyndrome(activity), Flu(activity),
                Frontite(activity), Hypertension(activity), Measles(activity), Myocarditis(activity), Myopia(activity),
                Otitis(activity), Otosclerosis(activity), Pharyngitis(activity), Tonsillitis(activity), Sprain(activity),
                BadSleep(activity), Obesity(activity), Flatfoot(activity), BrainConcussion(activity), Intoxication(activity),
                Allergy(activity), Stomatitis(activity), Gastritis(activity), Herpes(activity), Cholecystitis(activity),
                Laryngitis(activity), Osteoarthritis(activity), Atherosclerosis(activity), Bronchitis(activity), Scurvy(activity),
                Hives(activity), NailFungus(activity))
        for(disease in diseases){
            for(symptom in disease.symptoms) {
                if (symptom !in allSymptoms){
                    allSymptoms.add(symptom)
                }
            }
        }
        Collections.sort(allSymptoms)
        if (resources.getString(R.string.spinner_hint) !in allSymptoms) {
            allSymptoms.add(0, resources.getString(R.string.spinner_hint))
        }
    }

    private fun isKeyboardOpen():Boolean{
        if (view!=null) {
            val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            return view.rootView.height - view.height > metrics.heightPixels/2
        }
        return false
    }

    private fun hideKeyboard(){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(SymptomsFragment@this.view.windowToken, 0)
    }
}