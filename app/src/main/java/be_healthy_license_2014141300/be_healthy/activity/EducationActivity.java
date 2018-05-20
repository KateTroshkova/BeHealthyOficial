package be_healthy_license_2014141300.be_healthy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.be_healthy_license_2014141300.be_healthy.R;

public class EducationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
    }

    public void click(View view){
        finish();
    }
}
