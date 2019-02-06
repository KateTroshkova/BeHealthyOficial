package be_healthy_license_2014141300.be_healthy.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import be_healthy_license_2014141300.be_healthy.database.XLSReader
import com.android.vending.billing.IInAppBillingService
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.MainActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import android.content.ComponentName
import android.content.Context
import android.os.IBinder
import android.content.ServiceConnection
import android.content.Context.BIND_AUTO_CREATE
import android.util.Log
import io.reactivex.internal.util.NotificationLite.isSubscription
import org.json.JSONObject
import java.util.*
import java.util.Arrays.asList
import be_healthy_license_2014141300.be_healthy.InAppProduct
import android.app.PendingIntent
import io.reactivex.internal.util.NotificationLite.isSubscription





class StartActivity : AppCompatActivity() {

    private var inAppBillingService: IInAppBillingService?=null
    private val REQUEST_CODE_BUY = 42
    val BILLING_RESPONSE_RESULT_OK = 0
    val BILLING_RESPONSE_RESULT_USER_CANCELED = 1
    val BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE = 2
    val BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3
    val BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE = 4
    val BILLING_RESPONSE_RESULT_DEVELOPER_ERROR = 5
    val BILLING_RESPONSE_RESULT_ERROR = 6
    val BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED = 7
    val BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED = 8

    val PURCHASE_STATUS_PURCHASED = 0
    val PURCHASE_STATUS_CANCELLED = 1
    val PURCHASE_STATUS_REFUNDED = 2

    var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            inAppBillingService = IInAppBillingService.Stub.asInterface(service)
            val subscriptions = InAppProduct().getInAppPurchases(inAppBillingService, this@StartActivity,"subs",
                    "2014141300_be_healthy_month")
            for(product in subscriptions){
                Log.e("BeHealthy", product.toString())
            }
            readMyPurchases("subs");
        }

        override fun onServiceDisconnected(name: ComponentName) {
            inAppBillingService = null
        }
    }

    var observer: Observer<Int> = object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(value: Int?) {
        }

        override fun onError(e: Throwable) {
        }

        override fun onComplete() {
            var intent=Intent(this@StartActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        var reader = XLSReader.getInstance()
        reader.register(observer)
        reader.read(this)
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (serviceConnection != null) {
            unbindService(serviceConnection)
        }
    }

    @SuppressLint("RestrictedApi")
    @Throws(Exception::class)
    fun purchaseProduct(product: InAppProduct) {
        val sku = product.productId
        val type = if (product.isSubscription) "subs" else "inapp";
        // сюда вы можете добавить произвольные данные
        // потом вы сможете получить их вместе с покупкой
        val developerPayload = "42"
        val buyIntentBundle = inAppBillingService!!.getBuyIntent(
                3, getPackageName(), sku, type, developerPayload)
        val pendingIntent = buyIntentBundle.getParcelable<PendingIntent>("BUY_INTENT")
        startIntentSenderForResult(pendingIntent!!.intentSender, REQUEST_CODE_BUY, Intent(), Integer.valueOf(0)!!, Integer.valueOf(0)!!,
                Integer.valueOf(0)!!, null)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_BUY) {
            val responseCode = data.getIntExtra("RESPONSE_CODE", -1)
            if (responseCode == BILLING_RESPONSE_RESULT_OK) {
                val purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA")
                val dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE")
                // можете проверить цифровую подпись
                readPurchase(purchaseData)
            } else {
                // обрабатываем ответ
            }
        }
    }

    private fun readPurchase(purchaseData:String) {
        try {
            var jsonObject = JSONObject(purchaseData);
            // ид покупки, для тестовой покупки будет null
            var orderId = jsonObject.optString("orderId");
            // "com.example.myapp"
            var packageName = jsonObject.getString("packageName");
            // "com.example.myapp_testing_inapp1"
            var productId = jsonObject.getString("productId");
            // unix-timestamp времени покупки
            var purchaseTime = jsonObject.getLong("purchaseTime");
            // PURCHASE_STATUS_PURCHASED
            // PURCHASE_STATUS_CANCELLED
            // PURCHASE_STATUS_REFUNDED
            var purchaseState = jsonObject.getInt("purchaseState");
            // "12345"
            var developerPayload = jsonObject.optString("developerPayload");
            // токен покупки, с его помощью можно получить
            // данные о покупке на сервере
            var purchaseToken = jsonObject.getString("purchaseToken");
            // далее вы обрабатываете покупку

            Log.e("BeHealthy", orderId)
        } catch (e:Exception) {
        }
    }

    @Throws(Exception::class)
    private fun readMyPurchases(type: String) {
        var continuationToken: String? = null
        do {
            val result = inAppBillingService!!.getPurchases(
                    3, getPackageName(), type, continuationToken)
            if (result.getInt("RESPONSE_CODE", -1) != 0) {
                throw Exception("Invalid response code")
            }
            val responseList = result.getStringArrayList("INAPP_PURCHASE_DATA_LIST")
            for (purchaseData in responseList!!) {
                readPurchase(purchaseData)
            }
            continuationToken = result.getString("INAPP_CONTINUATION_TOKEN")
        } while (continuationToken != null)
    }
}
