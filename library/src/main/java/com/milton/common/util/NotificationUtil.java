
package com.milton.common.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.milton.common.lib.R;

import java.lang.reflect.Field;

public class NotificationUtil {
    private static final String TAG = "NotificationUtil";
    private static final String CONSTS_PATH = "android.service.notification.Consts";

    /**
     * 发送不重复的通知（Notification）
     * 关键点在这个requestCode，这里使用的是当前系统时间，巧妙的保证了每次都是一个新的Notification产生。
     * 
     * @param context
     * @param targetClass
     * @param title
     * @param message
     * @param extras
     */
    public static void sendNotification(Context context, Class targetClass, String title,
            String message, Bundle extras) {
        Intent mIntent = new Intent(context, targetClass);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (extras != null) {
            mIntent.putExtras(extras);
        }

        int requestCode = (int) System.currentTimeMillis();

        PendingIntent mContentIntent = PendingIntent.getActivity(context, requestCode, mIntent, 0);

        Notification mNotification = new NotificationCompat.Builder(context)
                .setContentTitle(title).setSmallIcon(R.drawable.app_icon)
                .setContentIntent(mContentIntent).setContentText(message)
                .build();
        mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotification.defaults = Notification.DEFAULT_ALL;

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(requestCode, mNotification);
    }

    /**
     * Helper method to get internal string constant value
     */
    public static String getStringConst(String constName) {
        Object value = getConst(constName);
        return value != null ? value.toString() : null;
    }

    /**
     * Helper method to get internal integer constant value
     */
    public static int getIntConst(String constName) {
        Object value = getConst(constName);
        int intValue = Integer.MIN_VALUE;
        if (value == null)
            return intValue;
        try {
            intValue = (Integer) value;
        } catch (Exception e) {
            Log.w(TAG, constName + " is not an integer", e);
        }
        return intValue;
    }

    /**
     * Helper method to get internal constant value
     */
    public static Object getConst(String constName) {
        if (constName == null) {
            Log.w(TAG, "const name is null");
            return null;
        }

        Class clz = null;
        try {
            clz = Class.forName(CONSTS_PATH);
        } catch (ClassNotFoundException e) {
            Log.w(TAG, "can't find class");
            return null;
        }

        Field field = null;
        try {
            field = clz.getField(constName);
        } catch (NoSuchFieldException e) {
            Log.w(TAG, "can't find const: " + constName);
            return null;
        }

        Object value = null;
        try {
            value = field.get(null);
        } catch (IllegalAccessException e) {
            Log.w(TAG, "can't access value: " + constName, e);
            return null;
        }

        return value;
    }

    /**
     * Helper method to enable notification features
     */
    public static Notification enableNotificationFeatures(Notification n,
            String... features) {
        final String key = getStringConst("EXTRA_HTC_FEATURES");
        if (key != null) {
            int value = 0;
            for (String feature : features) {
                int flag = getIntConst(feature);
                if (flag != Integer.MIN_VALUE)
                    value |= flag;
            }
            if (value > 0) {
                Bundle b = n.extras;
                if (b == null) {
                    b = new Bundle();
                } else {
                    value |= b.getInt(key, 0);
                }
                b.putInt(key, value);
                n.extras = b;
            }
        }
        return n;
    }

    /**
     * Helper method to control status bar glowing effect
     */
    public static void glow(Context context, String mode) {
        Log.d(TAG, "set glow mode to " + mode);
        final String action = getStringConst("STATUS_BAR_GLOW_ACTION");
        if (context != null && action != null) {
            String extraKey = getStringConst("STATUS_BAR_GLOW_MODE");
            int glowMode = getIntConst(mode);
            if (extraKey != null && glowMode != Integer.MIN_VALUE) {
                Intent intent = new Intent(action);
                intent.putExtra(extraKey, glowMode);
                context.sendBroadcast(intent);
            }
        }
    }
}
