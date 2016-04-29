
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.other.SilentInstallActivity;

public class Fragment2Other extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[] {
                SilentInstallActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "SilentInstallActivity",
        };
    }

}
