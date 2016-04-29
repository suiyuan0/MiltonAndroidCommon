
package com.milton.common.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.milton.common.activity.BaseActivity;
import com.milton.common.other.MD5Encoder;
import com.milton.common.util.PreferenceConstants;
import com.milton.common.util.PreferenceUtils;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = new Button(this);
        btn.setText("Login");
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save2Preferences();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        setContentView(btn);
    }

    private void save2Preferences() {
        PreferenceUtils.setPrefString(this, PreferenceConstants.PASSWORD, MD5Encoder.encode("password"));
    }

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

}
