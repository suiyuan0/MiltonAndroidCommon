
package com.milton.common.application;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        LogcatHelper.getInstance(this).start();
    }
}
