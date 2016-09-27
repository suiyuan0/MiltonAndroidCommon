package com.example.gradledemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.milton.common.util.ApplicationUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.tv_content)).setText(BuildConfig.API_URL);
//        ((TextView) findViewById(R.id.tv_app_id)).setText(BuildConfig.API_URL);
        ((TextView) findViewById(R.id.tv_version_name)).setText(ApplicationUtil.getCurrentVersionName(this));
        try {
            ((TextView) findViewById(R.id.tv_package_info)).setText(getPackageManager().getPackageInfo(getPackageName(), 0).toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Button btn = (Button) findViewById(R.id.btn);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    new Intent()
                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                }
            });
        }

    }
}
