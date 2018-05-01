package be_healthy_license_2014141300.be_healthy.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.be_healthy_license_2014141300.be_healthy.R;
import com.be_healthy_license_2014141300.be_healthy.view.HeartView;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import be_healthy_license_2014141300.be_healthy.ImageProcessing;

public class HeartRateMeasureActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private SurfaceView surfaceView;
    private HeartView heartView;
    private Camera mCamera;
    private AtomicBoolean processing = new AtomicBoolean(false);
    private String heartLine="";

    private boolean hasResult=false;

    private boolean mPreviewRunning;

    private int averageIndex = 0;
    private int averageArraySize = 4;
    private int[] averageArray = new int[averageArraySize];
    private double beats = 0;
    private long startTime = 0;
    private int beatsIndex = 0;
    private int beatsArraySize = 3;
    private int[] beatsArray = new int[beatsArraySize];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_measure2);
 //       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitle("Измерить пульс");
        surfaceView = (SurfaceView) findViewById(R.id.preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(surfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        heartView=(HeartView)findViewById(R.id.heart_view);
    }

    private void setFlash(boolean needFlash){
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
        //if (showResult) {
            setFlash(true);
        //}
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
            heartView.update(heartLine);

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
                if (!hasResult) {
                    hasResult=true;
                    int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                    Intent intent = new Intent(getResources().getString(R.string.action_measure_done));
                    intent.putExtra(getResources().getString(R.string.param_heartbeat), beatsAvg);
                    intent.putExtra(getResources().getString(R.string.param_heartline), heartLine);
                    LocalBroadcastManager.getInstance(HeartRateMeasureActivity.this).sendBroadcast(intent);
                    finish();
                }
            }
            processing.set(false);
        }

    };

}
