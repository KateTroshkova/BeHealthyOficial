package com.be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.activity.EducationActivity
import be_healthy_license_2014141300.be_healthy.activity.NavigationActivity
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import be_healthy_license_2014141300.be_healthy.fragment.AdditionalSettingsFragment
import be_healthy_license_2014141300.be_healthy.fragment.HeartFragment
import be_healthy_license_2014141300.be_healthy.fragment.IMBFragment
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.fragment.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class MainActivity : NavigationActivity(), UserTermsDialog.OnInstructionListener, AdditionalSettingsFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction(id:Int) {
        setFragment(fragments[id])
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        toolBar.title=fragmentNames[id]
    }

    private lateinit var navigationView: BottomNavigationView

    private var currentState=0
    private var needInstruction=false

    private var fragments = hashMapOf(MAIN to MainFragment.getInstance(),
            SEARCH to SearchFragment.getInstance(),
            EYE to EyeFragment.getInstance(),
            IMB to IMBFragment(),
            SAVE to SavedFragment.getInstance(),
            ALARM to AlarmFragment.getInstance(),
            SETTINGS to SettingsFragment.getInstance(),
            ADDITION to AdditionalSettingsFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, "ca-app-pub-5054095397379666~4357348574")
        setUpToolBar()
        navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        if(intent.hasExtra(resources.getString(R.string.param_state))){
            currentState=intent.getIntExtra(resources.getString(R.string.param_state), 0)
            fragments[SEARCH]=SearchFragment()
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

    override fun onPause() {
        super.onPause()
        (fragments[EYE] as EyeFragment).stop()
        (fragments[ALARM] as AlarmFragment).stop()
        (fragments[SAVE] as SavedFragment).stop()
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
        (fragments[SAVE] as SavedFragment).stop()
        when (item.itemId) {
            R.id.main -> {
                setFragment(fragments[MAIN])
                setBackground(MAIN)
            }
            R.id.search -> {
                setFragment(fragments[SEARCH])
                setBackground(SEARCH)
            }
            R.id.menu->{
                setFragment(fragments[ADDITION])
                setBackground(ADDITION)
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onShowInstruction() {
        if (needInstruction){
            startActivity(Intent(this, EducationActivity::class.java))
            needInstruction=false
        }
    }

    private fun setBackground(checked:Int){
        currentState=checked
        if (navigationView.menu!=null) {
            for (i in (MAIN..ADDITION)) {
                navigationView.menu.getItem(i).isChecked = false
            }
            navigationView.menu.getItem(checked).isChecked = true
        }
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        toolBar.title=fragmentNames[checked]
    }

    private fun setFragment(fragment: Fragment?){
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (fragmentTransaction!=null) {
            fragmentTransaction.replace(R.id.content, fragment)
            fragmentTransaction.commit()
        }
    }

    private inner class UserTermsTask(var activity:Activity): AsyncTask<Void, Void, String>() {

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
                needInstruction=true
            }
        }
    }
}

