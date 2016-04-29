
package com.milton.common.demo.activity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.milton.common.demo.R;

/** 注册验证界面activity */
public class RegisterConfirmActivity extends Activity implements OnClickListener {
    private Button btn_reg_reget, btn_title_left, btn_title_right;
    private TextView tv_reg_reget, tv_top_title;
    private MyCount mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_confirm);
        initView();
    }

    private void initView() {
        tv_reg_reget = (TextView) findViewById(R.id.tv_reg_reget);
        tv_top_title = (TextView) findViewById(R.id.tv_top_title);
        tv_top_title.setText("QQ注册");
        mc = new MyCount(10000, 1000);
        mc.start();
        btn_title_left = (Button) findViewById(R.id.btn_title_left);
        btn_title_left.setOnClickListener(this);
        btn_title_right = (Button) findViewById(R.id.btn_title_right);
        btn_title_right.setVisibility(View.GONE);
        btn_reg_reget = (Button) findViewById(R.id.btn_reg_reget);
        btn_reg_reget.setOnClickListener(this);

    }

    /** 自定义一个继承CountDownTimer的内部类，用于实现计时器的功能 */
    class MyCount extends CountDownTimer {
        /**
         * MyCount的构造方法
         * 
         * @param millisInFuture 要倒计时的时间
         * @param countDownInterval 时间间隔
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {// 在进行倒计时的时候执行的操作
            long second = millisUntilFinished / 1000;
            tv_reg_reget.setText(second + "秒后可以重新获得验证码");
            if (second == 10) {
                tv_reg_reget.setText(9 + "秒后可以重新获得验证码");
            }
            Log.i("PDA", millisUntilFinished / 1000 + "");

        }

        @Override
        public void onFinish() {// 倒计时结束后要做的事情
            // TODO Auto-generated method stub
            tv_reg_reget.setVisibility(View.GONE);
            btn_reg_reget.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_reg_reget:
                mc.start();
                tv_reg_reget.setVisibility(View.VISIBLE);
                btn_reg_reget.setVisibility(View.GONE);
                break;
            case R.id.btn_title_left:
                RegisterConfirmActivity.this.finish();
                break;
        }
    }

}
