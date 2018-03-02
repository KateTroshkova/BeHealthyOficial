package com.be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.CustomApplication
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.database.DB_Operation
import com.be_healthy_license_2014141300.be_healthy.disease.Disease
import com.be_healthy_license_2014141300.be_healthy.fragment.TreatmentFragment
import com.be_healthy_license_2014141300.be_healthy.view.CustomSizeTextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class DiseaseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val MAIN=0
    private val SEARCH=1
    private val HEART=2
    private val EYE=3
    private val SAVE=4
    private val ALARM=5
    private val SETTINGS=6

    private val CAMERA_PERMISSION=0

    private lateinit var treatment: Fragment
    private lateinit var magic: Fragment
    @JvmField var disease: Disease?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)
        val toolBar=findViewById(R.id.toolbar) as Toolbar
        toolBar.title=""
        setSupportActionBar(toolBar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
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
        var symptomsText=findViewById(R.id.symptoms_list) as CustomSizeTextView
        symptomsText.text=symptoms
        symptomsText.setTypeface(symptomsText.typeface, Typeface.ITALIC)

        treatment=TreatmentFragment(disease?.treatment)
        magic= TreatmentFragment(disease?.magic)

        val fragmentTranslation=fragmentManager.beginTransaction()
        fragmentTranslation.hide(treatment)
        fragmentTranslation.hide(magic)
        fragmentTranslation.commit()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), MAIN)
                startActivity(intent)
            }
            R.id.search -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), SEARCH)
                startActivity(intent)
            }
            R.id.heart -> {
                checkCameraPermission()
            }
            R.id.eye -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), EYE)
                startActivity(intent)
            }
            R.id.saved -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), SAVE)
                startActivity(intent)
            }
            R.id.alarm -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), ALARM)
                startActivity(intent)
            }
            R.id.settings->{
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), SETTINGS)
                startActivity(intent)
            }
            R.id.share -> {
                ShareManager(this).saveUrl()
                Toast.makeText(this, resources.getString(R.string.shared_info), Toast.LENGTH_SHORT).show()
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        }
        else{
            val intent= Intent(this, MainActivity::class.java)
            intent.putExtra(resources.getString(R.string.param_state), HEART)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent= Intent(this, MainActivity::class.java)
                    intent.putExtra(resources.getString(R.string.param_state), HEART)
                    startActivity(intent)
                }
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showTreatment(view: View){
        show(R.id.treatment, R.id.t_frame, treatment)
    }

    fun showMagic(view: View){
        show(R.id.magic, R.id.m_frame, magic)
    }

    fun show(textId:Int, backgroundId:Int, fragment:Fragment){
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

    fun save(view: View){
        DB_Operation(this).saveDisease(disease!!)
        Toast.makeText(this, resources.getString(R.string.saved_info), Toast.LENGTH_SHORT).show()
    }
}
