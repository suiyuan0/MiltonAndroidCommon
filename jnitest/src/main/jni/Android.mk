LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := emJniLibName
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := jnitest.c
LOCAL_LDLIBS += -llog
LOCAL_C_INCLUDES += /Users/milton/Milton/Tools/Mac/AS_Workspace/MiltonAndroidCommon/jnitest/src/main/jni


include $(BUILD_SHARED_LIBRARY)
