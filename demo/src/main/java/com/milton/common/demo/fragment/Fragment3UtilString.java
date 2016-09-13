
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.StringUtils;

public class Fragment3UtilString extends Fragment2Base {

    @Override
    public String[] getItemNames() {
        return new String[]{
                "checkChinese",
                "checkNickname",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                String text = "hi,你hao ";
                showToastShort(text + " contain chinese = " + StringUtils.checkChinese(text));
                break;
            case 1:
                String text2 = "昵称 ";
                showToastShort(text2 + " is legal = " + StringUtils.checkNickname(text2));
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
