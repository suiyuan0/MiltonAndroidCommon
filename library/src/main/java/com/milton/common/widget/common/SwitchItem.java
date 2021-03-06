package com.milton.common.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milton.common.lib.R;


/**
 * Created by milton on 16/6/7.
 */
public class SwitchItem extends RelativeLayout {
    private TextView mName;
    private CheckBox mCheckBox;

    public SwitchItem(Context context) {
        super(context);
    }

    public SwitchItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_switch_item, this);
    }

    public SwitchItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initTextName();
        initCheckBox();
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
            mName = (TextView) findViewById(R.id.tv_name);
        }
    }

    private void initCheckBox() {
        if (mCheckBox == null) {
            mCheckBox = (CheckBox) findViewById(R.id.cb_use);
        }
    }

    public void setCheckBoxClickListener(OnClickListener listener) {
        initCheckBox();
        if (mCheckBox != null) {
            mCheckBox.setOnClickListener(listener);
        }
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        initCheckBox();
        if (mCheckBox != null) {
            mCheckBox.setOnCheckedChangeListener(listener);
        }
    }
}
