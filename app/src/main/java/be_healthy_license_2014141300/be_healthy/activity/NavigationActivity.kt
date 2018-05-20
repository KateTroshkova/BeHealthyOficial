package be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.ShareManager
import com.be_healthy_license_2014141300.be_healthy.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

abstract class NavigationActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    protected val MAIN=0
    protected val SEARCH=1
    //protected val HEART=2
    protected val EYE=2
    protected var IMB=3
    protected val SAVE=4
    protected val ALARM=5
    protected val SETTINGS=6

    protected val fragmentNames= listOf<String>("Главная страница", "Поиск симптомов",/** "Измерение пульса", */
            "Тренировка для глаз", "Расчет ИМТ", "Сохраненные", "Будильник", "Настройки" )

    protected val CAMERA_PERMISSION=0

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
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
            /**R.id.heart -> {
                checkCameraPermission()
            }*/
            R.id.eye -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), EYE)
                startActivity(intent)
            }
            R.id.imb -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), IMB)
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

    /**protected open fun checkCameraPermission(){
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
    }*/

    protected fun setUpToolBar(){
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        toolBar.title=""
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }
}