
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.StringUtil;
import com.milton.common.util.ToastUtil;

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
                String text = "hi,你hao ";
                ToastUtil.showShort(this, text + " contain chinese = " + StringUtil.checkChinese(text));
                break;
            case 1:
                String text2 = "昵称 ";
                ToastUtil.showShort(this, text2 + " is legal = " + StringUtil.checkNickname(text2));
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
