#include "com_milton_common_demo_activity_jni_HelloJniActivity.h"
  
JNIEXPORT jstring JNICALL Java_com_milton_common_demo_activity_jni_HelloJniActivity_hellojni
  (JNIEnv* env, jobject obj)  
{  
    return env->NewStringUTF( "Hello from JNI !  Compiled with ABI .");  
} 