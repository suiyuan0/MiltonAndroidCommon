
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.CustomToast;
import com.milton.common.util.NetUtil;
import com.milton.common.util.NotificationUtil;

public class UtilsNotificationActivity extends UtilsBaseActivity {

    @Override
    public String[] getItemNames() {
        return new String[] {
                "sendNotification",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                NotificationUtil.sendNotification(this, NotificationActivity.class, "title", "message", null);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;

            default:
                break;
        }
    }
}
