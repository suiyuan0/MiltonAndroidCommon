
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.widget.LabelViewActivity;
import com.milton.common.demo.activity.widget.SlideSwitchActivity;

public class Fragment2Widget extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                LabelViewActivity.class,
                SlideSwitchActivity.class,

        };
    }

    public String[] getItemNames() {
        return new String[]{
                "LabelView",
                "SlideSwitch",
        };
    }

}
