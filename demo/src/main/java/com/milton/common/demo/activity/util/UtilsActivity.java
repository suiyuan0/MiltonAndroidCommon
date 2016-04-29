
package com.milton.common.demo.activity.util;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

public class UtilsActivity extends UtilsBaseActivity implements AdapterView.OnItemClickListener {
    private Class mClassList[] = {
            UtilsScreenActivity.class,
            UtilsContextActivity.class,
            UtilsResourceActivity.class,
            UtilsStringActivity.class,
            UtilsNetActivity.class,
            UtilsNotificationActivity.class,
    };

    @Override
    public String[] getItemNames() {
        return new String[] {
                "ScreenUtil",
                "ContextUtil",
                "ResourceUtil",
                "StringUtil",
                "NetUtil",
                "NotificationUtil",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        startActivity(new Intent(this, mClassList[i]));
    }

}
