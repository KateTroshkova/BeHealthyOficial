package com.be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.fragment.HeartFragment
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val CAMERA_PERMISSION=0
    private val WAKE_PERMISSION=1

    private lateinit var navigationView: NavigationView

    private val MAIN=0
    private val SEARCH=1
    private val HEART=2
    private val EYE=3
    private val SAVE=4
    private val ALARM=5
    private val SETTINGS=6

    private var currentState=0

    private val fragments = hashMapOf(MAIN to MainFragment(), SEARCH to SearchFragment(), HEART to HeartFragment(),
            EYE to EyeFragment(), SAVE to SavedFragment(), ALARM to AlarmFragment(), SETTINGS to SettingsFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportLanguage()
        setContentView(R.layout.activity_main)
        val toolBar=findViewById(R.id.toolbar) as Toolbar
        toolBar.title=""
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        if(intent.hasExtra(resources.getString(R.string.param_state))){
            currentState=intent.getIntExtra(resources.getString(R.string.param_state), 0)
            setFragment(fragments[currentState])
            setBackground(currentState)
        }
        else {
            setFragment(fragments[MAIN])
            setBackground(MAIN)
        }
    }

    override fun onSaveInstanceState(outState: Bundle){
        super.onSaveInstanceState(outState)
        outState.putInt(resources.getString(R.string.param_state), currentState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val state=savedInstanceState?.getInt(resources.getString(R.string.param_state), 0)
        if (state!=null) {
            currentState = state
            setFragment(fragments[currentState])
            setBackground(currentState)
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

    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                setFragment(fragments[MAIN])
                setBackground(MAIN)
            }
            R.id.search -> {
                setFragment(fragments[SEARCH])
                setBackground(SEARCH)
            }
            R.id.heart -> {
                checkCameraPermission()
                setBackground(HEART)
            }
            R.id.eye -> {
                setFragment(fragments[EYE])
                setBackground(EYE)
            }
            R.id.saved -> {
                setFragment(fragments[SAVE])
                setBackground(SAVE)
            }
            R.id.alarm -> {
                checkWakeLockPermission()
                setFragment(fragments[ALARM])
                setBackground(ALARM)
            }
            R.id.settings->{
                setFragment(fragments[SETTINGS])
                setBackground(SETTINGS)
            }
            R.id.share -> {
                ShareManager(this).saveUrl()
                Toast.makeText(this, resources.getString(R.string.shared_info), Toast.LENGTH_SHORT).show()
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        }
        else{
            setFragment(fragments[HEART])
        }
    }

    private fun checkWakeLockPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WAKE_LOCK), WAKE_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setFragment(fragments[HEART])
                }
            }
            WAKE_PERMISSION->{

            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBackground(checked:Int){
        currentState=checked
        if (navigationView.menu!=null) {
            for (i in (MAIN..SETTINGS)) {
                navigationView.menu.getItem(i).isChecked = false
            }
            navigationView.menu.getItem(checked).isChecked = true
        }
    }

    private fun setFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentTransaction!=null) {
            fragmentTransaction.replace(R.id.content, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun supportLanguage(){
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val languageToLoad = preferences.getString(resources.getString(R.string.param_language), "ru")
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locale = locale
        }
        else {
            config.setLocale(locale)
        }
        resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}

