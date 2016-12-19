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
import android.widget.Toast;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
    private static ExToast toast2;
    private static boolean useCtoast = false;

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

    public static void showToastCustom(Context context, CharSequence message, int duration) {
        if (useCtoast) {
            if (null != toast) {
                toast.hide();
            }
            toast = CToast.makeText(context, message, duration);
            toast.show();
        } else {
            if (null != toast2) {
                toast2.hide();
            }
            toast2 = ExToast.makeText(context, message, duration);
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
            text.setText(message);
            toast2.setView(layout);
            toast2.show();
        }

    }

    public static void showToastCustom2(Context context, CharSequence primary, CharSequence secondary, int duration) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_custom2, null);
        TextView textPrimary = (TextView) layout.findViewById(R.id.tv_primary);
        TextView textSecondary = (TextView) layout.findViewById(R.id.tv_secondary);
        textPrimary.setText(primary);
        textSecondary.setText(secondary);

        if (useCtoast) {
            if (null != toast) {
                toast.hide();
            }
            toast = new CToast(context);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            if (null != toast2) {
                toast2.hide();
            }
            toast2 = ExToast.makeText(context, primary, duration);
            toast2.setDuration(duration);
            toast2.setView(layout);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }

    public static void showToastNoraml(Context context, CharSequence message, int duration) {
        if (useCtoast) {
            if (null != toast) {
                toast.hide();
            }
            toast = CToast.makeText(context, message, duration, false);
            toast.show();
        } else {
            if (null != toast2) {
                toast2.hide();
            }
            toast2 = ExToast.makeText(context, message, duration);
            toast2.show();
        }
    }

    public static void showToast(Context context, int message, int duration) {
        showToast(context, context.getResources().getString(message), duration);
    }

    public static void hideToast() {
        if (useCtoast) {
            if (null != toast) {
                toast.hide();
            }
        } else {
            if (null != toast2) {
                toast2.hide();
            }
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


    public static class ExToast {
        public static final int LENGTH_ALWAYS = 0;
        public static final int LENGTH_SHORT = 2;
        public static final int LENGTH_LONG = 4;
        private static final String TAG = ExToast.class.getSimpleName();
        private Toast toast;
        private Context mContext;
        private int mDuration = LENGTH_SHORT;
        private int animations = -1;
        private boolean isShow = false;

        private Object mTN;
        private Method show;
        private Method hide;

        private Handler handler = new Handler();
        private Runnable hideRunnable = new Runnable() {
            @Override
            public void run() {
                hide();
            }
        };

        public ExToast(Context context) {
            this.mContext = context;
            if (toast == null) {
                toast = new Toast(mContext);
            }
        }

        public static ExToast makeText(Context context, CharSequence text, int duration) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            ExToast exToast = new ExToast(context);
            exToast.toast = toast;
            exToast.mDuration = duration;

            return exToast;
        }

//    public static ExToast makeText(Context context, int resId, int duration)
//            throws Resources.NotFoundException {
//        return makeText(context, context.getResources().getText(resId), duration);
//    }

        /**
         * Show the view for the specified duration.
         */
        public void show() {
            if (isShow) return;

            initTN();
            try {
                show.invoke(mTN);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            isShow = true;
            //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
            if (mDuration > LENGTH_ALWAYS) {
                handler.postDelayed(hideRunnable, mDuration);
            }
        }

        /**
         * Close the view if it's showing, or don't show it if it isn't showing yet.
         * You do not normally have to call this.  Normally view will disappear on its own
         * after the appropriate duration.
         */
        public void hide() {
            if (!isShow) return;
            try {
                hide.invoke(mTN);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            toast = null;
            isShow = false;
        }

        public View getView() {
            return toast.getView();
        }

        public void setView(View view) {
            toast.setView(view);
        }

        public int getDuration() {
            return mDuration;
        }

        /**
         * Set how long to show the view for.
         *
         * @see #LENGTH_SHORT
         * @see #LENGTH_LONG
         * @see #LENGTH_ALWAYS
         */
        public void setDuration(int duration) {
            mDuration = duration;
        }

        public void setMargin(float horizontalMargin, float verticalMargin) {
            toast.setMargin(horizontalMargin, verticalMargin);
        }

        public float getHorizontalMargin() {
            return toast.getHorizontalMargin();
        }

        public float getVerticalMargin() {
            return toast.getVerticalMargin();
        }

        public void setGravity(int gravity, int xOffset, int yOffset) {
            toast.setGravity(gravity, xOffset, yOffset);
        }

        public int getGravity() {
            return toast.getGravity();
        }

        public int getXOffset() {
            return toast.getXOffset();
        }

        public int getYOffset() {
            return toast.getYOffset();
        }

        public void setText(int resId) {
            setText(mContext.getText(resId));
        }

        public void setText(CharSequence s) {
            toast.setText(s);
        }

        public int getAnimations() {
            return animations;
        }

        public void setAnimations(int animations) {
            this.animations = animations;
        }

        private void initTN() {
            try {
                Field tnField = toast.getClass().getDeclaredField("mTN");
                tnField.setAccessible(true);
                mTN = tnField.get(toast);
                show = mTN.getClass().getMethod("show");
                hide = mTN.getClass().getMethod("hide");

                /**设置动画*/
                if (animations != -1) {
                    Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                    tnParamsField.setAccessible(true);
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
                    params.windowAnimations = animations;
                }

                /**调用tn.show()之前一定要先设置mNextView*/
                Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
                tnNextViewField.setAccessible(true);
                tnNextViewField.set(mTN, toast.getView());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
