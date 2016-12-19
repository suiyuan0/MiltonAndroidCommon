package com.example.ddd;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    ExToast mExToast;
    private EditText mEditText;
    private CToast mCToast;

    public static void show(Context context, String text) {
//        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null);
//        TextView textView = (TextView) view.findViewById(R.id.tv_toast);
//        textView.setText(text);
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setText(text);
        showMyToast(toast, 1000);//时间间隔在这是1秒，这个是可以控制的

    }

    private static void showMyToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, Toast.LENGTH_LONG);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button3 = (Button) findViewById(R.id.btn3);
        button4 = (Button) findViewById(R.id.btn4);
        button5 = (Button) findViewById(R.id.btn5);
        button6 = (Button) findViewById(R.id.btn6);
        mEditText = (EditText) findViewById(R.id.timeEditText);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        findViewById(R.id.showToastBtn).setOnClickListener(this);
        findViewById(R.id.hideToastBtn).setOnClickListener(this);
    }

    //自定义Toast控件

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mExToast = ExToast.makeText(MainActivity.this, "show btn1", 1000);
                mExToast.show();
                break;
            case R.id.btn2:
                if (mExToast != null) {
                    mExToast.hide();
                }
                break;
            case R.id.btn3:
                show(MainActivity.this, "showMyToast");
                break;
            case R.id.btn4:
                ToastUtil.showShort(MainActivity.this, "ToastUtil", "showShort");
                break;
            case R.id.btn5:
                ToastUtil.showToastNoraml(getApplicationContext(), "showToastNoraml 2000", 2000);
//                ToastUtil.showShort(getApplicationContext(), "成功了show short");
                break;
            case R.id.btn6:
                ToastUtil.showLong(getApplicationContext(), "成功了 show long");
                break;
            case R.id.showToastBtn:
                String text1 = mEditText.getText().toString();
                String text = TextUtils.isEmpty(text1) ? "10000" : text1;
                ToastUtil.showToastCustom(getApplicationContext(), "成功了22", TextUtils.isDigitsOnly(text) ? Integer.parseInt(text) : 5000);

                break;
            case R.id.hideToastBtn:
                ToastUtil.hideToast();
                break;
            default:
                break;
        }
    }
}
