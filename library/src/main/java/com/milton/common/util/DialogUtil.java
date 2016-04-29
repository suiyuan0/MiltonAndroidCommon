
package com.milton.common.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Gravity;

public class DialogUtil {
    public static ProgressDialog showProgress(Activity activity, String hintText) {
        Activity mActivity = null;
        if (activity.getParent() != null) {
            mActivity = activity.getParent();
            if (mActivity.getParent() != null) {
                mActivity = mActivity.getParent();
            }
        } else {
            mActivity = activity;
        }
        final Activity finalActivity = mActivity;
        ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
        window.getWindow().setGravity(Gravity.CENTER);

        window.setCancelable(false);
        return window;
    }
}
