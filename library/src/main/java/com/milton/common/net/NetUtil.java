
package com.milton.common.net;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author linming 2013-12-31
 */
public class NetUtil {

    // import android.database.Cursor;
    // import android.net.ConnectivityManager;
    // import android.net.NetworkInfo;
    // import android.os.Bundle;
    // import android.telephony.TelephonyManager;
    // import android.text.TextUtils;
    // import android.util.Log;

    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String NET_3G = "3gnet";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String UNINET = "uninet";

    public static final int TYPE_CT_WAP = 5;
    public static final int TYPE_CT_NET = 6;
    public static final int TYPE_CT_WAP_2G = 7;
    public static final int TYPE_CT_NET_2G = 8;

    public static final int TYPE_CM_WAP = 9;
    public static final int TYPE_CM_NET = 10;
    public static final int TYPE_CM_WAP_2G = 11;
    public static final int TYPE_CM_NET_2G = 12;

    public static final int TYPE_CU_WAP = 13;
    public static final int TYPE_CU_NET = 14;
    public static final int TYPE_CU_WAP_2G = 15;
    public static final int TYPE_CU_NET_2G = 16;

    public static final int TYPE_OTHER = 17;

    public static Uri PREFERRED_APN_URI = Uri
            .parse("content://telephony/carriers/preferapn");

    /** 没有网络 */
    public static final int TYPE_NET_WORK_DISABLED = 0;

    /** wifi网络 */
    public static final int TYPE_WIFI = 4;

    // @Override
    // public void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.activity_main);
    // long start = System.currentTimeMillis();
    // int checkNetworkType = checkNetworkType(this);
    // Log.i("NetType", "===========elpase:"
    // + (System.currentTimeMillis() - start));
    //
    // switch (checkNetworkType) {
    // case TYPE_WIFI:
    // Log.i("NetType", "================wifi");
    // break;
    // case TYPE_NET_WORK_DISABLED:
    // Log.i("NetType", "================no network");
    // break;
    // case TYPE_CT_WAP:
    // Log.i("NetType", "================ctwap");
    // break;
    // case TYPE_CT_WAP_2G:
    // Log.i("NetType", "================ctwap_2g");
    // break;
    // case TYPE_CT_NET:
    // Log.i("NetType", "================ctnet");
    // break;
    // case TYPE_CT_NET_2G:
    // Log.i("NetType", "================ctnet_2g");
    // break;
    // case TYPE_CM_WAP:
    // Log.i("NetType", "================cmwap");
    // break;
    // case TYPE_CM_WAP_2G:
    // Log.i("NetType", "================cmwap_2g");
    // break;
    // case TYPE_CM_NET:
    // Log.i("NetType", "================cmnet");
    // break;
    // case TYPE_CM_NET_2G:
    // Log.i("NetType", "================cmnet_2g");
    // break;
    // case TYPE_CU_NET:
    // Log.i("NetType", "================cunet");
    // break;
    // case TYPE_CU_NET_2G:
    // Log.i("NetType", "================cunet_2g");
    // break;
    // case TYPE_CU_WAP:
    // Log.i("NetType", "================cuwap");
    // break;
    // case TYPE_CU_WAP_2G:
    // Log.i("NetType", "================cuwap_2g");
    // break;
    // case TYPE_OTHER:
    // Log.i("NetType", "================other");
    // break;
    // default:
    // break;
    // }
    // }

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return TYPE_NET_WORK_DISABLED;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return TYPE_WIFI;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！

                    boolean is3G = isFastMobileNetwork(mContext);

                    final Cursor c = mContext.getContentResolver().query(
                            PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c
                                .getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            if (user.startsWith(CTWAP)) {
                                return is3G ? TYPE_CT_WAP : TYPE_CT_WAP_2G;
                            } else if (user.startsWith(CTNET)) {
                                return is3G ? TYPE_CT_NET : TYPE_CT_NET_2G;
                            }
                        }
                    }
                    c.close();

                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    Log.i("", "==================netmode:" + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();

                        if (netMode.equals(CMWAP)) {
                            return is3G ? TYPE_CM_WAP : TYPE_CM_WAP_2G;
                        } else if (netMode.equals(CMNET)) {
                            return is3G ? TYPE_CM_NET : TYPE_CM_NET_2G;
                        } else if (netMode.equals(NET_3G)
                                || netMode.equals(UNINET)) {
                            return is3G ? TYPE_CU_NET : TYPE_CU_NET_2G;
                        } else if (netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            return is3G ? TYPE_CU_WAP : TYPE_CU_WAP_2G;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER;
        }

        return TYPE_OTHER;

    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;

        }
    }

    /**
     * 判断网络状态
     * 
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        // WifiManager wifimanager = (WifiManager)
        // getSystemService(WIFI_SERVICE);
        // wifimanager.isWifiEnabled();//用来判断是否使用的是wifi
        // wifimanager.getWifiState();//获取wifi的状态 可用 正在获取 不可用 未知等

        return (info != null && info.isConnected());
    }

    /**
     * 设置网络
     * 
     * @param context
     */
    public static void showSetNetworkDialog(final Context context) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("设置网络");
        builder.setMessage("网络错误请检查网络状态");
        builder.setPositiveButton("设置网络", new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                // 类名一定要包含包名
                intent.setClassName("com.android.settings",
                        "com.android.settings.WirelessSettings");

                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    /**
     * 根据Android的安全机制，在使用ConnectivityManager时，必须在AndroidManifest.xml中添加<uses- permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 否则无法获得系统的许可。
     * 
     * @param context
     */
    public static void checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        mobile.toString(); // 显示3G网络连接状态
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        wifi.toString(); // 显示wifi连接状态
    }

    /**
     * 使用过Android手机上的手机QQ的朋友，应该知道，当QQ启动时，如果没有有效的网络连接，QQ会提示转入手机的网络配置界面。这是如何实现的呢。 其实很简单啦
     * 
     * @param context
     */
    public static void checkNetworkInfo1(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        mobile.toString(); // 显示3G网络连接状态
        // wifi
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        wifi.toString(); // 显示wifi连接状态

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == State.CONNECTED || mobile == State.CONNECTING) return;
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) return;

        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));// 进入无线网络配置界面
        // startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        // //进入手机中的wifi网络设置界面

    }

}
