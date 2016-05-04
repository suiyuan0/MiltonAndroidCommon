
package com.milton.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 *
 * @author way
 */
public class ToastUtil {
    // Toast
    private static Toast toast;
    private static final boolean isDebug = DebugUtil.isDebug;
    private static int lastDuration = -1;

    public static void showShort(Context context, CharSequence message) {
        showShort(context, message, true);
    }

    public static void showShort(Context context, CharSequence message, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, Toast.LENGTH_SHORT);
    }

    public static void showShort(Context context, int message) {
        showShort(context, message, true);
    }

    public static void showShort(Context context, int message, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, CharSequence message) {
        showLong(context, message, true);
    }

    public static void showLong(Context context, CharSequence message, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, Toast.LENGTH_LONG);
    }

    public static void showLong(Context context, int message) {
        showLong(context, message, true);
    }

    public static void showLong(Context context, int message, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, Toast.LENGTH_LONG);
    }

    public static void show(Context context, CharSequence message, int duration) {
        show(context, message, duration, true);
    }

    public static void show(Context context, CharSequence message, int duration, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, duration);
    }

    public static void show(Context context, int message, int duration) {
        show(context, message, duration, true);
    }

    public static void show(Context context, int message, int duration, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        show2(context, message, duration);
    }

    private static void show2(Context context, CharSequence message, int duration) {
        if (null == toast || lastDuration != duration) {
            lastDuration = duration;
            toast = Toast.makeText(context, message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    private static void show2(Context context, int message, int duration) {
        if (null == toast || lastDuration != duration) {
            lastDuration = duration;
            toast = Toast.makeText(context, message, duration);
            // toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

}
