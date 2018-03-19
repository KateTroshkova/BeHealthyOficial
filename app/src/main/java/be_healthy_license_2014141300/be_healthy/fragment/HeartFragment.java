package be_healthy_license_2014141300.be_healthy.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import be_healthy_license_2014141300.be_healthy.database.DB_Operation;
import com.be_healthy_license_2014141300.be_healthy.view.HeartView;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import be_healthy_license_2014141300.be_healthy.ImageProcessing;

public class HeartFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView surfaceView;
    private Camera mCamera;
    private HeartView heartView;
    private TextView infoText;
    private AppCompatButton startButton;

    private String heartLine="";

    private boolean mPreviewRunning;
    private boolean showResult=false;
    private AtomicBoolean processing = new AtomicBoolean(false);

    private int averageIndex = 0;
    private int averageArraySize = 4;
    private int[] averageArray = new int[averageArraySize];
    private double beats = 0;
    private long startTime = 0;
    private int beatsIndex = 0;
    private int beatsArraySize = 3;
    private int[] beatsArray = new int[beatsArraySize];

    private int age=-1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart, container, false);
        surfaceView = (SurfaceView) view.findViewById(R.id.preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        startButton=(AppCompatButton)view.findViewById(R.id.fab);
        startButton.setOnClickListener(this);
        infoText=(TextView)view.findViewById(R.id.info);
        heartView=(HeartView)view.findViewById(R.id.heart_view);
        startButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, startButton.getTextSize()*((CustomApplication)getActivity().getApplication()).getSize()*0.6f);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (hasAge()){
            if (hasFlash()) {
                start();
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
        String result=getActivity().getResources().getString(R.string.result_info)+heartBeat+"\n";
        switch (res){
            case -1:{
                result=result+getActivity().getResources().getString(R.string.too_low);
                break;
            }
            case 1:{
                result=result+getActivity().getResources().getString(R.string.too_high);
                break;
            }
            case 0:{
                result=result+getActivity().getResources().getString(R.string.norm);
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

    private void blockStartButton(){
        startButton.setEnabled(false);
        startButton.setVisibility(View.INVISIBLE);
    }

    private void activateStartButton(){
        startButton.setEnabled(true);
        startButton.setVisibility(View.VISIBLE);
    }

    private void start(){
        blockStartButton();
        infoText.setText("");
        showResult=true;
        setFlash(true);
        update();
    }

    private void update(){
        averageIndex = 0;
        averageArraySize = 4;
        averageArray = new int[averageArraySize];
        processing = new AtomicBoolean(false);
        beats = 0;
        startTime = 0;
        beatsIndex = 0;
        beatsArraySize = 3;
        beatsArray = new int[beatsArraySize];
        heartLine="";
    }

    private void stop(int beats){
        showResult=false;
        setFlash(false);
        activateStartButton();
        calculateResult(beats);
    }

    private void setFlash(boolean needFlash){
        if (hasFlash()) {
            Camera.Parameters p = mCamera.getParameters();
            if (needFlash) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }
            try {
                mCamera.setParameters(p);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void release(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            surfaceView.getHolder().removeCallback(this);
            mCamera.release();
        }
        surfaceView.getHolder().removeCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        Camera.Parameters parameters = mCamera.getParameters();
        if(parameters.getMaxExposureCompensation() != parameters.getMinExposureCompensation()){
            parameters.setExposureCompensation(0);
        }
        mCamera.setParameters(parameters);
        mCamera.setPreviewCallback(previewCallback);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
        if (mPreviewRunning) {
            mCamera.stopPreview();
        }
        if (showResult) {
            setFlash(true);
        }
        try {
            mCamera.setPreviewDisplay(arg0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
        mPreviewRunning = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mPreviewRunning = false;
        release();
    }

    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);//все правильно, именно в таком порядке
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int anAverageArray : averageArray) {
                if (anAverageArray > 0) {
                    averageArrayAvg += anAverageArray;
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            if (imgAvg < rollingAverage) {
                beats++;
                heartLine+="1";
            } else {
                heartLine+="0";
            }
            if (showResult) {
                heartView.update(heartLine);
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                beats/=3;
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int aBeatsArray : beatsArray) {
                    if (aBeatsArray > 0) {
                        beatsArrayAvg += aBeatsArray;
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                if (showResult) {
                    stop(beatsAvg);
                   updateHistory(beatsAvg);
                }
                heartLine="";
                startTime = System.currentTimeMillis();
                beats = 0;
            }
            processing.set(false);
        }

    };

    private void updateHistory(int beats){
        DateFormat df = DateFormat.getDateInstance();
        String date = df.format(Calendar.getInstance().getTime());
        new DB_Operation(getActivity()).saveResult(new HeartBeat(date, heartLine, beats));
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(getActivity().getResources().getString(R.string.action_update_history)));
    }
}
