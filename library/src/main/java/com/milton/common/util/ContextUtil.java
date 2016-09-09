
package com.milton.common.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO http://www.cnblogs.com/over140/archive/2013/03/05/2706068.html sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
 * 
 * @author v-ming.lin
 */
public class ContextUtil {
    public static void startApkActivity(final Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                ctx.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            Log.e("startActivity", e.getMessage());
        }
    }

    /**
     * 获取应用程序下所有Activity
     * 
     * @param ctx
     * @return
     */
    public static ArrayList<String> getActivities(Context ctx, String action) {
        ArrayList<String> result = new ArrayList<String>();
        Intent intent = new Intent(action, null);
        intent.setPackage(ctx.getPackageName());
        for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result;
    }

    /**
     * 检查有没有应用程序来接受处理你发出的intent
     * 
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取已经安装APK的路径
     * 
     * @param context
     */
    public static void getAllPackageInfo(Context context) {
        if (context == null) {
            return;
        }
        PackageManager pm = context.getPackageManager();

        for (ApplicationInfo app : pm.getInstalledApplications(0)) {
            Log.e("alinmi", "package: " + app.packageName + ", sourceDir: " + app.sourceDir);
        }
    }

    /**
     * 调用 便携式热点和数据共享 设置
     * 
     * @return
     */
    public static Intent getHotspotSetting() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        ComponentName com = new ComponentName("com.android.settings", "com.android.settings.TetherSettings");
        intent.setComponent(com);
        return intent;
    }

    /**
     * 收起状态栏
     * 
     * @param ctx
     */
    public static final void collapseStatusBar(Context ctx) {
        Object sbservice = ctx.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method collapse;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                collapse = statusBarManager.getMethod("collapsePanels");
            } else {
                collapse = statusBarManager.getMethod("collapse");
            }
            collapse.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 展开状态栏
     * 
     * @param ctx
     */
    public static final void expandStatusBar(Context ctx) {
        Object sbservice = ctx.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method expand;
            if (Build.VERSION.SDK_INT >= 17) {
                expand = statusBarManager.getMethod("expandNotificationsPanel");
            } else {
                expand = statusBarManager.getMethod("expand");
            }
            expand.invoke(sbservice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 注意：切换到全屏时，底部的虚拟按键仍然是显示的。次方法可多次调用用于切换 用途：播放器界面经常会用到
     * 
     * @param activity
     */
    public static void setFullScreen(Activity activity) {
        // 切换到全屏
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setNotFullScreen(Activity activity) {
        // 切换到非全屏
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    /**
     * 调用开发者选项中显示触摸位置功能 设置1显示，设置0不显示。
     * 
     * @param context
     * @param flag
     */
    public static void showTouch(Context context, boolean flag) {
        android.provider.Settings.System.putInt(context.getContentResolver(), "show_touches", flag ? 1 : 0);
    }

    /**
     * 获取设备上已安装并且可启动的应用列表 使用getInstalledApplications会返回很多无法启动甚至没有图标的系统应用。ResolveInfo .activityInfo.applicationInfo也能取到你想要的数据。
     * 
     * @param context
     * @return
     */
    public static List<ResolveInfo> getAllInstalledLauncherActivities(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, 0);
        return activities;
    }


}
