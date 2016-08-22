package com.milton.common.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by milton on 16/8/11.
 */
public class ApplicationUtil {
    public static final String TAG = ApplicationUtil.class.getSimpleName();

    public static int getCurrentVersionCode(Context context) {
        try {
            return context == null ? -1 : context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    public static String getCurrentVersionName(Context context) {
        try {
            return context == null ? "0.0.0" : context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(TAG, "PackageManager.NameNotFoundException  " + e.getMessage(), true);
            return "0.0.0";
        }
    }
}
