
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.multimedia.CameraActivity;

public class Fragment2MultiMedia extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                CameraActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "CameraActivity",
        };
    }

}
