
package com.milton.common.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Bundle;

import com.milton.common.lib.R;

public abstract class BaseActivity extends Activity {
    protected int activityCloseEnterAnimation;

    protected int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        initOrientation();
        initAnimation();
        setView();
        initView();
        setListener();
        initData();
    }

    protected void setTheme() {
        setTheme(R.style.ActivityTheme);
    }

    public void initOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void initAnimation() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {
                android.R.attr.windowAnimationStyle
        });

        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);

        activityStyle.recycle();

        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {
                android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation
        });

        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);

        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);

        activityStyle.recycle();
    }

    /**
     * 设置布局文件
     */
    public void setView() {
    }

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initView();

    /**
     * 设置控件的监听
     */
    public void setListener() {
    }

    public void initData() {

    }

    @Override
    protected void onResume() {
        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }
}
