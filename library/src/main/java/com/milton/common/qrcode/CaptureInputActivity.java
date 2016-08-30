package com.milton.common.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.milton.common.lib.R;


/**
 * Created by Junyu.zhu@Honeywell.com on 16/7/18.
 */
public class CaptureInputActivity extends Activity implements TextView.OnEditorActionListener {


    private EditText mEditText;
    private ImageView mImageView;
    public static final String QRCODE_ONELINE = "qrcode_oneline";
    public static final int RESULT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_manual_input);
        initView();
    }

    private void setTheme() {
        setTheme(R.style.CompatBaseTheme);
    }

    private void initView() {
        mEditText = (EditText) findViewById(R.id.et_input_qr);
        mEditText.setOnEditorActionListener(this);
        mImageView = (ImageView) findViewById(R.id.iv_left);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureInputActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (TextUtils.isEmpty(mEditText.getText())) {

        } else {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Intent mIntent = getIntent();
                String code = mEditText.getText().toString();
                mIntent.putExtra(QRCODE_ONELINE, code);
                Log.e("TTTTTTT::", code + "");
                setResult(RESULT_CODE, mIntent);
                finish();
            }
        }

        return true;
    }
}
