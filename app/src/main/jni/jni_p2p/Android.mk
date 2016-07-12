LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libP2P-prebuilt
LOCAL_SRC_FILES := ../lib_p2p/libp2pcm.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavcodec-prebuilt
LOCAL_SRC_FILES := ../lib_ffmpeg/lib/libavcodec.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavformat-prebuilt
LOCAL_SRC_FILES := ../lib_ffmpeg/lib/libavformat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libavutil-prebuilt
LOCAL_SRC_FILES := ../lib_ffmpeg/lib/libavutil.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := p2plib
LOCAL_C_INCLUDES := . \
				$(LOCAL_PATH)/../lib_ffmpeg/include
LOCAL_SRC_FILES := \
	p2pconn.cpp HonH264Decoder.cpp AVSync.cpp SPS.cpp
LOCAL_STATIC_LIBRARIES := libP2P-prebuilt \
					libavcodec-prebuilt libavformat-prebuilt libavutil-prebuilt
LOCAL_LDLIBS := -llog -lz
include $(BUILD_SHARED_LIBRARY)
