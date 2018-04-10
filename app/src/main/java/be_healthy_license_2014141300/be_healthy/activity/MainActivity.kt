package com.be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.activity.NavigationActivity
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import be_healthy_license_2014141300.be_healthy.fragment.HeartFragment
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.fragment.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class MainActivity : NavigationActivity() {

    private lateinit var navigationView: NavigationView

    private var currentState=0

    private val fragments = hashMapOf(MAIN to MainFragment(), SEARCH to SearchFragment(), HEART to HeartFragment(),
            EYE to EyeFragment(), SAVE to SavedFragment(), ALARM to AlarmFragment(), SETTINGS to SettingsFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpToolBar()
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
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.getBoolean(resources.getString(R.string.param_first_time), false)){
            UserTermsTask(this).execute()
            val editor=preferences.edit()
            editor.putBoolean(resources.getString(R.string.param_first_time), true)
            editor.apply()
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

    override fun onNavigationItemSelected(item: android.view.MenuItem): Boolean {
        (fragments[EYE] as EyeFragment).stop()
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

    override fun checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
        }
        else{
            setFragment(fragments[HEART])
            setBackground(HEART)
            val toolBar=findViewById(R.id.toolbar) as Toolbar
            toolBar.title=fragmentNames[HEART]
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setFragment(fragments[HEART])
                    setBackground(HEART)
                    val toolBar=findViewById(R.id.toolbar) as Toolbar
                    toolBar.title=fragmentNames[HEART]
                }
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
        val toolBar=findViewById(R.id.toolbar) as Toolbar
        toolBar.title=fragmentNames[checked]
    }

    private fun setFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentTransaction!=null) {
            fragmentTransaction.replace(R.id.content, fragment)
            fragmentTransaction.commit()
        }
    }

    private class UserTermsTask(var activity:Activity): AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            var termsOfUse=""
            var reader:BufferedReader? = null
            try {
                reader = BufferedReader(InputStreamReader(activity.assets.open("data.txt"), "UTF-8"))
                var line= reader.readLine()
                while (line!= null) {
                    termsOfUse+=line
                    line= reader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e:IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return termsOfUse
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result!=null) {
                val dialog = UserTermsDialog()
                dialog.setData(result.replace("#", "\n\n"))
                dialog.show(activity.fragmentManager, "userterms")
            }
        }
    }
}

