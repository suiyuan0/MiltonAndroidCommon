
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.milton.common.util.ResourceUtil;
import com.milton.common.util.ToastUtil;

public class UtilsResourceActivity extends UtilsBaseActivity {

    @Override
    public String[] getItemNames() {
        return new String[] {
                "getTextWidth",
                "dipToPX",
                "getTextFromAssets",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                String text = "hello";
                float size = 20;
                Toast.makeText(this, text + "'s size is " + size + ", " + text + "'s width is " + ResourceUtil.getTextWidth(text, size), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                ToastUtil.showShort(this, "10dp is " + ResourceUtil.dp2px(this, 10) + "pixel in this phone");
                break;
            case 2:
                ToastUtil.showShort(this, ResourceUtil.getTextFromAssets(this, "test.txt"));
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
