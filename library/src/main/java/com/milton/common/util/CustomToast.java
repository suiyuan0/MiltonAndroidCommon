
package com.milton.common.util;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {
    public static void show(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLogTip(Context context) {
        CustomToast.show(context, "see log");
    }
}
