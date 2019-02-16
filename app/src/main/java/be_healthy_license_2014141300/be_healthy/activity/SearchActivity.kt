package be_healthy_license_2014141300.be_healthy.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import be_healthy_license_2014141300.be_healthy.dialog.InfoDialog
import be_healthy_license_2014141300.be_healthy.adapter.SearchAdapter
import be_healthy_license_2014141300.be_healthy.dialog.RateAppDialog
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.disease.Disease
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.ArrayList
import android.view.inputmethod.InputMethodManager

class SearchActivity : AppCompatActivity(), SearchAdapter.OnChosenSymptomsChangeListener, RateAppDialog.OnRateListener, RateAppDialog.OnLaterListener{

    private var diseases= mutableListOf<Disease>()
    private var symptoms= mutableListOf<String>()
    private var availableSymptoms= mutableListOf<String>()
    private lateinit var searchButton:TextView
    private lateinit var header:TextView
    private var adapter:SearchAdapter?=null

    companion object {
        @JvmStatic var chosen= mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_search)
        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }
        initData()
        val list=findViewById<ListView>(R.id.symptom_list)
        adapter=SearchAdapter(this, availableSymptoms, this)
        list.adapter=adapter
        val userInputText=findViewById<EditText>(R.id.editText)

        userInputText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                userInputText.hint = ""
                userInputText.isCursorVisible=true
            }
            else {
                userInputText.hint = getString(R.string.choose_symptom)
                userInputText.isCursorVisible = false
            }
        }
        userInputText.isFocusable=false
        val disposable = RxTextView.textChanges(userInputText).subscribe{ string->
            val subtext=userInputText.text.toString().toLowerCase()
            updateSymptoms(subtext)
            adapter!!.updateCurrentSubstring(subtext)
            adapter!!.notifyDataSetChanged()
        }
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        header=findViewById(R.id.header)
        searchButton=findViewById(R.id.button)
        hideKeyboard()
        userInputText.isFocusableInTouchMode = false
        userInputText.isFocusable = false
        userInputText.isFocusableInTouchMode = true
        userInputText.isFocusable = true
        showInfoDialog()
        showRateDialog()
    }

    override fun onChosenSymptomsChange(_chosen:MutableList<String>) {
        chosen=_chosen
        if (chosen.size>0){
            header.visibility= View.INVISIBLE
            searchButton.visibility=View.VISIBLE
        }
        else{
            searchButton.visibility= View.INVISIBLE
            header.visibility=View.VISIBLE
        }
        val userInputText=findViewById<EditText>(R.id.editText)
        userInputText.setText("")
        adapter?.notifyDataSetChanged()
    }

    fun search(view:View){
        val intent = Intent(this, OptionActivity::class.java)
        intent.putExtra(resources.getString(R.string.param_disease_list), prepareList(sort()))
        intent.putExtra(resources.getString(R.string.param_symptoms_for_search), prepareSymptomList(chosen))
        findViewById<EditText>(R.id.editText).setText("")
        startActivity(intent)
    }

    private fun updateSymptoms(subtext:String){
        availableSymptoms.clear()
        for(symptom in chosen){
            availableSymptoms.add(symptom)
        }
        val temp= mutableListOf<String>()
        for(symptom in symptoms){
            if (!chosen.contains(symptom) && symptom.contains(subtext) && !temp.contains(symptom)){
                temp.add(symptom)
            }
        }
        temp.sort()
        for(symptom in temp){
            availableSymptoms.add(symptom)
        }
    }

    private fun initData(){
        diseases = StaticDiseaseData.diseases
        symptoms.clear()
        for(disease in diseases){
            disease.symptoms
                    .filter { it !in symptoms }
                    .forEach {
                        if (it.isNotEmpty()) {
                            symptoms.add(it)
                            availableSymptoms.add(it)
                        }
                    }
        }
        symptoms.sort()
        availableSymptoms.sort()
    }

    private fun showRateDialog(){
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        var rate_time=getUseTimes()
        rate_time++
        val alarmTimes=resources.getIntArray(R.array.rate_times_array)
        val needToShow=preferences.getBoolean(getString(R.string.param_need_rate), true)
        if (rate_time in alarmTimes && needToShow){
            val dialog=RateAppDialog()
            dialog.setLaterListener(this)
            dialog.setRateListener(this)
            dialog.isCancelable=false
            dialog.show(fragmentManager, "")
        }
        editor.putInt(getString(R.string.param_current_rate_time), rate_time)
        editor.apply()
    }

    override fun onLater() {

    }

    override fun onRate() {
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val editor=preferences.edit()
        editor.putBoolean(resources.getString(R.string.param_need_rate), false)
        editor.apply()
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    }

    private fun showInfoDialog(){
        if (getUseTimes()<=0) {
            val dialog = InfoDialog()
            dialog.setData(R.layout.n_dialog_search)
            dialog.show(fragmentManager, "")
        }
    }

    private fun getUseTimes():Int{
        val preferences=getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        return preferences.getInt(getString(R.string.param_current_rate_time), 0)
    }

    private fun prepareList(data:MutableList<Disease>): ArrayList<Disease> {
        val result = arrayListOf<Disease>()
        for(key in data){
            result.add(0, key)
        }
        return result
    }

    private fun prepareSymptomList(data:MutableList<String>):ArrayList<String>{
        val result = arrayListOf<String>()
        for(key in data){
            result.add(0, key)
        }
        return result
    }

    private fun hideKeyboard() {
        if(currentFocus !=null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    private fun sort():MutableList<Disease>{
        diseases.sort()
        var res = mapOf<Disease, Int>()
        for(disease in diseases){
            val count= chosen.count { it in disease.symptoms }
            if (count>0) {
                res=res.plus(mapOf(disease to count))
            }
        }
        res=res.toList().sortedBy { (_, value) -> value }.toMap()
        return res.keys.toMutableList()
    }
}
