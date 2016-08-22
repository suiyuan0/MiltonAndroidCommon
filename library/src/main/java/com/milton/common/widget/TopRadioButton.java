package com.milton.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.milton.common.lib.R;

/**
 * Created by milton on 16/8/3.
 */
public class TopRadioButton extends RadioButton {
    private Drawable drawableTop;
    private int mTopWith, mTopHeight;

    public TopRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    public TopRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TopRadioButton(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs != null) {
            float scale = context.getResources().getDisplayMetrics().density;
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TopRadioButton);
            drawableTop = a.getDrawable(R.styleable.TopRadioButton_drawableTop);
            mTopWith = a.getDimensionPixelOffset(R.styleable.TopRadioButton_drawableTopWidth, 20);
            mTopHeight = a.getDimensionPixelOffset(R.styleable.TopRadioButton_drawableTopHeight, 20);
            a.recycle();
            setCompoundDrawablesWithIntrinsicBounds(null, drawableTop, null, null);
        }
    }

    // 设置Drawable定义的大小
    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (top != null) {
            top.setBounds(0, 0, mTopWith <= 0 ? top.getIntrinsicWidth() : mTopWith, mTopHeight <= 0 ? top.getMinimumHeight() : mTopHeight);
        }

        setCompoundDrawables(left, top, right, bottom);
    }

}
