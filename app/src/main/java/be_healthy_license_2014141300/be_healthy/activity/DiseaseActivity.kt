package be_healthy_license_2014141300.be_healthy.activity

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.database.DBOperation
import be_healthy_license_2014141300.be_healthy.dialog.InfoDialog
import be_healthy_license_2014141300.be_healthy.disease.Disease
import be_healthy_license_2014141300.be_healthy.fragment.TreatmentFragment
import com.be_healthy_license_2014141300.be_healthy.R

class DiseaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    private lateinit var treatment: Fragment
    private lateinit var doctor:Fragment
    @JvmField var disease: Disease?=null
    private lateinit var checkBox:CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
        checkBox=findViewById(R.id.checkBoxFavor)
        checkBox.setOnCheckedChangeListener { _, checked ->
            if (!checked){
                var disposable = DBOperation(this@DiseaseActivity)
                        .deleteDisease(disease?.name!!)
                        .subscribe({Log.e("LOG", "next")},
                        { Log.e("LOG", "error")},
                        {Toast.makeText(this@DiseaseActivity,
                                "Заболевание удалено из сохраненных", Toast.LENGTH_SHORT).show()})
            } else{
                var disposable = DBOperation(this@DiseaseActivity)
                        .saveDisease(disease!!)
                        .subscribe({Log.e("LOG", "next")},
                        { Log.e("LOG", "error")},
                        {Toast.makeText(this@DiseaseActivity,
                                "Заболевание добавлено в сохраненные", Toast.LENGTH_SHORT).show()})
            }
        }

        navigationView.menu.getItem(1).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease))) {
            disease = intent.getParcelableExtra(resources.getString(R.string.param_disease)) as Disease
        }

        val name=findViewById<TextView>(R.id.name)
        name.text=disease?.name

        val description=findViewById<TextView>(R.id.description)
        description.text=disease?.description

        var symptoms=""
        for(symptom in disease?.symptoms!!){
            symptoms+= "$symptom,"
        }
        symptoms=symptoms.removeSuffix(",")
        symptoms.replaceFirst(symptoms[0], symptoms[0].toUpperCase())
        val symptomsText=findViewById<TextView>(R.id.symptoms_list)
        symptomsText.text=symptoms

        (findViewById<View>(R.id.back_button)).setOnClickListener { this@DiseaseActivity.onBackPressed() }

        treatment = TreatmentFragment()
        doctor=TreatmentFragment()
            findViewById<LinearLayout>(R.id.long_layout).visibility=View.VISIBLE
            (treatment as TreatmentFragment).setData(disease?.treatment!!)
        (doctor as TreatmentFragment).setData(mutableListOf(disease?.doctor!!))

        val fragmentTranslation = fragmentManager.beginTransaction()
        fragmentTranslation.hide(treatment)
        fragmentTranslation.hide(doctor)
        fragmentTranslation.commit()
        var disposable = DBOperation(this).readDisease().subscribe { data->
            try {
                if (disease?.name in data!!){
                    checkBox.isChecked=true
                }
            }
            catch(e: NullPointerException){

            }
        }

        showTreatment(null)

        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.getBoolean(resources.getString(R.string.param_first_disease), false)) {
            val dialog = InfoDialog()
            dialog.setData(R.layout.n_dialog_save)
            dialog.show(fragmentManager, "")
            val editor=preferences.edit()
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

    fun showDoctor(view:View?){
        show((view==null), R.id.doctor, R.id.d_frame, R.id.doctor_arrow, doctor)
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
