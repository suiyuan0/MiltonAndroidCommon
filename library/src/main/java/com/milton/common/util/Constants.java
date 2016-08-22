package com.milton.common.util;

public class Constants {

    //update start
    // json {"url":"http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}
    public static final String APK_DOWNLOAD_URL = "url";
    public static final String APK_UPDATE_CONTENT = "updateMessage";
    public static final String APK_VERSION_CODE = "versionCode";
    public static final int TYPE_NOTIFICATION = 2;
    public static final int TYPE_DIALOG = 1;
    public static final String TAG = "UpdateChecker";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/feicien/android-auto-update/develop/extras/update.json";
    //update end
}
