
package com.milton.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.milton.common.lib.R;

/**
 * Toast统一管理类
 */
public class ToastUtil {
    // Toast
    private static Toast toast;
    private static final boolean isDebug = DebugUtil.isDebug;
    private static int lastDuration = -1;
    private static final boolean isCustom = true;

    public static void showShort(Context context, CharSequence message) {
        showShort(context, message, false);
    }

    public static void showShort(Context context, CharSequence message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, Toast.LENGTH_SHORT);
        }
    }

    public static void showShort(Context context, int message) {
        showShort(context, message, true);
    }

    public static void showShort(Context context, int message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, Toast.LENGTH_SHORT);
        }
    }

    public static void showLong(Context context, CharSequence message) {
        showLong(context, message, false);
    }

    public static void showLong(Context context, CharSequence message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, Toast.LENGTH_LONG);
        }
    }

    public static void showLong(Context context, int message) {
        showLong(context, message, false);
    }

    public static void showLong(Context context, int message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, Toast.LENGTH_LONG);
        }
    }

    public static void show(Context context, CharSequence message, int duration) {
        show(context, message, duration, true);
    }

    public static void show(Context context, CharSequence message, int duration, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        showToast(context, message, duration);
    }

    public static void show(Context context, int message, int duration) {
        show(context, message, duration, true);
    }

    public static void show(Context context, int message, int duration, boolean showDebug) {
        if (showDebug && isDebug) {
            return;
        }
        showToast(context, message, duration);
    }

    public static void showToast(Context context, CharSequence message, int duration) {
        if (isCustom) {
            showToastCustom(context, message, duration);
        } else {
            showToastNoraml(context, message, duration);
        }
    }

    public static void showToastCustom(Context context, CharSequence message, int duration) {
        if (null != toast) {
            toast.cancel();
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        View view = layout.findViewById(R.id.layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("alinmi", " toast = " + toast);
                if (null != toast) {
                    toast.cancel();
                }
            }
        });
        text.setText(message);
        toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public static void showShort(Context context, CharSequence primary, CharSequence secondary) {
        showShort(context, primary, secondary, false);
    }

    public static void showShort(Context context, CharSequence primary, CharSequence secondary, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToastCustom2(context, primary, secondary, Toast.LENGTH_SHORT);
        }
    }

    public static void showLong(Context context, CharSequence primary, CharSequence secondary) {
        showLong(context, primary, secondary, false);
    }

    public static void showLong(Context context, CharSequence primary, CharSequence secondary, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToastCustom2(context, primary, secondary, Toast.LENGTH_LONG);
        }
    }

    public static void showToastCustom2(Context context, CharSequence primary, CharSequence secondary, int duration) {
        if (null != toast) {
            toast.cancel();
        }
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom2, null);
        TextView textPrimary = (TextView) layout.findViewById(R.id.tv_primary);
        TextView textSecondary = (TextView) layout.findViewById(R.id.tv_secondary);
        View view = layout.findViewById(R.id.layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.e("alinmi", " toast = " + toast);
                if (null != toast) {
                    toast.cancel();
                }
            }
        });
        textPrimary.setText(primary);
        textSecondary.setText(secondary);
        toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastNoraml(Context context, CharSequence message, int duration) {
        if (null != toast) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public static void showToast(Context context, int message, int duration) {
        showToast(context, context.getResources().getString(message), duration);
//        if (null == toast || lastDuration != duration) {
//            lastDuration = duration;
//            toast = Toast.makeText(context, message, duration);
//            // toast.setGravity(Gravity.CENTER, 0, 0);
//        } else {
//            toast.setText(message);
//        }
//
//        toast.show();
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
