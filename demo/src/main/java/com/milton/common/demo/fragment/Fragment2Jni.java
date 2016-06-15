
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.jni.HelloJniActivity;

public class Fragment2Jni extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                HelloJniActivity.class,
//                SlidingMenuActivity.class,
//                SlidingQQActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "Hello Jni",
//                "SlidingMenu",
//                "Sliding4QQ",
        };
    }

}
