package com.milton.jnitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = ((TextView) findViewById(R.id.text));
        tv.setText(new NdkJniUtils().getCLanguageString());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "" + new NdkJniUtils().calAAndB(15, 20), Toast.LENGTH_SHORT).show();

            }
        });
//        Toast.makeText(this, "" + new NdkJniUtils().calAAndB(15, 20), Toast.LENGTH_SHORT).show();
        new NdkJniUtils().loginServer("用户", "password1234");
        int[] a = {1, 2, 3, 4, 5};
        NdkJniUtils.intArray(a);
        String[] b = {"abs", "中国人", "银行"};
        NdkJniUtils.stringArray(b);

        new NdkJniUtils().callCcode();
        new NdkJniUtils().callCcode2();
        new NdkJniUtils().callCcode3();

    }
}
