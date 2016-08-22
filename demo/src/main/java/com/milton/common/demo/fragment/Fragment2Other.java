
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.other.SilentInstallActivity;
import com.milton.common.demo.activity.other.UpdateActivity;

public class Fragment2Other extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                SilentInstallActivity.class,
                UpdateActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "SilentInstallActivity",
                "UpdateActivity",
        };
    }

}
