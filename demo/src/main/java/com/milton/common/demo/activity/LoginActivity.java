
package com.milton.common.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.milton.common.activity.BaseActivity;
import com.milton.common.other.MD5Encoder;
import com.milton.common.util.PreferenceConstants;
import com.milton.common.util.PreferenceUtils;

public class LoginActivity extends BaseActivity {
    Button mButton;

    @Override
    public void setView() {
        // TODO Auto-generated method stub
        mButton = new Button(this);
        setContentView(mButton);
    }

    @Override
    public void initView() {
        mButton.setText("Login");
    }

    @Override
    public void setListener() {
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save2Preferences();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void save2Preferences() {
        PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, MD5Encoder.encode("password"));
    }

}
