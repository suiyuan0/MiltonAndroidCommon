package com.milton.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milton.common.lib.R;
import com.milton.common.util.LogUtil;

/**
 * Created by milton on 16/9/22.
 */
public class TitleLinearLayout extends LinearLayout {
    private final String TAG = TitleLinearLayout.class.getSimpleName();

    public TitleLinearLayout(Context context) {
        this(context, null);
    }

    public TitleLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TitleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.e(TAG, "TitleLinearLayout getChildCount() = " + getChildCount());
        setOrientation(VERTICAL);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleViewGroup, defStyleAttr, 0);
        String title = ta.getString(R.styleable.TitleViewGroup_title);
        TextView tv = new TextView(getContext());
        tv.setText(title);
        tv.setBackgroundColor(Color.GRAY);
        tv.setTextColor(Color.WHITE);
        addView(tv);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LogUtil.e(TAG, "onFinishInflate getChildCount() = " + getChildCount());
//        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        LogUtil.e(TAG, "onLayout");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        LogUtil.e(TAG, "dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.e(TAG, "onDraw");
    }
    //    public TitleLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

}
