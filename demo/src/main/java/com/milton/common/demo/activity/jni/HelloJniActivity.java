
package com.milton.common.demo.activity.jni;

import android.widget.TextView;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;

public class HelloJniActivity extends BaseActivity {
    private TextView mTextView;


    @Override
    public void initView() {
        setContentView(R.layout.activity_jni_hello);
        mTextView = (TextView) findViewById(R.id.tv_text);
        mTextView.setText(hellojni());
    }

    public native String hellojni();

    static {
        System.loadLibrary("jni");
    }
}
