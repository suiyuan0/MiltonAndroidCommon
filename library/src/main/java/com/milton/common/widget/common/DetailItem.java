package com.milton.common.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milton.common.lib.R;


/**
 * Created by milton on 16/6/7.
 */
public class DetailItem extends RelativeLayout {
    private TextView mContent;

    public DetailItem(Context context) {
        super(context);
    }

    public DetailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_detail_item, this);
    }

    public DetailItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (TextView) findViewById(R.id.tv_content);
    }

    public TextView getContent() {
        return mContent;
    }

    public void setContent(int resId) {
        if (mContent == null) {
            mContent = (TextView) findViewById(R.id.tv_content);
        }
        if (mContent != null) {
            mContent.setText(resId);
        }
    }
}
