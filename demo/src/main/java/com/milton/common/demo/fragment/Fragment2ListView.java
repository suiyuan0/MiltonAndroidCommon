
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.listview.*;

public class Fragment2ListView extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[]{
                ExpandableListItemActivity.class,

        };
    }

    public String[] getItemNames() {
        return new String[]{
                "ExpandableListItem",

        };
    }

}
