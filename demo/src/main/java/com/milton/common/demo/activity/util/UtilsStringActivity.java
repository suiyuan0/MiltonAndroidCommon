
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.CustomToast;
import com.milton.common.util.StringUtil;

public class UtilsStringActivity extends UtilsBaseActivity {

    @Override
    public String[] getItemNames() {
        return new String[] {
                "checkChinese",
                "checkNickname",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                String text = "hi,��hao ";
                CustomToast.show(this, text + " contain chinese = " + StringUtil.checkChinese(text));
                break;
            case 1:
                String text2 = "��� ";
                CustomToast.show(this, text2 + " is legal = " + StringUtil.checkNickname(text2));
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
