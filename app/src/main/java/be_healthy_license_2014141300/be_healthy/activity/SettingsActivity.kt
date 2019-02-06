package be_healthy_license_2014141300.be_healthy.activity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SettingsActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_settings)
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
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

    fun share(view: View){
        ShareManager(this).saveUrl()
        Toast.makeText(this, resources.getString(R.string.shared_info), Toast.LENGTH_SHORT).show()
    }

    fun showUserTerms(view:View){
        UserTermsTask(this).execute()
    }

    private inner class UserTermsTask(var activity: Activity): AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg p0: Void?): String {
            var termsOfUse=""
            var reader: BufferedReader? = null
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
                    } catch (e: IOException) {
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
