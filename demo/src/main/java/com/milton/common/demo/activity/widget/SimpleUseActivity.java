package com.milton.common.demo.activity.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;

import com.milton.common.activity.TitleBarActivity;
import com.milton.common.demo.R;
import com.milton.common.widget.ColorTrackView;


public class SimpleUseActivity extends TitleBarActivity {
    ColorTrackView mView;

    @Override
    protected String getCustomTitle() {
        return "SimpleUse";
    }

    @Override
    protected int getContent() {
        return R.layout.activity_color_track_view_simple;
    }

    @Override
    protected void initView() {
        super.initView();
        mView = (ColorTrackView) findViewById(R.id.id_changeTextColorView);
    }

    @SuppressLint("NewApi")
    public void startLeftChange(View view) {
        mView.setDirection(Gravity.LEFT);
        ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                .start();
    }

    @SuppressLint("NewApi")
    public void startRightChange(View view) {
        mView.setDirection(Gravity.RIGHT);
        ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                .start();
    }

    @SuppressLint("NewApi")
    public void startTopChange(View view) {
        mView.setDirection(Gravity.TOP);
        ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                .start();
    }

    @SuppressLint("NewApi")
    public void startBottomChange(View view) {
        mView.setDirection(Gravity.BOTTOM);
        ObjectAnimator.ofFloat(mView, "progress", 0, 1).setDuration(2000)
                .start();
    }
}
