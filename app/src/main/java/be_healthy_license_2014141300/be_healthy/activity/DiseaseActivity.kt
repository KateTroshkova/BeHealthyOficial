package com.be_healthy_license_2014141300.be_healthy.activity

import android.app.Fragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.util.TypedValue
import android.view.Menu
import android.view.View
import be_healthy_license_2014141300.be_healthy.activity.NavigationActivity
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import com.be_healthy_license_2014141300.be_healthy.fragment.TreatmentFragment
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView
import org.intellij.lang.annotations.MagicConstant
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import be_healthy_license_2014141300.be_healthy.disease.CurableDisease
import com.be_healthy_license_2014141300.be_healthy.adapter.SavedDiseaseAdapter
import java.lang.NullPointerException


class DiseaseActivity : NavigationActivity(){

    private lateinit var treatment: Fragment
    private lateinit var magic: Fragment
    @JvmField var disease: Disease?=null
    private lateinit var checkBox:CheckBox
    //private var needMenu=true

    private var receiver=object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            try {
                var data = intent?.getStringArrayListExtra(resources.getString(R.string.param_saved_list))
                if (disease?.name in data!!){
                    checkBox.isChecked=true
                }
            }
            catch(e: NullPointerException){

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(resources.getString(R.string.action_save)))
        setContentView(R.layout.activity_disease)
        setUpToolBar()
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId=SEARCH

        checkBox=findViewById<CheckBox>(R.id.checkBoxFavor)
        checkBox.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (!p1){
                    checkBox.isChecked=!p1
                }
                else{
                    DB_Operation(this@DiseaseActivity).saveDisease(disease!!)
                    Toast.makeText(this@DiseaseActivity, resources.getString(R.string.saved_info), Toast.LENGTH_SHORT).show()
                }
            }
        })
        //navigationView.setNavigationItemSelectedListener(this)
        //navigationView.setCheckedItem(SEARCH)
        navigationView.menu.getItem(SEARCH).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease))) {
            disease = intent.getParcelableExtra<Disease>(resources.getString(R.string.param_disease)) as Disease
        }
        //if (intent.hasExtra(resources.getString(R.string.param_from_saved))) {
            //if (intent.getBooleanExtra(resources.getString(R.string.param_from_saved), false)) {
                //(findViewById(R.id.fab)).visibility=View.INVISIBLE
                //needMenu=false
          //  }
        //}
        val name=findViewById<TextView>(R.id.name)
        name.text=disease?.name
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, name.textSize*(application as CustomApplication).size_coef)

        val description=findViewById<TextView>(R.id.description)
        description.text=disease?.description

        val warning=findViewById<TextView>(R.id.warning)
        warning.text=disease?.warning

        var symptoms=""
        for(symptom in disease?.symptoms!!){
            symptoms+=symptom+","
        }
        symptoms=symptoms.removeSuffix(",")
        val symptomsText=findViewById<CustomSizeTextView>(R.id.symptoms_list)
        symptomsText.text=symptoms
        symptomsText.setTypeface(symptomsText.typeface, Typeface.ITALIC)

        (findViewById<View>(R.id.back_button)).setOnClickListener { this@DiseaseActivity.onBackPressed() }

        treatment = TreatmentFragment()
        magic = TreatmentFragment()
        if (disease?.warning?.isEmpty()!!) {
            findViewById<CustomSizeTextView>(R.id.treatment).visibility=View.VISIBLE
            findViewById<CustomSizeTextView>(R.id.magic).visibility=View.VISIBLE
            (treatment as TreatmentFragment).setData(disease?.treatment!!)
            (magic as TreatmentFragment).setData(disease?.magic!!)

            val fragmentTranslation = fragmentManager.beginTransaction()
            fragmentTranslation.hide(treatment)
            fragmentTranslation.hide(magic)
            fragmentTranslation.commit()
        }
        else{
            findViewById<CustomSizeTextView>(R.id.treatment).visibility=View.INVISIBLE
            findViewById<CustomSizeTextView>(R.id.magic).visibility=View.INVISIBLE
        }
        DB_Operation(this).readDisease()
    }

    fun showTreatment(view: View){
        show(R.id.treatment, R.id.t_frame, treatment)
    }

    fun showMagic(view: View){
        show(R.id.magic, R.id.m_frame, magic)
    }

    private fun show(textId:Int, backgroundId:Int, fragment:Fragment){
        val text=findViewById<TextView>(textId)
        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.replace(backgroundId, fragment)
        fragmentTranslation.show(fragment)
        text.setBackgroundResource(R.drawable.background_dark)
        if (!fragment.isHidden){
            fragmentTranslation.hide(fragment)
            text.setBackgroundResource(R.drawable.background_light)
        }
        fragmentTranslation.commit()
    }
}
