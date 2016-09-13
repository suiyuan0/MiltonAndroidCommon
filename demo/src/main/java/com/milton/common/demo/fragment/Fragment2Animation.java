
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.animations.AnimationActivity;

public class Fragment2Animation extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                AnimationActivity.class,
//                SlidingMenuActivity.class,
//                SlidingQQActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "activity animations",
//                "SlidingMenu",
//                "Sliding4QQ",
        };
    }

}
