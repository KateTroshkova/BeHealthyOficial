package com.be_healthy_license_2014141300.be_healthy.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.widget.AdapterView
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
        (findViewById(R.id.back_button)).setOnClickListener { this@OptionActivity.onBackPressed() }
    }
}
