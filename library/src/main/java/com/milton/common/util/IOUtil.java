
package com.milton.common.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class IOUtil {
    /**
     * 文件夹排序（先文件夹排序，后文件排序）
     * 
     * @param files
     */
    public static void sortFiles(File[] files) {
        Arrays.sort(files, new Comparator<File>() {

            @Override
            public int compare(File lhs, File rhs) {
                // 返回负数表示o1 小于o2，返回0 表示o1和o2相等，返回正数表示o1大于o2。
                boolean l1 = lhs.isDirectory();
                boolean l2 = rhs.isDirectory();
                if (l1 && !l2)
                    return -1;
                else if (!l1 && l2)
                    return 1;
                else {
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        });
    }

    /**
     * 解压一个压缩文档 到指定位置
     * 
     * @param zipFileString 压缩包的名字
     * @param outPathString 指定的路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        java.util.zip.ZipInputStream inZip = new java.util.zip.ZipInputStream(new java.io.FileInputStream(zipFileString));
        java.util.zip.ZipEntry zipEntry;
        String szName = "";

        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();

            if (zipEntry.isDirectory()) {

                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                java.io.File folder = new java.io.File(outPathString + java.io.File.separator + szName);
                folder.mkdirs();

            } else {

                java.io.File file = new java.io.File(outPathString + java.io.File.separator + szName);
                file.createNewFile();
                // get the output stream of the file
                java.io.FileOutputStream out = new java.io.FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // read (len) bytes into buffer
                while ((len = inZip.read(buffer)) != -1) {
                    // write (len) byte from buffer at the position 0
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }// end of while

        inZip.close();

    }// end of func
}
