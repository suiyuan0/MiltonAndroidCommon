
package com.milton.common.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.milton.common.demo.R;
import com.milton.common.demo.eventbus.BaseEvent;
import com.milton.common.util.LogUtil;
import com.milton.common.util.PreferenceConstants;
import com.milton.common.util.PreferenceUtils;
import com.milton.common.util.time.TimeUtil;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import de.greenrobot.event.EventBus;

public class SplashActivity extends FragmentActivity {
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
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        AVAnalytics.trackAppOpened(getIntent());
        setContentView(R.layout.activity_splash);
        // 判断是否从推送通知栏打开的
        XGPushClickedResult message = XGPushManager.onActivityStarted(this);
        LogUtil.e("alinmi22", "isTaskRoot() = " + isTaskRoot() + ", XGPushClickedResult = " + message);
        if (message != null) {
            // 获取自定义key-value
            String customContent = message.getCustomContent();
            if (isTaskRoot()) {
                login();
            } else {
                EventBus.getDefault().post(new BaseEvent(1, customContent, true));
                finish();
            }
        } else {
            login();
        }
//        log();
//        log2();
//        log3();

    }

    private void login() {
        String token = XGPushConfig.getToken(getApplicationContext());
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtil.e("alinmi22", "+++ register push sucess. token:" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.e("alinmi22", "+++ register push fail. token:" + data + ", errCode:" + errCode + ",msg:" + msg);
            }
        });
        mHandler = new Handler();
        String password = PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, "");
        if (!TextUtils.isEmpty(password)) {
            mHandler.postDelayed(gotoMainAct, 1000);
        } else {
            mHandler.postDelayed(gotoLoginAct, 1000);
        }
    }

    private void log() {
        Log.e("milton11", "formatShowTime");
        for (int i = 0; i < times.length; i++) {
            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime(times[i]));
        }
    }

    //    private void log2() {
//        Log.e("milton11", "formatShowTime2");
//        for (int i = 0; i < times.length; i++) {
//            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime2(times[i]));
//        }
//    }
//
//    private void log3() {
//        Log.e("milton11", "formatShowTime3");
//        for (int i = 0; i < times.length; i++) {
//            Log.e("milton11", times[i] + " -> " + TimeUtil.formatShowTime3(times[i]));
//        }
//    }

}
