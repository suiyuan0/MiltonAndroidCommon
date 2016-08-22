package com.milton.common.demo.activity.other;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;
import com.milton.common.update.UpdateChecker;
import com.milton.common.util.ApplicationUtil;

public class UpdateActivity extends BaseActivity {

    @Override
    public void initView() {
        setContentView(R.layout.activity_update);
        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);

        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateChecker.checkForDialog(UpdateActivity.this);
            }
        });
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UpdateChecker.checkForNotification(UpdateActivity.this);
            }
        });


        TextView textView = (TextView) findViewById(R.id.textView1);

        textView.setText("当前版本信息: versionName = " + ApplicationUtil.getCurrentVersionName(this) + " versionCode = " + ApplicationUtil.getCurrentVersionCode(this));
    }

}
