
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.util.UtilsContextActivity;
import com.milton.common.demo.activity.util.UtilsNetActivity;
import com.milton.common.demo.activity.util.UtilsNotificationActivity;
import com.milton.common.demo.activity.util.UtilsResourceActivity;
import com.milton.common.demo.activity.util.UtilsScreenActivity;
import com.milton.common.demo.activity.util.UtilsStringActivity;

public class Fragment2Util extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[] {
                UtilsScreenActivity.class,
                UtilsContextActivity.class,
                UtilsResourceActivity.class,
                UtilsStringActivity.class,
                UtilsNetActivity.class,
                UtilsNotificationActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "ScreenUtil",
                "ContextUtil",
                "ResourceUtil",
                "StringUtil",
                "NetUtil",
                "NotificationUtil",
        };
    }

}
