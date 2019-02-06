package be_healthy_license_2014141300.be_healthy.activity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.*
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.support.design.widget.BottomNavigationView
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.AvailablePurchase
import be_healthy_license_2014141300.be_healthy.UserPurchaseItems
import be_healthy_license_2014141300.be_healthy.database.XLSReader
import be_healthy_license_2014141300.be_healthy.dialog.BuyDialog
import be_healthy_license_2014141300.be_healthy.dialog.ConnectionDialog
import be_healthy_license_2014141300.be_healthy.dialog.FirstBuyDialog
import be_healthy_license_2014141300.be_healthy.dialog.UserTermsDialog
import be_healthy_license_2014141300.be_healthy.listener.NotificationReciever
import com.android.vending.billing.IInAppBillingService
import com.be_healthy_license_2014141300.be_healthy.R
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class MenuActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
        BuyDialog.OnAcceptListener, FirstBuyDialog.OnNewAcceptListener {

    private val TIME:Long=777600000

    val ITEM_ID_LIST = "ITEM_ID_LIST"
    val RESPONSE_CODE = 1001
    private var mService: IInAppBillingService? = null
    private var hasSub=false

    private val symbols = CharArray(37)
    init
    {
        var idx=0
        for (idx in 0..10)
        symbols[idx] = ('0' + idx).toChar()
        for (idx in 10..36)
        symbols[idx] = ('a' + idx - 10).toChar()
    }

    private val serviceCount = 0
    private val goNext = false

    private var receiver=object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetwork = cm.activeNetworkInfo;
            var isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting
            if(!isConnected){
                var dialog = ConnectionDialog()
                dialog.show(fragmentManager, "")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    internal var mServiceConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mService = IInAppBillingService.Stub.asInterface(service)
            hasSub=extractAllUserPurchase(getAllUserPurchase("subs")!!).size>0 ||
                    extractAllUserPurchase(getAllUserPurchase("inapp")!!).size>0
            if (!hasSub) {
               // var mAsyncTask = AvailablePurchaseAsyncTask(packageName, "subs")
               // mAsyncTask.execute()
                var mAsyncTask = AvailablePurchaseAsyncTask(packageName, "inapp")
                mAsyncTask.execute()
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
        //adviceCheckBox.isChecked=true
        if (adviceCheckBox.isChecked){
            showFragment(R.id.advice_fragment)
        }
        else{
            hideFragment(R.id.advice_fragment)
        }
    }

    fun goToTrain(view:View){
        var intent=Intent(this, TrainActivity::class.java)
        startActivity(intent)
    }

    fun goToSearch(view: View){
        var intent=Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    fun goToIMB(view:View){
        var intent=Intent(this, IMBActivity::class.java)
        startActivity(intent)
    }

    fun showSettings(view:View){
        var intent=Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun goToDiseaseSearch(view:View){
        var intent=Intent(this, DiseaseSearchActivity::class.java)
        startActivity(intent)
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
            var intent = Intent(this, NotificationReciever::class.java)
            var pIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            var am = getSystemService(ALARM_SERVICE) as AlarmManager
            am.set(AlarmManager.RTC, System.currentTimeMillis()+TIME, pIntent)
        }
    }

    private fun showUserTerms(){
        val preferences=getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        if (!preferences.getBoolean(resources.getString(R.string.param_first_time), false)){
            UserTermsTask(this).execute()
            val editor=preferences.edit()
            editor.putBoolean(resources.getString(R.string.param_first_time), true)
            editor.apply()
        }
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

    private inner class AvailablePurchaseAsyncTask(internal var packageName: String, var type:String) : AsyncTask<Void, Void, Bundle>() {
        override fun doInBackground(vararg voids: Void): Bundle? {
            val skuList = ArrayList<String>()
            skuList.add("2014141300_be_healthy_month")
            skuList.add("2014141300_be_healthy_year")
            val query = Bundle()
            query.putStringArrayList(ITEM_ID_LIST, skuList)
            var skuDetails: Bundle? = null
            try {
                skuDetails = mService!!.getSkuDetails(3, packageName, type, query)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            return skuDetails
        }

        override fun onPostExecute(skuDetails: Bundle) {
            val canPurchase = ArrayList<AvailablePurchase>()
            val response = skuDetails.getInt("RESPONSE_CODE")
            if (response == 0) {
                val responseList = skuDetails.getStringArrayList("DETAILS_LIST")
                if (responseList != null) {
                    for (thisResponse in responseList) {
                        var `object`: JSONObject? = null
                        try {
                            `object` = JSONObject(thisResponse)
                            val sku = `object`.getString("productId")
                            val price = `object`.getString("price")
                            canPurchase.add(AvailablePurchase(sku, price))
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }
                }
            }
            for (item in canPurchase) {
                Log.e("BeHealthy", item.sku + " " + item.price)
            }

            if (isBillingSupported("subs") || isBillingSupported("inapp")) {
                val preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)
                //val license_time = preferences.getLong(getString(R.string.param_license_time), 0)
                //val currentDate = Date(System.currentTimeMillis())
                //val licenseDate = Date(license_time)
                if (!hasSub) {
                var cm = this@MenuActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                var activeNetwork = cm.activeNetworkInfo;
                var isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting
                if (isConnected) {
                    //if (license_time <= 0) {
                    //    val dialog = FirstBuyDialog(this@MenuActivity)
                    //    dialog.show(fragmentManager, "")
                    //} else {
                        val hasPurches = preferences.getBoolean(getString(R.string.param_paid), false)
                        if (/**currentDate.after(licenseDate) && */!hasPurches) {
                            val dialog = BuyDialog(this@MenuActivity)
                            dialog.show(fragmentManager, "")
                        }// else {
                        //}
                    //}
                }
                }
                Log.e("BeHealthy", "supported")
            } else {
                Log.e("BeHealthy", "not supported")
            }
        }
    }

    fun isBillingSupported(type:String): Boolean {
        var response = 1
        try {
            response = mService!!.isBillingSupported(3, packageName, type)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        return if (response > 0) {
            false
        } else true
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mService != null) {
            unbindService(mServiceConn)
        }
    }

    private fun purchaseItem(sku: String, type:String) {
        val generatedPayload = getPayLoad()
        try {
            val buyIntentBundle = mService!!.getBuyIntent(3, packageName, sku, type, generatedPayload)
            val pendingIntent = buyIntentBundle.getParcelable<PendingIntent>("BUY_INTENT")
            try {
                startIntentSenderForResult(pendingIntent!!.getIntentSender(), RESPONSE_CODE, Intent(),
                        Integer.valueOf(0)!!, Integer.valueOf(0)!!, Integer.valueOf(0)!!)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }

        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    private fun getPayLoad(): String {
        val randomString = RandomString(36)
        return randomString.nextString()
    }

    inner class RandomString(length: Int) {
        private val random = Random()
        private val buf: CharArray

        init {
            if (length < 1)
                throw IllegalArgumentException("length < 1: $length")
            buf = CharArray(length)
        }

        fun nextString(): String {
            for (idx in buf.indices)
                buf[idx] = symbols[random.nextInt(symbols.size)]
            return String(buf)
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
                startActivity(Intent(this, StartActivityJava::class.java))
            }
        }
    }

    override fun OnAcceptMonth() {
        try {
            purchaseItem("2014141300_be_healthy_month", "subs")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun OnAcceptYear() {
        try {
            purchaseItem("2014141300_full_access", "inapp")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun OnNewAccept() {
        val preferences = getSharedPreferences(resources.getString(R.string.preferences), Context.MODE_PRIVATE)
        val licenseTime = 7L * 24L * 60L * 60L * 1000L + System.currentTimeMillis()
        val editor = preferences.edit()
        editor.putLong(resources.getString(R.string.param_license_time), licenseTime)
        editor.apply()
    }

    fun getAllUserPurchase(type:String): Bundle? {
        var ownedItems: Bundle? = null
        try {
            ownedItems = mService!!.getPurchases(3, packageName, type, null)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        return ownedItems
    }

    fun extractAllUserPurchase(ownedItems: Bundle): List<UserPurchaseItems> {
        val mUserItems = ArrayList<UserPurchaseItems>()
        val response = ownedItems.getInt("RESPONSE_CODE")
        if (response == 0) {
            val ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST")
            val purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST")
            val signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST")
            val continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN")
            if (purchaseDataList != null) {
                for (i in purchaseDataList.indices) {
                    val purchaseData = purchaseDataList[i]
                    assert(signatureList != null)
                    val signature = signatureList!![i]
                    assert(ownedSkus != null)
                    val sku = ownedSkus!![i]
                    val allItems = UserPurchaseItems(sku, purchaseData, signature)
                    mUserItems.add(allItems)
                }
            }
        }
        return mUserItems
    }
}
