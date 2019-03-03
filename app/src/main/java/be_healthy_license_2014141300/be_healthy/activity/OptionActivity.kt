package be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import be_healthy_license_2014141300.be_healthy.adapter.OptionAdapter
import be_healthy_license_2014141300.be_healthy.disease.Disease

class OptionActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.getItem(1).isChecked = true
        if (intent.hasExtra(resources.getString(R.string.param_disease_list))) {
            val options = intent.getParcelableArrayListExtra<Disease>(resources.getString(R.string.param_disease_list))
            val symptoms = intent.getStringArrayListExtra(resources.getString(R.string.param_symptoms_for_search))!!
            val list=findViewById<ListView>(R.id.preview_list)
            val adapter = OptionAdapter(this, options, symptoms)
            list.adapter=adapter
            list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val intent = android.content.Intent(this@OptionActivity, DiseaseActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_disease), adapter.getItem(position) as Disease)
                startActivity(intent)
            }
        }
        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }
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
}
