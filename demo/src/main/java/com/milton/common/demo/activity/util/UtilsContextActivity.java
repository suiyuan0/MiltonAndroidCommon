
package com.milton.common.demo.activity.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.milton.common.demo.util.Constants;
import com.milton.common.util.ContextUtil;
import com.milton.common.util.ToastUtil;

import java.util.List;

public class UtilsContextActivity extends UtilsBaseActivity {
    private static final String TAG = "alinmi";

    @Override
    public String[] getItemNames() {
        return new String[] {
                "startApkActivity",
                "getActivities",
                "isIntentAvailable",
                "getAllPackageInfo",
                "getHotspotSetting",
                "collapseStatusBar",
                "expandStatusBar",
                "getStatusBarHeight",
                "setFullScreen",
                "setNotFullScreen",
                "getAllInstalledLauncherActivities",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                // ContextUtil.startApkActivity(this, "com.tencent.mobileqq");
                ContextUtil.startApkActivity(this, "com.topnews");
                break;
            case 1:
                List<String> list = ContextUtil.getActivities(this, Constants.UTIL_ACTION);
                if (list != null && list.size() > 0) {
                    for (int i = 0, size = list.size(); i < size; i++)
                        Log.e(TAG, list.get(i));
                }
                break;
            case 2:
                String action = "action.a";
                ToastUtil.showShort(this, action + "  isIntentAvailable = " + ContextUtil.isIntentAvailable(this, action));
                break;
            case 3:
                ContextUtil.getAllPackageInfo(this);
                break;
            case 4:
                startActivity(ContextUtil.getHotspotSetting());
                break;
            case 5:
                ContextUtil.collapseStatusBar(this);
                ToastUtil.showShort(this, "collapseStatusBar");
                break;
            case 6:
                ContextUtil.expandStatusBar(this);
                ToastUtil.showShort(this, "expandStatusBar");
                break;
            case 7:
                ToastUtil.showShort(this, "StatusBarHeight is " + ContextUtil.getStatusBarHeight(this));
                break;
            case 8:
                ContextUtil.setFullScreen(this);
                ToastUtil.showShort(this, "setFullScreen");
                break;
            case 9:
                ContextUtil.setNotFullScreen(this);
                ToastUtil.showShort(this, "setNotFullScreen");
                break;
            case 10:
                List<ResolveInfo> list2 = ContextUtil.getAllInstalledLauncherActivities(this);
                if (list2 != null && list2.size() > 0) {
                    for (int i = 0, size = list2.size(); i < size; i++) {
                        ApplicationInfo app = list2.get(i).activityInfo.applicationInfo;
                        Log.e(TAG, "package: " + app.packageName + ", sourceDir: " + app.sourceDir);
                    }
                }
                break;

            default:
                break;
        }
    }
}
