package com.milton.jnitest;

import android.util.Log;

/**
 * Created by milton on 16/9/28.
 */
public class NdkJniUtils {


    static {
        System.loadLibrary("emJniLibName"); //defaultConfig.ndk.moduleName
    }

    public static native void intArray(int[] a);

    public static native void stringArray(String[] s);

    //C调用java中参数为string的方法
    public static void printString(String s) {
        System.out.println(s);
    }

    public native String getCLanguageString();

    public native int calAAndB(int a, int b);

    public native void loginServer(String user, String pwd);

    public native void callCcode();

    public native void callCcode2();

    //C调用java中空方法 com.milton.jnitest.NdkJniUtils

    public native void callCcode3();

    public void helloFromJava() {
        System.out.println("hello from java");
        Log.e("system.out2", "hello from java");
    }

    //C调用java中的带两个int参数的方法
    public int add(int x, int y) {
        return x + y;
    }
}
