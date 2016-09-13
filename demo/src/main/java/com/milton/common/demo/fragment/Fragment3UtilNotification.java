
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.AdapterView;

import com.milton.common.demo.activity.notification.NotificationActivity;
import com.milton.common.util.NotificationUtil;

public class Fragment3UtilNotification extends Fragment2Base {

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
                NotificationUtil.sendNotification(getActivity(), NotificationActivity.class, "title", "message", null);
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
