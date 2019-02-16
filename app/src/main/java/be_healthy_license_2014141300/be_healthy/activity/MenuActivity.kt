package be_healthy_license_2014141300.be_healthy.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.Purchase
import be_healthy_license_2014141300.be_healthy.dialog.BuyDialog
import be_healthy_license_2014141300.be_healthy.dialog.ConnectionDialog
import be_healthy_license_2014141300.be_healthy.dialog.FirstBuyDialog
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import be_healthy_license_2014141300.be_healthy.listener.NotificationReciever
import com.android.vending.billing.IInAppBillingService
import com.be_healthy_license_2014141300.be_healthy.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MenuActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        BuyDialog.OnAcceptListener, FirstBuyDialog.OnNewAcceptListener {

    private val TIME:Long=777600000
    private val RESPONSE_CODE = 1001
    private var mService: IInAppBillingService? = null
    private var hasSub=false

    private var receiver=object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting
            if(!isConnected){
                val dialog = ConnectionDialog()
                dialog.show(fragmentManager, "")
            }
        }
    }

    private var userTermObservable= Observable.create<String> { o->
        var termsOfUse=""
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(assets.open("data.txt"), "UTF-8"))
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
        o.onNext(termsOfUse)
        o.onComplete()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private var mServiceConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IInAppBillingService.Stub.asInterface(service)
            val purchase=Purchase()
            hasSub=purchase.hasPurchase(purchase.getAllUserPurchase(mService!!,
                    packageName, "subs")!!) ||
                    purchase.hasPurchase(purchase.getAllUserPurchase(mService!!,
                            packageName,"inapp")!!)
            if (!hasSub) {
               getPurchase()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_main)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        hideFragment(R.id.save_fragment)
        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE)
        val adviceCheckBox=findViewById<CheckBox>(R.id.advice_arrow)
        adviceCheckBox.isChecked=true
        showUserTerms()
        startNotification()
    }

    override fun onBackPressed() {
        //do nothing
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

    fun openSave(view:View){
        val saveCheckBox=findViewById<CheckBox>(R.id.save_arrow)
        if(view !is CheckBox) {
            saveCheckBox.isChecked = !saveCheckBox.isChecked
        }
        if (saveCheckBox.isChecked){
            showFragment(R.id.save_fragment)
        }
        else{
            hideFragment(R.id.save_fragment)
        }
    }

    fun openAdvice(view:View){
        val adviceCheckBox=findViewById<CheckBox>(R.id.advice_arrow)
        if (view !is CheckBox) {
            adviceCheckBox.isChecked = !adviceCheckBox.isChecked
        }
        if (adviceCheckBox.isChecked){
            showFragment(R.id.advice_fragment)
        }
        else{
            hideFragment(R.id.advice_fragment)
        }
    }

    fun goToTrain(view:View){
        startActivity(Intent(this, TrainActivity::class.java))
    }

    fun goToSearch(view: View){
        startActivity(Intent(this, SearchActivity::class.java))
    }

    fun goToIMB(view:View){
        startActivity(Intent(this, IMBActivity::class.java))
    }

    fun showSettings(view:View){
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    fun goToDiseaseSearch(view:View){
        startActivity(Intent(this, DiseaseSearchActivity::class.java))
    }

    private fun hideFragment(id:Int){
        val transaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentById(id)
        transaction.hide(fragment)
        transaction.commit()
    }

    private fun showFragment(id:Int){
        val transaction = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentById(id)
        transaction.show(fragment)
        transaction.commit()
    }

    private fun startNotification(){
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.contains(resources.getString(R.string.preferences))) {
            val editor=preferences.edit()
            editor.putInt(resources.getString(R.string.preferences), 0)
            editor.apply()
            val intent = Intent(this, NotificationReciever::class.java)
            val pIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            val am = getSystemService(ALARM_SERVICE) as AlarmManager
            am.set(AlarmManager.RTC, System.currentTimeMillis()+TIME, pIntent)
        }
    }

    private fun showUserTerms(){
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.getBoolean(resources.getString(R.string.param_first_time), false)){
            var disposable = userTermObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { result->
                        val dialog = UserTermsDialog()
                        dialog.setData(result.replace("#", "\n\n"))
                        dialog.show(fragmentManager, "")
                    }
            val editor=preferences.edit()
            editor.putBoolean(resources.getString(R.string.param_first_time), true)
            editor.apply()
        }
    }

    private fun getPurchase(){
        val purchase=Purchase()
        if (purchase.isBillingSupported(mService!!, packageName, "subs") ||
                purchase.isBillingSupported(mService!!, packageName, "inapp")) {
            val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
            //val license_time = preferences.getLong(getString(R.string.param_license_time), 0)
            //val currentDate = Date(System.currentTimeMillis())
            //val licenseDate = Date(license_time)
            val cm = this@MenuActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            if (isConnected) {
                //if (license_time <= 0) {
                //    val dialog = FirstBuyDialog(this@MenuActivity)
                //    dialog.show(fragmentManager, "")
                //} else {
                val hasPurches = preferences.getBoolean(getString(R.string.param_paid), false)
                if ( /**currentDate.after(licenseDate) && */ !hasPurches) {
                    val dialog = BuyDialog()
                    dialog.setListener(this@MenuActivity)
                    dialog.show(fragmentManager, "")
                }
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mService != null) {
            unbindService(mServiceConn)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putBoolean(getString(R.string.param_paid), true)
                editor.apply()
            }
            else{
                startActivity(Intent(this, StartActivity::class.java))
            }
        }
    }

    override fun onAcceptMonth() {
        try {
            Purchase().purchaseItem(this, mService!!, packageName, "2014141300_be_healthy_month", "subs")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAcceptForever() {
        try {
            Purchase().purchaseItem(this, mService!!, packageName, "2014141300_full_access", "inapp")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewAccept() {
        val preferences = getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val licenseTime = 7L * 24L * 60L * 60L * 1000L + System.currentTimeMillis()
        val editor = preferences.edit()
        editor.putLong(resources.getString(R.string.param_license_time), licenseTime)
        editor.apply()
    }
}
