
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.html5.Html5Activity;

public class Fragment2Html5 extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                Html5Activity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "Html5Activity",
        };
    }

}
