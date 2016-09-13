
package com.milton.common.demo.fragment;


import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.widget.AdapterView;

import com.milton.common.demo.util.Constants;
import com.milton.common.util.ContextUtil;
import com.milton.common.util.LogUtil;

import java.util.List;

public class Fragment3UtilContext extends Fragment2Base {
    private static final String TAG = Fragment3UtilContext.class.getSimpleName();

    @Override
    public String[] getItemNames() {
        return new String[]{
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
                // ContextUtil.startApkActivity(getActivity(), "com.tencent.mobileqq");
                ContextUtil.startApkActivity(getActivity(), "com.topnews");
                break;
            case 1:
                List<String> list = ContextUtil.getActivities(getActivity(), Constants.UTIL_ACTION);
                if (list != null && list.size() > 0) {
                    for (int i = 0, size = list.size(); i < size; i++)
                        LogUtil.e(TAG, list.get(i));
                }
                break;
            case 2:
                String action = "action.a";
                showToastShort(action + "  isIntentAvailable = " + ContextUtil.isIntentAvailable(getActivity(), action));
                break;
            case 3:
                ContextUtil.getAllPackageInfo(getActivity());
                break;
            case 4:
                startActivity(ContextUtil.getHotspotSetting());
                break;
            case 5:
                ContextUtil.collapseStatusBar(getActivity());
                showToastShort("collapseStatusBar");
                break;
            case 6:
                ContextUtil.expandStatusBar(getActivity());
                showToastShort("expandStatusBar");
                break;
            case 7:
                showToastShort("StatusBarHeight is " + ContextUtil.getStatusBarHeight(getActivity()));
                break;
            case 8:
                ContextUtil.setFullScreen(getActivity());
                showToastShort("setFullScreen");
                break;
            case 9:
                ContextUtil.setNotFullScreen(getActivity());
                showToastShort("setNotFullScreen");
                break;
            case 10:
                List<ResolveInfo> list2 = ContextUtil.getAllInstalledLauncherActivities(getActivity());
                if (list2 != null && list2.size() > 0) {
                    for (int i = 0, size = list2.size(); i < size; i++) {
                        ApplicationInfo app = list2.get(i).activityInfo.applicationInfo;
                        LogUtil.e(TAG, "package: " + app.packageName + ", sourceDir: " + app.sourceDir);
                    }
                }
                break;

            default:
                break;
        }
    }
}
