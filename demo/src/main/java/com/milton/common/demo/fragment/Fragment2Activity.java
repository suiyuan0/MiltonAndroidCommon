
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.activities.ChannelActivity;
import com.milton.common.demo.activity.activities.CityListActivity;
import com.milton.common.demo.activity.activities.CitySelectActivity0;
import com.milton.common.demo.activity.activities.LoginActivity;
import com.milton.common.demo.activity.activities.StatusBarActivity;

public class Fragment2Activity extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[] {
                ChannelActivity.class,
                CityListActivity.class,
                CitySelectActivity0.class,
                LoginActivity.class,
                StatusBarActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "ChannelActivity",
                "CityListActivity",
                "CitySelectActivity",
                "LoginActivity",
                "StatusBarActivity",
        };
    }

}

