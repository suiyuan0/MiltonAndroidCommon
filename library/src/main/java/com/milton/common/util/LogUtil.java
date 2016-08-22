
package com.milton.common.util;

import android.util.Log;
import android.view.View.MeasureSpec;

public class LogUtil {
    private final static String MODE_UNSPECIFIED = "UNSPECIFIED";
    private final static String MODE_ATMOST = "AT_MOST";
    private final static String MODE_EXACTLY = "EXACTLY";
    private final static String MODE_UNKNOW = "Unknow Mode";

    /**
     * Send a error log message and log the exception.
     *
     * @param tag  Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param args the message to log.
     */
    public static void logE(String tag, Object... args) {
        if (null == args) {
            return;
        }

        String msg = "";
        if (args.length < 2) {
            msg = args[0].toString();
        } else {
            final StringBuilder sb = new StringBuilder();
            for (Object o : args) {
                sb.append(o);
            }
            msg = sb.toString();
        }

        Log.e(tag, msg, new Exception());
    }

    /**
     * Extracts the mode(String) from the supplied measure specification.
     *
     * @param measureSpec The measure specification to extract the mode from.
     * @return The mode(String) defined in the supplied measure specification
     */
    public static String getSpecMode(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        String result = "";
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = MODE_UNSPECIFIED;
                break;
            case MeasureSpec.AT_MOST:
                result = MODE_ATMOST;
                break;
            case MeasureSpec.EXACTLY:
                result = MODE_EXACTLY;
                break;
            default:
                result = MODE_UNKNOW;
                break;
        }
        return result;
    }

    public static final boolean isDebug = DebugUtil.isDebug;
    private static final String TAG = "LogUtil";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        i(msg, false);
    }

    public static void i(String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        d(msg, false);
    }

    public static void d(String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        e(msg, false);
    }

    public static void e(String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        v(msg, false);
    }

    public static void v(String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.v(TAG, msg);
        }
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        i(tag, msg, false);
    }

    public static void i(String tag, String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        d(tag, msg, false);
    }

    public static void d(String tag, String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        e(tag, msg, false);
    }

    public static void e(String tag, String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        v(tag, msg, false);
    }

    public static void v(String tag, String msg, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            Log.v(tag, msg);
        }
    }
}
