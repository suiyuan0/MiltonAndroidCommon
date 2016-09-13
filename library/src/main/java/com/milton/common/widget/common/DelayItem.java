package com.milton.common.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milton.common.lib.R;


/**
 * Created by milton on 16/6/7.
 */
public class DelayItem extends LinearLayout {
    private TextView mName;
    private CheckBox mCheckBox;
    private View mDelayGroup;
    private TimeSelectorItem mTimeSelectorItem;

    public DelayItem(Context context) {
        super(context);
    }

    public DelayItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_delay_item, this);
        setOrientation(VERTICAL);
    }

    public DelayItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDelayGroup = findViewById(R.id.ll_time_selector);
        initCheckBox();
        initTextName();
        initTimeSelector();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDelayGroup.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        mTimeSelectorItem.setName(R.string.delay_time);
    }

    public void setTextName(int resId) {
        initTextName();
        if (mName != null) {
            mName.setText(resId);
        }
    }

    public TextView getTextName() {

        initTextName();
        return mName;
    }

    public void setTextName(String name) {
        initTextName();
        if (mName != null) {
            mName.setText(name);
        }
    }

    public boolean isChecked() {
        initCheckBox();
        if (mCheckBox != null) {
            return mCheckBox.isChecked();
        } else {
            return false;
        }
    }

    public void setChecked(boolean checked) {
        initCheckBox();
        if (mCheckBox != null) {
            mCheckBox.setChecked(checked);
        }
    }

    public CheckBox getCheckBox() {
        initCheckBox();
        return mCheckBox;
    }

    private void initTextName() {
        if (mName == null) {
            mName = (TextView) findViewById(R.id.tv_delay);
        }
    }

    private void initCheckBox() {
        if (mCheckBox == null) {
            mCheckBox = (CheckBox) findViewById(R.id.cb_delay);
        }
    }

    private void initTimeSelector() {
        if (mTimeSelectorItem == null) {
            mTimeSelectorItem = (TimeSelectorItem) findViewById(R.id.tsi_time_selector);
        }
    }

    public TimeSelectorItem getTimeSelectorItem() {
        initTimeSelector();
        return mTimeSelectorItem;
    }

    public void setCheckBoxClickListener(OnClickListener listener) {
        initCheckBox();
        if (mCheckBox != null) {
            mCheckBox.setOnClickListener(listener);
        }
    }
}
