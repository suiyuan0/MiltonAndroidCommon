package com.milton.common.demo.activity.sliding;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.milton.common.activity.BaseFragmentActivity;
import com.nineoldandroids.view.ViewHelper;
import com.milton.common.demo.R;

public class SlidingQQActivity extends BaseFragmentActivity {

    private DrawerLayout mDrawerLayout;
    private Button mLeft;
    private Button mRight;
    private CheckBox mScale;
    private boolean bNeedScale;

    @Override
    protected void setTheme() {
        setTheme(R.style.SlidingActivityTheme);
    }

    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sliding_qq);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
//                Gravity.RIGHT);
        mLeft = (Button) findViewById(R.id.btn_title_left);
        mRight = (Button) findViewById(R.id.btn_title_right);
        mScale = (CheckBox) findViewById(R.id.cb_scale);


    }

    public void OpenRightMenu() {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
    }

    public void OpenLeftMenu() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.LEFT);
    }

    public void setListener() {
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenLeftMenu();
            }
        });
        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenRightMenu();
            }
        });
        mScale.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bNeedScale = isChecked;
            }
        });
        mDrawerLayout.setDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("START")) {

                    float leftScale = 1 - 0.3f * scale;
                    if (bNeedScale) {
                        ViewHelper.setScaleX(mMenu, leftScale);
                        ViewHelper.setScaleY(mMenu, leftScale);
                    }
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    if (bNeedScale) {
                        ViewHelper.setScaleX(mContent, rightScale);
                        ViewHelper.setScaleY(mContent, rightScale);
                    }
                } else {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    if (bNeedScale) {
                        ViewHelper.setScaleX(mContent, rightScale);
                        ViewHelper.setScaleY(mContent, rightScale);
                    }
                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(
                        DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }


}
