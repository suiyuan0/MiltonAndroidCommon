
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.sliding.DrawerLayoutActivity;
import com.milton.common.demo.activity.sliding.SlidingMenuActivity;

public class Fragment2Sliding extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[] {
                DrawerLayoutActivity.class,
                SlidingMenuActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "DrawerLayout",
                "SlidingMenu",
        };
    }

}
