package com.milton.common.dialog;

/**
 * Created by milton on 16/5/24.
 */


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milton.common.lib.R;

public class LoadingDialog extends Dialog {
    private String content;

    public LoadingDialog(Context context, String title) {
        super(context, R.style.loadingDialogStyle);
        content = title;
    }

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        if (TextUtils.isEmpty(content)) {
            TextView tv = (TextView) this.findViewById(R.id.tv);
            tv.setVisibility(View.VISIBLE);
            tv.setText(content);
        }
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }
}