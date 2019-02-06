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
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
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
import android.widget.RelativeLayout
import android.view.ViewGroup.LayoutParams.FILL_PARENT
import android.widget.LinearLayout
import be_healthy_license_2014141300.be_healthy.InfoDialog
import be_healthy_license_2014141300.be_healthy.activity.MenuActivity
import be_healthy_license_2014141300.be_healthy.activity.SearchActivity
import kotlinx.android.synthetic.main.app_bar_main.*

class DiseaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    private lateinit var treatment: Fragment
    private lateinit var magic: Fragment
    @JvmField var disease: Disease?=null
    private lateinit var checkBox:CheckBox

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
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
        checkBox=findViewById<CheckBox>(R.id.checkBoxFavor)
        checkBox.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if (!p1){
                    DB_Operation(this@DiseaseActivity).deleteDisease(disease?.name!!)
                }
                else{
                    DB_Operation(this@DiseaseActivity).saveDisease(disease!!)
                    Toast.makeText(this@DiseaseActivity, "Заболевание добавлено в сохраненные", Toast.LENGTH_SHORT).show()
                }
            }
        })

        navigationView.menu.getItem(1).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease))) {
            disease = intent.getParcelableExtra<Disease>(resources.getString(R.string.param_disease)) as Disease
        }

        val name=findViewById<TextView>(R.id.name)
        name.text=disease?.name

        val description=findViewById<TextView>(R.id.description)
        description.text=disease?.description

        val warning=findViewById<TextView>(R.id.warning)
        warning.text=disease?.warning

        var symptoms=""
        for(symptom in disease?.symptoms!!){
            symptoms+=symptom+","
        }
        symptoms=symptoms.removeSuffix(",")
        symptoms.replaceFirst(symptoms[0], symptoms[0].toUpperCase())
        val symptomsText=findViewById<CustomSizeTextView>(R.id.symptoms_list)
        symptomsText.text=symptoms

        (findViewById<View>(R.id.back_button)).setOnClickListener { this@DiseaseActivity.onBackPressed() }

        treatment = TreatmentFragment()
        magic = TreatmentFragment()
        if (disease?.warning?.isEmpty()!!) {
            findViewById<LinearLayout>(R.id.long_layout).visibility=View.VISIBLE
            var warning=findViewById<ScrollView>(R.id.warningView)
            warning.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0)
            (treatment as TreatmentFragment).setData(disease?.treatment!!)
            (magic as TreatmentFragment).setData(disease?.magic!!)

            val fragmentTranslation = fragmentManager.beginTransaction()
            fragmentTranslation.hide(treatment)
            fragmentTranslation.hide(magic)
            fragmentTranslation.commit()
        }
        else{
            findViewById<LinearLayout>(R.id.long_layout).visibility=View.INVISIBLE
        }
        DB_Operation(this).readDisease()
        showTreatment(null)

        var preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.getBoolean(resources.getString(R.string.param_first_disease), false)) {
            var dialog = InfoDialog(R.layout.n_dialog_save)
            dialog.show(fragmentManager, "")
            var editor=preferences.edit()
            editor.putBoolean(resources.getString(R.string.param_first_disease), true)
            editor.apply()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                val intent= Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.search -> {
                val intent= Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    fun showTreatment(view: View?){
        show((view==null), R.id.treatment, R.id.t_frame, R.id.treatment_arrow, treatment)
    }

    fun showMagic(view: View?){
        show((view==null), R.id.magic, R.id.m_frame, R.id.magic_arrow, magic)
    }

    private fun show(firstTime:Boolean, textId:Int, backgroundId:Int, arrowId:Int, fragment:Fragment){
        val text=findViewById<TextView>(textId)
        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.replace(backgroundId, fragment)
        fragmentTranslation.show(fragment)
        text.setTypeface(null, Typeface.BOLD)
        findViewById<CheckBox>(arrowId).isChecked=true
        if (!fragment.isHidden && !firstTime){
            fragmentTranslation.hide(fragment)
            findViewById<CheckBox>(arrowId).isChecked=false
            text.setTypeface(null, Typeface.NORMAL)
        }
        fragmentTranslation.commit()
    }
}
