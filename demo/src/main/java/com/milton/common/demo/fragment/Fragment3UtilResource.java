
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.ResourceUtil;

public class Fragment3UtilResource extends Fragment2Base {

    @Override
    public String[] getItemNames() {
        return new String[]{
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
                showToastShort(text + "'s size is " + size + ", " + text + "'s width is " + ResourceUtil.getTextWidth(text, size));
                break;
            case 1:
                showToastShort("10dp is " + ResourceUtil.dp2px(getActivity(), 10) + "pixel in this phone");
                break;
            case 2:
//                ToastUtil.showShort(this, ResourceUtil.getTextFromAssets(this, "test.txt"));
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
