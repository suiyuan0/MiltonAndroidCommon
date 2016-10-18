#include "com_milton_jnitest_NdkJniUtils.h"
#include <android/log.h>
#include <string.h>

#define LOG_TAG "System.out2"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

//typedef enum android_LogPriority {
//    ANDROID_LOG_UNKNOWN = 0,
//    ANDROID_LOG_DEFAULT,    /* only for SetMinPriority() */
//            ANDROID_LOG_VERBOSE,
//    ANDROID_LOG_DEBUG,
//    ANDROID_LOG_INFO,
//    ANDROID_LOG_WARN,
//    ANDROID_LOG_ERROR,
//    ANDROID_LOG_FATAL,
//    ANDROID_LOG_SILENT,     /* only for SetMinPriority(); must be last */
//} android_LogPriority;


//占位符	数据类型
//%d	int
//%ld	long int
//%c	char
//%f	float
//%lf	double
//%x	十六进制
//%O	八进制
//%s	字符串


//方法名称规定 : Java_完整包名类名_方法名()
JNIEXPORT jstring JNICALL Java_com_milton_jnitest_NdkJniUtils_getCLanguageString
        (JNIEnv *env, jobject obj) {
    LOGD("This is Jni test!!! 此处有中文");
    jstring aaa = (*env)->NewStringUTF(env, "This is Jni test!!! 此处有中文");
    LOGD(" aaaaaaa %s", (*env)->GetStringUTFChars(env, aaa, 0));
    return (*env)->NewStringUTF(env, "This is Jni test!!! 此处有中文");

}

JNIEXPORT jint JNICALL Java_com_milton_jnitest_NdkJniUtils_calAAndB
        (JNIEnv *env, jobject obj, jint a, jint b) {
    LOGI("JNI_日志 : x = %d , y = %d", a, b);
    return a + b;
}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_loginServer
        (JNIEnv *env, jobject obj, jstring a, jstring b) {
    LOGD("loginServer  a is %s , b is %s", a, b);

    char *p = (*env)->GetStringUTFChars(env, a, 0);
    char *q = (*env)->GetStringUTFChars(env, b, 0);
    char *o = (*env)->GetStringChars(env, a, 0);
    //打印Java传递过来的数据
    LOGD("loginServer  a is %s , b is %s , a is %s ", p, q, o);

    char *append = "append";

    //strcat(dest, source) 函数可以将source字符串 添加到dest字符串后面
    LOGD("strcat(p, append) = %s", strcat(q, append));
}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_intArray
        (JNIEnv *env, jclass clazz, jintArray arr) {
//获取arr大小
    int len = (*env)->GetArrayLength(env, arr);

    //在LogCat中打印出arr的大小
    LOGI("the length of array is %d", len);

    //如果长度为0, 返回arr
//    if (len == 0)
//        return arr;

    //如果长度大于0, 那么获取数组中的每个元素
    jint *p = (*env)->GetIntArrayElements(env, arr, 0);

    //打印出数组中每个元素的值
    int i = 0;
    for (; i < len; i++) {
        LOGI("arr[%d] = %d", i, *(p + i));
    }

//    return arr;
}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_stringArray
        (JNIEnv *env, jclass clazz, jobjectArray array) {


}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_callCcode
        (JNIEnv *env, jobject obj) {
    //调用DataProvider对象中的helloFromJava()方法
    //获取到某个对象, 获取对象中的方法, 调用获取到的方法
    LOGI("in code  callCcode");
    //DataProvider完整类名 shulaing.han.ndk_callback.DataProvider
    char *classname = "com/milton/jnitest/NdkJniUtils";


    jclass dpclazz = (*env)->FindClass(env, classname);
    if (dpclazz == 0) {
        LOGI("class not find !!!");
    } else {
        LOGI("class find !!!");
    }

    //参数介绍 : 第二个参数是Class对象, 第三个参数是方法名,第四个参数是方法的签名, 获取到调用的method
    jmethodID methodID = (*env)->GetMethodID(env, dpclazz, "helloFromJava", "()V");
    if (methodID == 0) {
        LOGI("method not find !!!");
    } else {
        LOGI("method find !!!");
    }

    /*
     * 调用方法 void (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
     * 参数介绍 : 后面的 ... 是可变参数, 如果该返回值void的方法有参数, 就将参数按照次序排列
     */
    LOGI("before call method");
    (*env)->CallVoidMethod(env, obj, methodID);
    LOGI("after call method");
}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_callCcode2
        (JNIEnv *env, jobject obj) {
//调用DataProvider对象中的helloFromJava()方法
    //获取到某个对象, 获取对象中的方法, 调用获取到的方法
    LOGI("in code  callCcode2");
    //DataProvider完整类名 shulaing.han.ndk_callback.DataProvider
    char *classname = "com/milton/jnitest/NdkJniUtils";


    jclass dpclazz = (*env)->FindClass(env, classname);
    if (dpclazz == 0)
        LOGI("class not find !!!");
    else
        LOGI("class find !!!");

    //参数介绍 : 第二个参数是Class对象, 第三个参数是方法名,第四个参数是方法的签名, 获取到调用的method
    jmethodID methodID = (*env)->GetStaticMethodID(env, dpclazz, "printString",
                                                   "(Ljava/lang/String;)V");
    if (methodID == 0)
        LOGI("method not find !!!");
    else
        LOGI("method find !!!");

    /*
     * 调用方法 void (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
     * 参数介绍 : 后面的 ... 是可变参数, 如果该返回值void的方法有参数, 就将参数按照次序排列
     */
    LOGI("before call method");
    (*env)->CallStaticVoidMethod(env, dpclazz, methodID, (*env)->NewStringUTF(env,
                                                                              "printString method callback success!!"));
    LOGI("after call method");
}

JNIEXPORT void JNICALL Java_com_milton_jnitest_NdkJniUtils_callCcode3
        (JNIEnv *env, jobject obj) {
//调用DataProvider对象中的helloFromJava()方法
    //获取到某个对象, 获取对象中的方法, 调用获取到的方法
    LOGI("in code  callCcode3");
    //DataProvider完整类名 shulaing.han.ndk_callback.DataProvider
    char *classname = "com/milton/jnitest/NdkJniUtils";


    jclass dpclazz = (*env)->FindClass(env, classname);
    if (dpclazz == 0)
        LOGI("class not find !!!");
    else
        LOGI("class find !!!");

    //参数介绍 : 第二个参数是Class对象, 第三个参数是方法名,第四个参数是方法的签名, 获取到调用的method
    jmethodID methodID = (*env)->GetMethodID(env, dpclazz, "add", "(II)I");
    if (methodID == 0)
        LOGI("method not find !!!");
    else
        LOGI("method find !!!");

    /*
     * 调用方法 void (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
     * 参数介绍 : 后面的 ... 是可变参数, 如果该返回值void的方法有参数, 就将参数按照次序排列
     */
    LOGI("before call method");
    jint result = (*env)->CallIntMethod(env, obj, methodID, 3, 5);
    LOGI("after call method %d ", result);
}
