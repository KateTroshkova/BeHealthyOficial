package be_healthy_license_2014141300.be_healthy.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.design.widget.BottomNavigationView
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

abstract class NavigationActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener /**NavigationView.OnNavigationItemSelectedListener*/ {

    protected val MAIN=0
    protected val SEARCH=1
    protected var ADDITION=2
    protected val EYE=3
    protected var IMB=4
    protected val ALARM=5
    protected val SAVE=6
    protected val SETTINGS=7

    protected val fragmentNames= listOf<String>("Главная страница", "Поиск по симптомам",/** "Измерение пульса", */
             "Дополнительно", "Тренировка для глаз", "Расчет ИМТ", "Будильник", "Сохраненные", "Настройки" )

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
            R.id.menu -> {
                val intent= Intent(this, MainActivity::class.java)
                intent.putExtra(resources.getString(R.string.param_state), ADDITION)
                startActivity(intent)
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    protected fun setUpToolBar(){
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        toolBar.title=""
        setSupportActionBar(toolbar)
    }
}