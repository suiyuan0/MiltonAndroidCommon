/*
 * AVSync.hh
 *
 *  Created on: Nov 16, 2015
 *      Author: e562450
 */

#ifndef AVSYNC_HH_
#define AVSYNC_HH_

#include <android/log.h>
#include <jni.h>

// 5秒超时
//#define WAITTIMEOUT    (5)
// 500 ms超时
#define WAITTIMEOUT    (500)
// 缓冲100毫秒
#define BUFFERTIME     (200)
#define FIXRTPHEADLEN  (12)

#define MAX_FRAME_SIZE (512 * 1024 * 1024)
//start code maybe 3
#define NALU_START_CODE_LEN (4)
#define HW_NALU_TYPE_SPS (0x01)
#define HW_NALU_TYPE_PPS (0x02)
#define HW_NALU_TYPE_IDR (0x04)

//#define PRINTF         printf
// 定义info信息
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "av sync",__VA_ARGS__)
// 定义debug信息
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "av sync", __VA_ARGS__)
// 定义error信息
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "av sync",__VA_ARGS__)

// NALU type
typedef enum _NALU_TYPE {
	NALU_TYPE_UNSPECIFIED = 0,
	NALU_TYPE_NONIDR,
	NALU_TYPE_CODESLICEA,
	NALU_TYPE_CODESLICEB,
	NALU_TYPE_CODESLICEC,
	NALU_TYPE_IDR,
	NALU_TYPE_SEI,
	NALU_TYPE_SPS,
	NALU_TYPE_PPS,
	NALU_TYPE_AUD,
	NALU_TYPE_EOSEQ,
	NALU_TYPE_EOSTR,
	NALU_TYPE_FILTER,
} NALU_TYPE;

#endif /* AVSYNC_HH_ */
