package be_healthy_license_2014141300.be_healthy.activity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;
import com.be_healthy_license_2014141300.be_healthy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import be_healthy_license_2014141300.be_healthy.AvailablePurchase;
import be_healthy_license_2014141300.be_healthy.UserPurchaseItems;
import be_healthy_license_2014141300.be_healthy.database.XLSReader;
import be_healthy_license_2014141300.be_healthy.dialog.BuyDialog;
import be_healthy_license_2014141300.be_healthy.dialog.FirstBuyDialog;
import io.reactivex.disposables.Disposable;

public class StartActivityJava extends AppCompatActivity{


    io.reactivex.Observer<Integer> observer = new io.reactivex.Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
           // if (goNext) {
                Intent intent = new Intent(StartActivityJava.this, MenuActivity.class);
                startActivity(intent);
           // }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        XLSReader reader = XLSReader.Companion.getInstance();
        reader.register(observer);
        reader.read(StartActivityJava.this);
    }

}