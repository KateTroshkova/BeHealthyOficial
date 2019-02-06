package be_healthy_license_2014141300.be_healthy;

import android.content.Context;
import android.os.Bundle;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InAppProduct{

    public String productId;
    public String storeName;
    public String storeDescription;
    public String price;
    public boolean isSubscription;
    public int priceAmountMicros;
    public String currencyIsoCode;

    @Override
    public String toString() {
        return productId+" "+storeName+" "+storeDescription+" "+price+" "+isSubscription+" "+priceAmountMicros+" "+currencyIsoCode;
    }

    public List<InAppProduct> getInAppPurchases(IInAppBillingService inAppBillingService, Context context, String type, String... productIds) throws Exception {
        ArrayList<String> skuList = new ArrayList<>(Arrays.asList(productIds));
        Bundle query = new Bundle();
        query.putStringArrayList("ITEM_ID_LIST", skuList);
        Bundle skuDetails = inAppBillingService.getSkuDetails(
                3, context.getPackageName(), type, query);
        ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");
        List<InAppProduct> result = new ArrayList<>();
        for (String responseItem : responseList) {
            JSONObject jsonObject = new JSONObject(responseItem);
            InAppProduct product = new InAppProduct();
            // "com.example.myapp_testing_inapp1"
            product.productId = jsonObject.getString("productId");
            // Покупка
            product.storeName = jsonObject.getString("title");
            // Детали покупки
            product.storeDescription = jsonObject.getString("description");
            // "0.99USD"
            product.price = jsonObject.getString("price");
            // "true/false"
            product.isSubscription = jsonObject.getString("type").equals("subs");
            // "990000" = цена x 1000000
            product.priceAmountMicros = Integer.parseInt(jsonObject.getString("price_amount_micros"));
            // USD
            product.currencyIsoCode = jsonObject.getString("price_currency_code");
            result.add(product);
        }
        return result;
    }
}