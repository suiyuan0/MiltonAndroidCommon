
package com.milton.common.net;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @author linming 2013-12-31 下面我从安卓开发的角度，简单写一下如何获取手机设备信息和手机号码 需要在工程的AndroidManifest.xml文件中，添加权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
 */
public class PhoneInfoUtil {

    /**
     * 获取电话号码
     * 
     * @param context
     * @return
     */
    public String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber = null;
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取手机服务商信息
     * 
     * @param context
     * @return
     */
    public String getProvidersName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = "N/A";
        String IMSI;
        try {
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProvidersName;
    }

    /**
     * 获取手机信息
     * 
     * @param context
     * @return
     */
    public String getPhoneInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();

        sb.append("\nDeviceId(IMEI) = " + telephonyManager.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = "
                + telephonyManager.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + telephonyManager.getLine1Number());
        sb.append("\nNetworkCountryIso = "
                + telephonyManager.getNetworkCountryIso());
        sb.append("\nNetworkOperator = "
                + telephonyManager.getNetworkOperator());
        sb.append("\nNetworkOperatorName = "
                + telephonyManager.getNetworkOperatorName());
        sb.append("\nNetworkType = " + telephonyManager.getNetworkType());
        sb.append("\nPhoneType = " + telephonyManager.getPhoneType());
        sb.append("\nSimCountryIso = " + telephonyManager.getSimCountryIso());
        sb.append("\nSimOperator = " + telephonyManager.getSimOperator());
        sb.append("\nSimOperatorName = "
                + telephonyManager.getSimOperatorName());
        sb.append("\nSimSerialNumber = "
                + telephonyManager.getSimSerialNumber());
        sb.append("\nSimState = " + telephonyManager.getSimState());
        sb.append("\nSubscriberId(IMSI) = "
                + telephonyManager.getSubscriberId());
        sb.append("\nVoiceMailNumber = "
                + telephonyManager.getVoiceMailNumber());
        return sb.toString();
    }
}

/*
 * 最后，我们看LOGCAT日志，结果如下： ------------------------------------------------------------ 04-01 16:20:57.105: I/System.out(952): 460003121934674 04-01 16:20:57.105: I/System.out(952): getProvidersName:中国移动
 * 04-01 16:20:57.115: I/System.out(952): getNativePhoneNumber:136XXXXXXX 04-01 16:20:57.115: I/System.out(952): ------------------------ 04-01 16:20:57.145: I/System.out(952): getPhoneInfo: 04-01
 * 16:20:57.145: I/System.out(952): DeviceId(IMEI) = 352XXXXXXXX61328 04-01 16:20:57.145: I/System.out(952): DeviceSoftwareVersion = 01 04-01 16:20:57.145: I/System.out(952): Line1Number = 136XXXXXXX
 * 04-01 16:20:57.145: I/System.out(952): NetworkCountryIso = cn 04-01 16:20:57.145: I/System.out(952): NetworkOperator = 46000 04-01 16:20:57.145: I/System.out(952): NetworkOperatorName = 中国移动 04-01
 * 16:20:57.145: I/System.out(952): NetworkType = 2 04-01 16:20:57.145: I/System.out(952): PhoneType = 1 04-01 16:20:57.145: I/System.out(952): SimCountryIso = cn 04-01 16:20:57.145:
 * I/System.out(952): SimOperator = 46000 04-01 16:20:57.145: I/System.out(952): SimOperatorName = CMCC 04-01 16:20:57.145: I/System.out(952): SimSerialNumber = 898xxxxxx90108 04-01 16:20:57.145:
 * I/System.out(952): SimState = 5 04-01 16:20:57.145: I/System.out(952): SubscriberId(IMSI) = 46000xxxxxxxx4674 那么我们来解释一下上面的信息具体代表啥意思。 getNativePhoneNumber 获取的手机号 DeviceId(IMEI)手机 国际移动用户识别码
 * NetworkOperator 移动运营商编号 NetworkOperatorName 移动运营商名称 SimSerialNumber SimOperator SimCountryIso SimSerialNumber SubscriberId(IMSI) 关于手机SIM卡的一些详细信息 其实代码中没有写
 * 获取System.ANDROID_ID这个操作，因为很多手机设备获取不到andnroid_id 当然了，我们今天主要讲述<uses-permission android:name="android.permission.READ_PHONE_STATE"/> 光使用这个权限我们可以获得的手机设备信息和手机号 如果想获得WIFI，蓝牙，GPS，读写SDCARD更多的信息，就需要添加其它的权限。
 * 此外，本次测试我拿自己的手机SIM卡测试是可以获取到手机号码的，当然了有些手机号码是获取不到的，这里也给大家罗列了原因， 手机号码不是所有的都能获取。只是有一部分可以拿到。
 * 这个是由于移动运营商没有把手机号码的数据写入到sim卡中.SIM卡只有唯一的编号，供网络与设备识别那就是IMSI号码，手机的信号也可以说是通过这个号码在网络中传递的，并不是手机号码。试想，你的SIM丢失后，补办一张新的会换号码吗？是不会的.就是因为在你的手机号码对应的IMSI号 在移动运营商中被修改成新SIM卡的IMSI号码。 　　那么手机号为什么有的就能显示呢?
 * 　　这个就像是一个变量，当移动运营商为它赋值了，它自然就会有值。不赋值自然为空。 　　对于移动的用户，手机号码(MDN)保存在运营商的服务器中，而不是保存在SIM卡里。SIM卡只保留了IMSI和一些验证信息。手机每次入网注册的时候，都会以短信的形式将IMSI及验证信息上传到运营商的服务器，服务器在完成注册动作之后，会以短信的形式将注册的结果下发到手机里。下发的内容会因条件不同而不同。
 * 　　如果服务器在下发的短信中，不包含手机的号码，手机是无法取得电话号码。如果短信中包含了号码，手机才会将其缓存，以备他用.此外，对于其他运行商的SIM卡或者UIM卡，MDN有可能保存在UIM卡中。100%能够取得本机号码不太可能。 　　移动神州行,联通的卡是可以取到的.动感地带的取不到.别的卡还没有试过.
 * 　　能够读取SIM卡号的话应该有前提.那就是SIM卡已经写入了本机号码，不然是无法读取的。
 */
