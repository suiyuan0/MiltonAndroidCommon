package com.milton.common.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by milton on 16/6/28.
 */
public class FileUtil {
    public static String generateAbsoluteFilePath(Context context, String path) {
        String absolutePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + path;
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            absolutePath = context.getFilesDir().getAbsolutePath() + File.separator + path;
        }
        return absolutePath;
    }
}
