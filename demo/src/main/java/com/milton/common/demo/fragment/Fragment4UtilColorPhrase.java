
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milton.common.demo.R;
import com.milton.common.util.phrase.ColorPhrase;

public class Fragment4UtilColorPhrase extends BaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment4_util_colorphrase;
    }

    @Override
    protected void initView() {
        super.initView();
        final EditText editText = (EditText) rootView.findViewById(R.id.editText1);
        final TextView textView = (TextView) rootView.findViewById(R.id.textView1);
        Button button = (Button) rootView.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pattern = editText.getText().toString();
                CharSequence chars = ColorPhrase.from(pattern).withSeparator("{}").innerColor(0xFFE6454A).outerColor(0xFF00FF00).format();
                textView.setText(chars);
            }
        });

    }


}
