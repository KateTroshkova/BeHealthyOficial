package com.be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.adapter.OptionAdapter
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class OptionActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val MAIN=0
    private val SEARCH=1
    private val HEART=2
    private val EYE=3
    private val SAVE=4
    private val ALARM=5
    private val SETTINGS=6

    private val CAMERA_PERMISSION=0

    @JvmField var options = mutableListOf<Disease>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(SEARCH)
        navigationView.menu.getItem(SEARCH).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease_list))) {
            options = intent.getParcelableArrayListExtra<Disease>(resources.getString(R.string.param_disease_list))
        }
        val list=findViewById(R.id.preview_list) as android.widget.ListView
        val adapter = OptionAdapter(this, options)
        list.adapter=adapter
        list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = android.content.Intent(this@OptionActivity, DiseaseActivity::class.java)
            intent.putExtra(resources.getString(R.string.param_disease), adapter.getItem(position) as Disease)
            startActivity(intent)
        }
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
}
