
package com.example.ddd;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Toast统一管理类
 */
public class ToastUtil {
    private static final boolean isDebug = true;
    private static final boolean isCustom = true;
    private static final int LENGTH_SHORT = 2000;
    private static final int LENGTH_LONG = 5000;
    // Toast
    private static CToast toast;

    public static void showShort(Context context, CharSequence message) {
        showShort(context, message, false);
    }

    public static void showShort(Context context, CharSequence message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, LENGTH_SHORT);
        }
    }

    public static void showShort(Context context, int message) {
        showShort(context, message, true);
    }

    public static void showShort(Context context, int message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, LENGTH_SHORT);
        }
    }

    public static void showLong(Context context, CharSequence message) {
        showLong(context, message, false);
    }

    public static void showLong(Context context, CharSequence message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, LENGTH_LONG);
        }
    }

    public static void showLong(Context context, int message) {
        showLong(context, message, false);
    }

    public static void showLong(Context context, int message, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToast(context, message, LENGTH_LONG);
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
            toast.hide();
        }
        toast = CToast.makeText(context, message, duration);
        toast.show();
    }


    public static void showShort(Context context, CharSequence primary, CharSequence secondary) {
        showShort(context, primary, secondary, false);
    }

    public static void showShort(Context context, CharSequence primary, CharSequence secondary, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToastCustom2(context, primary, secondary, LENGTH_SHORT);
        }
    }

    public static void showLong(Context context, CharSequence primary, CharSequence secondary) {
        showLong(context, primary, secondary, false);
    }

    public static void showLong(Context context, CharSequence primary, CharSequence secondary, boolean alwaysShow) {
        if (alwaysShow || isDebug) {
            showToastCustom2(context, primary, secondary, LENGTH_LONG);
        }
    }

    public static void showToastCustom2(Context context, CharSequence primary, CharSequence secondary, int duration) {
        if (null != toast) {
            toast.hide();
        }
//        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom2, null);
//        TextView textPrimary = (TextView) layout.findViewById(R.id.tv_primary);
//        TextView textSecondary = (TextView) layout.findViewById(R.id.tv_secondary);
//        View view = layout.findViewById(R.id.layout);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtil.e("alinmi", " toast = " + toast);
//                if (null != toast) {
//                    toast.cancel();
//                }
//            }
//        });
//        textPrimary.setText(primary);
//        textSecondary.setText(secondary);
//        toast = new Toast(context);
//        toast.setDuration(duration);
//        toast.setView(layout);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
    }

    public static void showToastNoraml(Context context, CharSequence message, int duration) {
        if (null != toast) {
            toast.hide();
        }
        toast = CToast.makeText(context, message, duration, false);
        toast.show();
    }

    public static void showToast(Context context, int message, int duration) {
        showToast(context, context.getResources().getString(message), duration);
    }

    public static void hideToast() {
        if (null != toast) {
            toast.hide();
        }
    }

    public static class CToast {


        public static final int LENGTH_SHORT = 2000;
        public static final int LENGTH_LONG = 3500;

        private final Handler mHandler = new Handler();
        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        private int mDuration = LENGTH_SHORT;
        private int mGravity = Gravity.CENTER;
        private int mX, mY;
        private float mHorizontalMargin;
        private float mVerticalMargin;
        private View mView;
        private View mNextView;
        private WindowManager mWM;
        private final Runnable mShow = new Runnable() {
            public void run() {
                handleShow();
            }
        };
        private final Runnable mHide = new Runnable() {
            public void run() {
                handleHide();
            }
        };

        public CToast(Context context) {
            init(context);
        }

        public static CToast makeText(Context context, CharSequence message, int duration, boolean isCustom) {
            CToast result = new CToast(context);
            if (isCustom) {
                View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom, null);
                TextView text = (TextView) layout.findViewById(R.id.text);
                View view = layout.findViewById(R.id.layout);
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("alinmi121", "onTouch");
                        return false;
                    }
                });
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                LogUtil.e("alinmi", " toast = " + toast);
//                    if (null != toast) {
//                        toast.cancel();
//                    }
//                }
//            });
                text.setText(message);
                result.mNextView = layout;
            } else {
                LinearLayout mLayout = new LinearLayout(context);
                TextView tv = new TextView(context);
                tv.setText(message);
                tv.setTextColor(Color.WHITE);
                tv.setGravity(Gravity.CENTER);
                mLayout.setBackgroundResource(R.mipmap.widget_toast_bg);

                int w = context.getResources().getDisplayMetrics().widthPixels / 2;
                int h = context.getResources().getDisplayMetrics().widthPixels / 10;
                mLayout.addView(tv, w, h);
                result.mNextView = mLayout;
            }
            result.mDuration = duration;

            return result;
        }

        public static CToast makeText(Context context, CharSequence message, int duration) {
            return makeText(context, message, duration, true);
        }

        /**
         * Return the view.
         *
         * @see #setView
         */
        public View getView() {
            return mNextView;
        }

        /**
         * Set the view to show.
         *
         * @see #getView
         */
        public void setView(View view) {
            mNextView = view;
        }

        /**
         * Return the duration.
         *
         * @see #setDuration
         */
        public int getDuration() {
            return mDuration;
        }

        /**
         * Set how long to show the view for.
         *
         * @see #LENGTH_SHORT
         * @see #LENGTH_LONG
         */
        public void setDuration(int duration) {
            mDuration = duration;
        }

        /**
         * Set the margins of the view.
         *
         * @param horizontalMargin The horizontal margin, in percentage of the
         *                         container width, between the container's edges and the
         *                         notification
         * @param verticalMargin   The vertical margin, in percentage of the
         *                         container height, between the container's edges and the
         *                         notification
         */
        public void setMargin(float horizontalMargin, float verticalMargin) {
            mHorizontalMargin = horizontalMargin;
            mVerticalMargin = verticalMargin;
        }

        /**
         * Return the horizontal margin.
         */
        public float getHorizontalMargin() {
            return mHorizontalMargin;
        }

        /**
         * Return the vertical margin.
         */
        public float getVerticalMargin() {
            return mVerticalMargin;
        }

        /**
         * Set the location at which the notification should appear on the screen.
         *
         * @see android.view.Gravity
         * @see #getGravity
         */
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mGravity = gravity;
            mX = xOffset;
            mY = yOffset;
        }

        /**
         * Get the location at which the notification should appear on the screen.
         *
         * @see android.view.Gravity
         * @see #getGravity
         */
        public int getGravity() {
            return mGravity;
        }

        /**
         * Return the X offset in pixels to apply to the gravity's location.
         */
        public int getXOffset() {
            return mX;
        }

        /**
         * Return the Y offset in pixels to apply to the gravity's location.
         */
        public int getYOffset() {
            return mY;
        }

        /**
         * schedule handleShow into the right thread
         */
        public void show() {
            mHandler.post(mShow);

            if (mDuration > 0) {
                mHandler.postDelayed(mHide, mDuration);
            }
        }

        /**
         * schedule handleHide into the right thread
         */
        public void hide() {
            mHandler.post(mHide);
        }

        private void init(Context context) {
            final WindowManager.LayoutParams params = mParams;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");

            mWM = (WindowManager) context.getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE);
        }


        private void handleShow() {

            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
//            mWM = WindowManagerImpl.getDefault();
                final int gravity = mGravity;
                mParams.gravity = gravity;
                if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0f;
                }
                if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0f;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mWM.addView(mView, mParams);
            }
        }

        private void handleHide() {
            if (mView != null) {
                if (mView.getParent() != null) {
                    mWM.removeView(mView);
                }
                mView = null;
            }
        }
    }

}

