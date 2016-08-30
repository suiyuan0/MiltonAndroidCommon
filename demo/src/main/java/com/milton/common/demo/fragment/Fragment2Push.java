
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.push.JPushActivity;
import com.milton.common.demo.activity.push.XGPushActivity;

public class Fragment2Push extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                XGPushActivity.class,
                JPushActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "XGPushActivity",
                "JPushActivity",
        };
    }

}
