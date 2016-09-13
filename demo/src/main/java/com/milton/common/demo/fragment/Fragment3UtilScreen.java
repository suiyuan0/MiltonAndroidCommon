
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.ScreenUtil;

public class Fragment3UtilScreen extends Fragment2Base {

    @Override
    public String[] getItemNames() {
        return new String[]{
                "getScreenPhysicalSize",
                "isTablet",
                "showScreenSize",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                showToastLong("ScreenPhysicalSize = " + ScreenUtil.getScreenPhysicalSize(getActivity()));
                break;
            case 1:
                showToastLong("isTablet = " + ScreenUtil.isTablet(getActivity()));
                break;
            case 2:
                ScreenUtil.showScreenSize(getActivity());
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;

            default:
                break;
        }
    }
}
