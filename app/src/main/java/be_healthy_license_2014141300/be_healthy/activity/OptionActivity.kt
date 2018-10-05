package com.be_healthy_license_2014141300.be_healthy.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import be_healthy_license_2014141300.be_healthy.activity.NavigationActivity
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.adapter.OptionAdapter
import com.be_healthy_license_2014141300.be_healthy.disease.Disease

class OptionActivity : NavigationActivity() {

    @JvmField var options = mutableListOf<Disease>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        setUpToolBar()
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId=SEARCH
        //navigationView.setNavigationItemSelectedListener(this)
        //navigationView.setCheckedItem(SEARCH)
        navigationView.menu.getItem(SEARCH).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease_list))) {
            options = intent.getParcelableArrayListExtra<Disease>(resources.getString(R.string.param_disease_list))
        }
        val list=findViewById<ListView>(R.id.preview_list)
        val adapter = OptionAdapter(this, options)
        list.adapter=adapter
        list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = android.content.Intent(this@OptionActivity, DiseaseActivity::class.java)
            intent.putExtra(resources.getString(R.string.param_disease), adapter.getItem(position) as Disease)
            startActivity(intent)
        }
        (findViewById<View>(R.id.back_button)).setOnClickListener { this@OptionActivity.onBackPressed() }
    }
}
