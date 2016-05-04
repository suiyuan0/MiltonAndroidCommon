
package com.milton.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class OtherUtil {
    public static final String TAG = "OtherUtil";

    /**
     * 静默安装的实现类，调用install()方法执行具体的静默安装逻辑 执行具体的静默安装逻辑，需要手机ROOT。
     * 
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public static boolean install(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 获取手机的堆大小，结果是以MB为单位进行返回
     *
     * @param context
     * @return
     */
    public static int getHeapSize(Context context) {
        if (null == context) {
            Log.e(TAG, "context can't be null!!!", new Exception());
            return 0;
        }
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        return heapSize;
    }

    public static int getMaxMemory() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
        Log.d(TAG, "Max memory is " + maxMemory + "MB");
        return maxMemory;
    }
}
