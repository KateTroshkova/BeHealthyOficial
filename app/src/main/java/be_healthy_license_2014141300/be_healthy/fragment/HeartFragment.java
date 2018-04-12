package be_healthy_license_2014141300.be_healthy.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.be_healthy_license_2014141300.be_healthy.CustomApplication;
import com.be_healthy_license_2014141300.be_healthy.HeartBeat;
import com.be_healthy_license_2014141300.be_healthy.R;

import be_healthy_license_2014141300.be_healthy.activity.HeartRateMeasureActivity;
import be_healthy_license_2014141300.be_healthy.database.DB_Operation;
import com.be_healthy_license_2014141300.be_healthy.view.HeartView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import be_healthy_license_2014141300.be_healthy.ImageProcessing;

public class HeartFragment extends Fragment implements View.OnClickListener {

    private TextView infoText;
    private AppCompatButton startButton;
    private int age=-1;
    private Activity existActivity;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int beats=intent.getIntExtra(existActivity.getResources().getString(R.string.param_heartbeat), 0);
            String heartLine=intent.getStringExtra(existActivity.getResources().getString(R.string.param_heartline));
            calculateResult(beats);
            updateHistory(beats, heartLine);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart, container, false);
        existActivity=getActivity();
        startButton=(AppCompatButton)view.findViewById(R.id.fab);
        startButton.setOnClickListener(this);
        infoText=(TextView)view.findViewById(R.id.info);
        startButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, startButton.getTextSize()*((CustomApplication)getActivity().getApplication()).getSize()*0.6f);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(getResources().getString(R.string.action_measure_done)));
        return view;
    }

    @Override
    public void onClick(View view) {
        if (hasAge()){
            if (hasFlash()) {
                getActivity().startActivity(new Intent(getActivity(), HeartRateMeasureActivity.class));
            }
            else{
                infoText.setText(getActivity().getResources().getString(R.string.no_flash_error));
            }
        }
        else{
            infoText.setText(getActivity().getResources().getString(R.string.error_age));
        }
    }

    private boolean hasAge(){
        SharedPreferences preferences=getActivity().getSharedPreferences(getActivity().getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        age=preferences.getInt(getActivity().getResources().getString(R.string.param_age), -1);
        return age!=-1;
    }

    private boolean hasFlash(){
        return getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void calculateResult(int heartBeat){
        int result=0;
        if (age<1){
            result=norm(heartBeat, 110, 170);
        }
        if (age>=1 && age<=3){
            result=norm(heartBeat, 94, 155);
        }
        if (age>=4 && age<=6){
            result=norm(heartBeat, 86, 126);
        }
        if (age>6 && age<=8){
            result=norm(heartBeat, 78, 118);
        }
        if (age>8 && age<=10){
            result=norm(heartBeat, 68, 108);
        }
        if (age>10 && age<=12){
            result=norm(heartBeat, 60, 100);
        }
        if (age>12 && age<=15){
            result=norm(heartBeat, 55, 95);
        }
        if (age>15 && age<=50){
            result=norm(heartBeat, 60, 80);
        }
        if (age>50 && age<=60){
            result=norm(heartBeat, 65, 85);
        }
        if (age>60){
            result=norm(heartBeat, 70, 90);
        }
        showResult(heartBeat, result);
    }

    private void showResult(int heartBeat, int res){
        String result=existActivity.getResources().getString(R.string.result_info)+heartBeat+"\n";
        switch (res){
            case -1:{
                result=result+existActivity.getResources().getString(R.string.too_low);
                break;
            }
            case 1:{
                result=result+existActivity.getResources().getString(R.string.too_high);
                break;
            }
            case 0:{
                result=result+existActivity.getResources().getString(R.string.norm);
                break;
            }
            default:{
                break;
            }
        }
        infoText.setText(result);
    }

    private int norm(int heartBeat, int min, int max){
        if (heartBeat<min){
            return -1;
        }
        if (heartBeat>max){
            return 1;
        }
        return 0;
    }

    private void updateHistory(int beats, String heartLine){
        DateFormat df = DateFormat.getDateInstance();
        String date = df.format(Calendar.getInstance().getTime());
        new DB_Operation(existActivity).saveResult(new HeartBeat(date, heartLine, beats));
        LocalBroadcastManager.getInstance(existActivity).sendBroadcast(new Intent(existActivity.getResources().getString(R.string.action_update_history)));
    }

    public void stop(){
        if (existActivity!=null && receiver!=null) {
            LocalBroadcastManager.getInstance(existActivity).unregisterReceiver(receiver);
        }
    }
}
