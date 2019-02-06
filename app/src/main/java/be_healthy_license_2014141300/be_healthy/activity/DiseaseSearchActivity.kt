package be_healthy_license_2014141300.be_healthy.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import be_healthy_license_2014141300.be_healthy.InfoDialog
import be_healthy_license_2014141300.be_healthy.adapter.DiseaseAdapter
import be_healthy_license_2014141300.be_healthy.adapter.SearchAdapter
import be_healthy_license_2014141300.be_healthy.dialog.RateAppDialog
import be_healthy_license_2014141300.be_healthy.disease.StaticDiseaseData
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.DiseaseActivity
import com.be_healthy_license_2014141300.be_healthy.activity.OptionActivity
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import com.jakewharton.rxbinding2.widget.RxTextView
import java.util.ArrayList

class DiseaseSearchActivity : AppCompatActivity(){

    private var diseases= mutableListOf<Disease>()
    private var availableDiseases= mutableListOf<Disease>()
    private var adapter: DiseaseAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_diseasse_search)
        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }
        initData()
        val list=findViewById<ListView>(R.id.symptom_list)
        adapter= DiseaseAdapter(this, availableDiseases)
        list.adapter=adapter
        val userInputText=findViewById<EditText>(R.id.editText)
        userInputText.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                userInputText.hint = ""
                userInputText.isCursorVisible = true
            }
            else {
                userInputText.hint = getString(R.string.choose_disease)
                userInputText.isCursorVisible = false
            }
        }
        RxTextView.textChanges(userInputText).subscribe{ string->
            val subtext=userInputText.text.toString().toLowerCase()
            updateSymptoms(subtext)
            adapter!!.notifyDataSetChanged()
        }
        list.setOnItemClickListener { p0, p1, p2, p3 ->
            val intent = android.content.Intent(this@DiseaseSearchActivity, DiseaseActivity::class.java)
            intent.putExtra(resources.getString(R.string.param_disease), adapter!!.getItem(p2) as Disease)
            initData()
            adapter?.notifyDataSetChanged()
            findViewById<EditText>(R.id.editText).setText("")
            startActivity(intent)
        }
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        userInputText.clearFocus()
        setSupportActionBar(toolBar)
        showInfoDialog()
    }

    private fun updateSymptoms(subtext:String){
        availableDiseases.clear()
        for(disease in diseases){
            if (subtext in disease.name.toLowerCase()){
                availableDiseases.add(disease)
            }
        }
    }

    private fun initData(){
        diseases = StaticDiseaseData.diseases
        for(disease in diseases){
            availableDiseases.add(disease)
        }
        availableDiseases.reverse()
    }

    private fun showInfoDialog(){
        if (getUseTimes()<=0) {
            val dialog = InfoDialog(R.layout.n_dialog_disease_search)
            dialog.show(fragmentManager, "")
            val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putInt(getString(R.string.param_disease_dialog), 42)
            editor.apply()
        }
    }

    private fun getUseTimes():Int{
        val preferences=getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
        return preferences.getInt(getString(R.string.param_disease_dialog), 0)
    }
}
