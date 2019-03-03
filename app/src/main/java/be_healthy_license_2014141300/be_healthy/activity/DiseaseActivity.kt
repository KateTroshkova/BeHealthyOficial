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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
        navigationView.menu.getItem(1).isChecked = true

        disease=getDisease()

        fillConstantInfo()
        fillCheckBox()
        initFragments()

        (findViewById<View>(R.id.back_button)).setOnClickListener { this@DiseaseActivity.onBackPressed() }
        showDialog()
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

    private fun fillConstantInfo(){
        setDataToTextView(R.id.name, disease!!.name)
        setDataToTextView(R.id.description, disease!!.description)
        setDataToTextView(R.id.symptoms_list, symptomsListToString())
    }

    private fun fillCheckBox(){
        val checkBox=findViewById<CheckBox>(R.id.checkBoxFavor)
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
        var disposable = DBOperation(this).readDisease().subscribe { data->
            try {
                if (disease?.name in data!!){
                    checkBox.isChecked=true
                }
            }
            catch(e: NullPointerException){

            }
        }
    }

    private fun initFragments(){
        treatment = TreatmentFragment()
        doctor=TreatmentFragment()
        (treatment as TreatmentFragment).setData(disease?.treatment!!)
        (doctor as TreatmentFragment).setData(mutableListOf(disease?.doctor!!))

        val fragmentTranslation = fragmentManager.beginTransaction()
        fragmentTranslation.hide(treatment)
        fragmentTranslation.hide(doctor)
        fragmentTranslation.commit()
    }

    private fun setDataToTextView(id:Int, name:String){
        (findViewById<TextView>(id)).text=name
    }

    private fun getDisease():Disease?{
        if (intent.hasExtra(resources.getString(R.string.param_disease))) {
            return intent.getParcelableExtra(resources.getString(R.string.param_disease)) as Disease
        }
        return null
    }

    private fun symptomsListToString():String{
        var symptoms=""
        for(symptom in disease?.symptoms!!){
            symptoms+= "$symptom,"
        }
        symptoms=symptoms.removeSuffix(",")
        symptoms.replaceFirst(symptoms[0], symptoms[0].toUpperCase())
        return symptoms
    }

    private fun showDialog(){
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

    fun showTreatment(view: View?){
        changeState(R.id.treatment, R.id.t_frame, R.id.treatment_arrow, treatment)
    }

    fun showDoctor(view:View?){
        changeState(R.id.doctor, R.id.d_frame, R.id.doctor_arrow, doctor)
    }

    private fun changeState(textId:Int, backgroundId:Int, arrowId:Int, fragment:Fragment){
        val text=findViewById<TextView>(textId)
        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.replace(backgroundId, fragment)
        if (fragment.isHidden) {
            fragmentTranslation.show(fragment)
            text.setTypeface(null, Typeface.BOLD)
            findViewById<CheckBox>(arrowId).isChecked = true
        }
        else{
            fragmentTranslation.hide(fragment)
            text.setTypeface(null, Typeface.NORMAL)
            findViewById<CheckBox>(arrowId).isChecked=false
        }
        fragmentTranslation.commit()
    }
}
