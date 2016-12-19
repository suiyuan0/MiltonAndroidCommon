package com.milton.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.milton.common.lib.R;


/**
 * Created by milton on 16/7/12.
 */
public class SquareImageView2 extends ImageView {
    private boolean mBasedOnWidth = true;

    public SquareImageView2(Context context) {
        super(context);
    }

    public SquareImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        if (getMeasuredWidth() < getMeasuredHeight()) {
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        } else {
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}