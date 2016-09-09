
package com.milton.common.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class IOUtils {
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
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();

            } else {

                File file = new File(outPathString + File.separator + szName);
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


    /**
     * Close closable object and wrap {@link IOException} with {@link
     * RuntimeException}
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }


    /**
     * Close closable and hide possible {@link IOException}
     *
     * @param closeable closeable object
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }


    /**
     * 保存文本
     *
     * @param fileName 文件名字
     * @param content  内容
     * @param append   是否累加
     * @return 是否成功
     */
    public static boolean saveTextValue(String fileName, String content, boolean append) {

        try {
            File textFile = new File(fileName);
            if (!append && textFile.exists()) textFile.delete();

            FileOutputStream os = new FileOutputStream(textFile);
            os.write(content.getBytes("UTF-8"));
            os.close();
        } catch (Exception ee) {
            return false;
        }

        return true;
    }


    /**
     * 删除目录下所有文件
     *
     * @param Path 路径
     */
    public static void deleteAllFile(String Path) {

        // 删除目录下所有文件
        File path = new File(Path);
        File files[] = path.listFiles();
        if (files != null) {
            for (File tfi : files) {
                if (tfi.isDirectory()) {
                    System.out.println(tfi.getName());
                } else {
                    tfi.delete();
                }
            }
        }
    }
}
