package be_healthy_license_2014141300.be_healthy

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.RemoteException
import com.android.vending.billing.IInAppBillingService

class Purchase {

    private val RESPONSE_CODE = 1001

    fun getAllUserPurchase(service: IInAppBillingService, packageName:String, type:String): Bundle? {
        var ownedItems: Bundle? = null
        try {
            ownedItems = service.getPurchases(3, packageName, type, null)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return ownedItems
    }

    fun hasPurchase(ownedItems: Bundle): Boolean{
        val response = ownedItems.getInt("RESPONSE_CODE")
        if (response == 0) {
            val purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST")
            if (purchaseDataList != null) {
                if (purchaseDataList.size>0){
                    return true
                }
            }
        }
        return false
    }

    fun isBillingSupported(service: IInAppBillingService, packageName:String, type:String): Boolean {
        var response = 1
        try {
            response = service.isBillingSupported(3, packageName, type)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return response <= 0
    }

    fun purchaseItem(activity: Activity, service: IInAppBillingService, packageName:String, sku: String, type:String) {
        val payload=PayLoad()
        val generatedPayload = payload.getPayLoad()
        try {
            val buyIntentBundle = service.getBuyIntent(3, packageName, sku, type, generatedPayload)
            val pendingIntent = buyIntentBundle.getParcelable<PendingIntent>("BUY_INTENT")
            try {
                activity.startIntentSenderForResult(pendingIntent!!.intentSender, RESPONSE_CODE, Intent(),
                        Integer.valueOf(0)!!, Integer.valueOf(0)!!, Integer.valueOf(0)!!)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}