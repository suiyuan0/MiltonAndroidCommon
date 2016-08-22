package com.milton.common.update;

import android.content.Context;
import com.milton.common.util.Constants;
import com.milton.common.util.LogUtil;

public class UpdateChecker {
    private final static String TAG = UpdateChecker.class.getSimpleName();

    public static void checkForDialog(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_DIALOG, true).execute();
        } else {
            LogUtil.e(TAG, "The arg context is null");
        }
    }


    public static void checkForNotification(Context context) {
        if (context != null) {
            new CheckUpdateTask(context, Constants.TYPE_NOTIFICATION, false).execute();
        } else {
            LogUtil.e(TAG, "The arg context is null");
        }

    }


}
