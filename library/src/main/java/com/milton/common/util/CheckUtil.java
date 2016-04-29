
package com.milton.common.util;


import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @hide
 * @deprecated [module internal use] Only use in CC, please do not use it.
 */
public class CheckUtil {

    private static final String TAG = "CheckUtil";

    private static final String MSG_IS_NOT_UITHREAD = "Current thread is not UI thread";

    private static final String MSG_IS_NOT_CONTEXT_THEME_WRAPPER = "Current context is not ContextThemeWrapper";

    // define minimum height of window
    private final static int minWindowHeight = 100;
    // define minimum width of window
    private final static int minWindowWidth = 100;

    public static boolean isUIThread(Context context) {
        if ( null == context) {
            return false;
        }

        final long uiThreadId = context.getMainLooper().getThread().getId();
        final long currentId = Thread.currentThread().getId();

        if (uiThreadId != currentId) {
            Log.e(TAG, MSG_IS_NOT_UITHREAD, new Exception(MSG_IS_NOT_UITHREAD));
            return false;
        }
        return true;
    }

    public static boolean isContextThemeWrapper(Context context) {
        if (  null == context) {
            return false;
        }

        if (context instanceof ContextThemeWrapper) {
            return true;
        } else {
            Log.e(TAG, MSG_IS_NOT_CONTEXT_THEME_WRAPPER, new Exception(
                    MSG_IS_NOT_CONTEXT_THEME_WRAPPER));
            return false;
        }
    }

    /**
     * Parameters for safety checks
     *
     * @hide
     * @deprecated [Module internal use]
     */
    public static void safeUpdateViewLayout(View view, ViewGroup.LayoutParams p,
            WindowManager windowManager) {
        checkLayoutParams(p);
        if (null == windowManager) {
            Log.e(TAG, "(WindowManager) context.getSystemService(Context.WINDOW_SERVICE) is null",
                    new Exception());
            return;
        }
        windowManager.updateViewLayout(view, p);
    }

    /**
     * Check Parameters
     *
     * @hide
     * @deprecated [Module internal use]
     */
    public static void checkLayoutParams(ViewGroup.LayoutParams p) {
        if (null == p) {
            Log.e(TAG, "ViewGroup.LayoutParams is null", new Exception());
            return;
        }
        if (p.height <= 0 || p.width < 0) {
            Log.e(TAG, "checkLayoutParams ViewGroup.LayoutParams p.height =" + p.height + ", p.width " +
                "=" + p.width);
        }
        if (p.height <= 0) {
            p.height = minWindowHeight;
        }
        if (p.width < 0) {
            p.width = minWindowWidth;
        }
    }
}
