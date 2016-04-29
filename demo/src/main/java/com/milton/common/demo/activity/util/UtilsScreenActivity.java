
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.milton.common.util.ScreenUtil;

public class UtilsScreenActivity extends UtilsBaseActivity {

    @Override
    public String[] getItemNames() {
        return new String[] {
                "getScreenPhysicalSize",
                "isTablet",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                Toast.makeText(this, "ScreenPhysicalSize = " + ScreenUtil.getScreenPhysicalSize(this), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "isTablet = " + ScreenUtil.isTablet(this), Toast.LENGTH_SHORT).show();
                break;
            case 2:

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
