package com.be_healthy_license_2014141300.be_healthy.activity

import android.app.Fragment
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.activity.NavigationActivity
import be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import com.be_healthy_license_2014141300.be_healthy.fragment.TreatmentFragment
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView
import org.intellij.lang.annotations.MagicConstant

class DiseaseActivity : NavigationActivity() {

    private lateinit var treatment: Fragment
    private lateinit var magic: Fragment
    @JvmField var disease: Disease?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)
        setUpToolBar()
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(SEARCH)
        navigationView.menu.getItem(SEARCH).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease))) {
            disease = intent.getParcelableExtra<Disease>(resources.getString(R.string.param_disease)) as Disease
        }
        val name=findViewById(R.id.name) as TextView
        name.text=disease?.name
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, name.textSize*(application as CustomApplication).size_coef)

        val description=findViewById(R.id.description) as TextView
        description.text=disease?.description

        var symptoms=""
        for(symptom in disease?.symptoms!!){
            symptoms+=symptom+","
        }
        symptoms=symptoms.removeSuffix(",")
        val symptomsText=findViewById(R.id.symptoms_list) as CustomSizeTextView
        symptomsText.text=symptoms
        symptomsText.setTypeface(symptomsText.typeface, Typeface.ITALIC)

        treatment=TreatmentFragment()
        magic=TreatmentFragment()
        (treatment as TreatmentFragment).setData(disease?.treatment!!)
        (magic as TreatmentFragment).setData(disease?.magic!!)

        val button=findViewById(R.id.fab) as Button
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.textSize*(application as CustomApplication).size_coef*0.6f)

        (findViewById(R.id.back_button)).setOnClickListener { this@DiseaseActivity.onBackPressed() }

        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.hide(treatment)
        fragmentTranslation.hide(magic)
        fragmentTranslation.commit()
    }

    fun showTreatment(view: View){
        show(R.id.treatment, R.id.t_frame, treatment)
    }

    fun showMagic(view: View){
        show(R.id.magic, R.id.m_frame, magic)
    }

    fun save(view: View){
        DB_Operation(this).saveDisease(disease!!)
        Toast.makeText(this, resources.getString(R.string.saved_info), Toast.LENGTH_SHORT).show()
    }

    private fun show(textId:Int, backgroundId:Int, fragment:Fragment){
        val text=findViewById(textId) as TextView
        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.replace(backgroundId, fragment)
        fragmentTranslation.show(fragment)
        text.setBackgroundResource(R.drawable.dark_background)
        if (!fragment.isHidden){
            fragmentTranslation.hide(fragment)
            text.setBackgroundResource(R.drawable.light_background)
        }
        fragmentTranslation.commit()
    }
}
