package com.milton.common.widget.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milton.common.lib.R;


/**
 * Created by milton on 16/6/7.
 */
public class EditTextItem extends LinearLayout {
    private TextView mName;
    private EditText mEditName;
    private OnEditTextChangedListener mOnEditTextChangedListener;

    public EditTextItem(Context context) {
        super(context);
    }

    public EditTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_edit_item, this);
        setOrientation(HORIZONTAL);
    }

    public EditTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initTextName();
        initEditName();
        if (mEditName != null) {
            mEditName.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    if (mOnEditTextChangedListener != null) {
                        mOnEditTextChangedListener.afterTextChanged(s.toString());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }
        mName = (TextView) findViewById(R.id.tv_name);
        mEditName = (EditText) findViewById(R.id.et_name);

    }

    public void setTextName(int resId) {
        initTextName();
        if (mName != null) {
            mName.setText(resId);
        }
    }

    public TextView getTextName() {
        return mName;
    }

    public void setTextName(String name) {
        initTextName();
        if (mName != null) {
            mName.setText(name);
        }
    }

    public void setEditName(int resId) {
        initEditName();
        if (mEditName != null) {
            mEditName.setText(resId);
        }
    }

    public EditText getEditName() {
        return mEditName;
    }

    public void setEditName(String name) {
        initEditName();
        if (mEditName != null) {
            mEditName.setText(name);
        }
    }

    private void initEditName() {
        if (mEditName == null) {
            mEditName = (EditText) findViewById(R.id.et_name);
        }
    }

    private void initTextName() {
        if (mName == null) {
            mName = (TextView) findViewById(R.id.tv_name);
        }
    }

    public void setHint(int resId) {
        initEditName();
        if (mEditName != null) {
            mEditName.setHint(resId);
        }
    }

    public void setOnEditTextChangedListener(OnEditTextChangedListener listener) {
        mOnEditTextChangedListener = listener;
    }

    public String getContent() {
        return mEditName != null ? mEditName.getText().toString() : "";
    }

    public int getIntegerContent() {
        if (mEditName == null) {
            return -1;
        } else {
            final String res = mEditName.getText().toString();
            if ((!TextUtils.isEmpty(res)) && TextUtils.isDigitsOnly(res)) {
                return Integer.parseInt(res);
            } else {
                return -1;
            }
        }
    }

    public interface OnEditTextChangedListener {
        public void afterTextChanged(String s);
    }
}
