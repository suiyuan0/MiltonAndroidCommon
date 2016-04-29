
package com.milton.common.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.milton.common.demo.R;
import com.milton.common.util.PreferenceConstants;
import com.milton.common.util.PreferenceUtils;
import com.milton.common.util.TimeUtil;

public class SplashActivity extends FragmentActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
//        log();
//        log2();
//        log3();
        mHandler = new Handler();
        String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
        if (!TextUtils.isEmpty(password)) {
            mHandler.postDelayed(gotoMainAct, 1000);
        } else {
            mHandler.postDelayed(gotoLoginAct, 1000);
        }
    }

    Runnable gotoLoginAct = new Runnable() {

        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    };

    Runnable gotoMainAct = new Runnable() {

        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };
    String times[] = {
            "2015-12-31 13:07:07",
            "2015-12-31 00:00:00",
            "2015-12-31 00:00:01",
            "2015-12-31 00:01:02",
            "2015-12-30 00:00:01",
            "2015-12-30 23:59:59",
            "2015-12-29 08:08:08",
            "2015-12-28 08:08:08",
            "2015-12-27 08:08:08",
            "2015-12-26 08:08:08",
            "2015-12-25 08:08:08",
            "2015-12-24 08:08:08",
            "2015-12-23 08:08:08"
    };
    private void log() {
        Log.e("milton11", "formatShowTime");
        for (int i = 0; i < times.length; i++) {
            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime(times[i]));
        }
    }

    private void log2() {
        Log.e("milton11", "formatShowTime2");
        for (int i = 0; i < times.length; i++) {
            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime2(times[i]));
        }
    }

    private void log3() {
        Log.e("milton11", "formatShowTime3");
        for (int i = 0; i < times.length; i++) {
            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime3(times[i]));
        }
    }
}
