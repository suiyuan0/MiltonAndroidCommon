/*
 * AVSync.cpp
 *
 *  Created on: Nov 16, 2015
 *      Author: e562450
 */

#include "AVSync.hh"
#include "AVSyncApi.hh"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "RtpHeader.h"

static unsigned char start_code[4] = {0x00, 0x00, 0x00, 0x01};


// 模板，用户启动线程。pthread_create启动的函数不能是非static，此法可以解决这个问题。
template <typename TYPE, void (TYPE::*ThdRun)() >
void* _thread_t(void* param)//线程启动函数，声明为模板函数
{
	TYPE* This = (TYPE*)param;
	This->ThdRun();
	return NULL;
}

// 链表数据，注意：没有检查参数，用前检查
AVSyncRtpData * AVSyncer::newRtpData(unsigned char * data, int len, unsigned char * exData, int exLen) {
	AVSyncRtpData * ret = new AVSyncRtpData();
	if(NULL != ret) {
		ret->mAVData = new unsigned char [len];
		ret->mDataLen = len;
		memcpy(ret->mAVData, data, len);
		ret->next = NULL;
		ret->prev = NULL;
        ret->mExData = new unsigned char [exLen + 1];
        memcpy(ret->mExData, exData, exLen);
        ret->mExData[exLen] = '\0';
        ret->mExDataLen = exLen;
	}
	return ret;
}
int AVSyncer::deleteRtpData(AVSyncRtpData * rtp) {
	if(NULL == rtp){
		return -1;
	}
	if(NULL != rtp->mAVData) {
		delete rtp->mAVData;
		rtp->mAVData = NULL;
		rtp->mDataLen = 0;
	}
    if(NULL != rtp->mExData) {
        delete rtp->mExData;
        rtp->mExData = NULL;
        rtp->mExDataLen = 0;
    }
	rtp->next = NULL;
	rtp->prev = NULL;
	delete rtp;
	rtp = NULL;
	return 0;
}

AVSyncer::AVSyncer() {
	int ret;
	mVideoListHead = NULL;
	mVideoListTail = NULL;
	mAudioListHead = NULL;
	mAudioListTail = NULL;
	ret = pthread_mutex_init(&mVideoLock, NULL);
	//LOGE("create thread ret = %d\n", ret);
	ret = pthread_mutex_init(&mAudioLock, NULL);
	//LOGE("create thread ret = %d\n", ret);
#ifdef __MACH__
    mSyncSem = sem_open("honeywell_cube", O_CREAT, 0644, 0);
#else
    ret = sem_init(&mSyncSem, 0, 0);
#endif
	//LOGE("create thread ret = %d\n", ret);

	mNeedSync = 0;
	mRecvReady = 1;
	mNextVideoSeq = 0;
	mNextAudioSeq = 0;
	mSysStartTm = 0;
	//mAVStartTm = 0;
	mVideoStartStp = 0;
	mAudioStartStp = 0;
	mCallBack = NULL;

	memset(&mMediaInfo, 0, sizeof(mMediaInfo));
	mMediaInfo.memory_buf = (unsigned char*)malloc(MAX_FRAME_SIZE);
    if (mMediaInfo.memory_buf == NULL)
    {
        LOGE("memory_buf failed!!\n");
    }

	if(pthread_create(&mThread, NULL, _thread_t<AVSyncer, &AVSyncer::ThdRun>, this)){
		LOGE("create thread failed!!\n");
	}
}
AVSyncer::AVSyncer(bool bSync) {
	AVSyncer();
	mNeedSync = bSync;
}
AVSyncer::AVSyncer(void * cb, bool bSync) {
	AVSyncer();
	mNeedSync = bSync;
	mCallBack = (StreamDataCallback)cb;
}
AVSyncer::~AVSyncer() {
	AVSyncRtpData * tmpPtr = 0;
	mThdRun = false;
	mRecvReady = 0;
	pthread_join(mThread, NULL); //等待线程结束
	pthread_mutex_destroy(&mVideoLock);
	pthread_mutex_destroy(&mAudioLock);
#ifdef __MACH__
    int retErr = sem_close(mSyncSem);
    //ClearSemaphore("honeywell_cube");
#else
    sem_destroy(&mSyncSem);
#endif

	// free buffer
	if (mMediaInfo.memory_buf) {
		free(mMediaInfo.memory_buf);
		mMediaInfo.memory_buf = 0;
	}
	mMediaInfo.frame_len = 0;
	mMediaInfo.write_pos = 0;

	// clear list
	while(mVideoListHead) {
		tmpPtr = mVideoListHead;
		mVideoListHead = mVideoListHead->next;
		deleteRtpData(tmpPtr);
		tmpPtr = NULL;
	}
	mVideoListTail = NULL;
	while(mAudioListHead) {
		tmpPtr = mAudioListHead;
		mAudioListHead = mAudioListHead->next;
		deleteRtpData(tmpPtr);
		tmpPtr = NULL;
	}
	mAudioListTail = NULL;
}

/*bool AVSyncer::checkMark(unsigned long stp) {
	bool ret = false;
	AVSyncRtpData * ptr = 0;
	ptr = mVideoListHead;
	while(NULL != ptr){
		if(ptr->mRtpStp == stp) {
			ret = true;
			break;
		}
		ptr = ptr->next;
	}

	return ret;
}*/

int AVSyncer::unpackH264(unsigned char* stream, unsigned int stream_len)
{
	if (!stream || stream_len <= 0) {
		return -1;
	}
	//reset_rtp_unpack_264(media_info);
	unsigned char nalu_type = (*stream) & 0x1F;
	unsigned int temp;
	if (nalu_type < 24) { //single NALU
		return unpackSingleNalu(stream, stream_len);
	} else if (nalu_type == 24) {  //stap-a
		return unpackStapaNalu(stream, stream_len, temp);
	} else if (nalu_type == 28) { //fu-a
		return unpackFuaNalu(stream, stream_len);
	} else {
		//do not support
	}
	return 0;
}

int AVSyncer::unpackPCM(unsigned char* stream, unsigned int stream_len){
	if ((MAX_FRAME_SIZE - mMediaInfo.frame_len) < stream_len) {
		return -1;
	}
	if(stream_len <= 0){
		return -1;
	}
	memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, stream, stream_len);
	mMediaInfo.write_pos += stream_len;
	mMediaInfo.frame_len += stream_len;
	return 0;
}

int AVSyncer::unpackSingleNalu(unsigned char* stream, unsigned int stream_len)
{
	if ((MAX_FRAME_SIZE - mMediaInfo.frame_len) < (NALU_START_CODE_LEN + stream_len)) {
		return -1;
	}
	memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, start_code, NALU_START_CODE_LEN);
	mMediaInfo.write_pos += NALU_START_CODE_LEN;
	memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, stream, stream_len);
	mMediaInfo.write_pos += stream_len;
	mMediaInfo.frame_len += (stream_len + NALU_START_CODE_LEN);

	return 0;
}

int AVSyncer::unpackStapaNalu(unsigned char* stream, unsigned int stream_len, unsigned int& nalu_type)
{
	unsigned int offset = 1; //skip stap_header
	nalu_type = 0;
	while (offset < stream_len) {
		int nalu_size = (*(stream + offset) << 8) | (*(stream + offset + 1));
		if ((MAX_FRAME_SIZE - mMediaInfo.frame_len) < (NALU_START_CODE_LEN + nalu_size)) {
			//ortp_error("[%s(%d):%s]: %s", __FILE__, __LINE__, __FUNCTION__, "there is no enough memory");
			return -1;
		}

		if ((*(stream + offset + 2) & 0x1F) == NALU_TYPE_SPS) {
			nalu_type |= HW_NALU_TYPE_SPS;
		} else if ((*(stream + offset + 2) & 0x1F) == NALU_TYPE_PPS) {
			nalu_type |= HW_NALU_TYPE_PPS;
		} else if ((*(stream + offset + 2) & 0x1F) == NALU_TYPE_IDR) {
			nalu_type |= HW_NALU_TYPE_IDR;
		}
		memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, start_code, NALU_START_CODE_LEN);
		mMediaInfo.write_pos += NALU_START_CODE_LEN;
		memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, stream + offset + 2, nalu_size);
		mMediaInfo.write_pos += nalu_size;
		mMediaInfo.frame_len += (NALU_START_CODE_LEN + nalu_size);
		offset += (2 + nalu_size);
	}
	return 0;
}

int AVSyncer::unpackFuaNalu(unsigned char* stream, unsigned int stream_len)
{
	if (stream[1] & 0x80) {
		if (MAX_FRAME_SIZE - mMediaInfo.frame_len < (NALU_START_CODE_LEN + 1)) {
			return -1;
		}
		memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, start_code, NALU_START_CODE_LEN);
		mMediaInfo.write_pos += NALU_START_CODE_LEN;
		char ualu_hdr = (stream[0] & 0xe0) | (stream[1] & 0x1f);
		*(mMediaInfo.memory_buf + mMediaInfo.write_pos) = ualu_hdr;
		mMediaInfo.write_pos++;
		mMediaInfo.frame_len += NALU_START_CODE_LEN + 1;
	}
	if ((MAX_FRAME_SIZE - mMediaInfo.frame_len) < (stream_len - 2)) {
		//ortp_error("[%s(%d):%s]: %s", __FILE__, __LINE__, __FUNCTION__, "there is no enough memory");
		return -1;
	}
	memcpy(mMediaInfo.memory_buf + mMediaInfo.write_pos, stream + 2, stream_len - 2);
	mMediaInfo.frame_len += (stream_len - 2);
	mMediaInfo.write_pos += (stream_len - 2);
	return 0;
}

int AVSyncer::resetRtpUnpack()
{
	mMediaInfo.frame_len = 0;
	mMediaInfo.write_pos = 0;
	return 0;
}

void AVSyncer::ThdRun() {
	int result;
	unsigned long    stp;
	unsigned long    curTime = 0;
	struct timespec  sys_time = {0,0};
	struct timespec  ts;
	struct timeval   tt;
	AVSyncRtpData *  ptr;
	mThdRun = true;
	//pthread_detach(pthread_self());
	//sem_post(&mSyncSem);
	while(mThdRun) {
		gettimeofday(&tt, NULL);
		//ts.tv_sec = tt.tv_sec + WAITTIMEOUT;
		//ts.tv_nsec = tt.tv_usec*1000;
		ts.tv_sec = tt.tv_sec + (tt.tv_usec/1000 + WAITTIMEOUT)/1000;
		ts.tv_nsec = ((tt.tv_usec + WAITTIMEOUT*1000)%1000000) * 1000;
#ifdef __MACH__
        result = sem_timedwait(mSyncSem, &ts);
#else
        result = sem_timedwait(&mSyncSem, &ts);
#endif
		if(!mThdRun){
			break;
		}
		if(result == 0){
			//LOGE("result is 0?\n");
			// 有信号过来
			// 1. 同步，2. 组帧
			/***  视频  ***/
			if(0 == pthread_mutex_lock(&mVideoLock)) {
				//lock
				if(mVideoListHead != NULL) {
					stp = mVideoListHead->mRtpStp;
					clock_gettime(CLOCK_MONOTONIC,&sys_time);
					curTime = sys_time.tv_sec * 1000 + sys_time.tv_nsec/1000000;
					if(curTime >= stp) {
						// stp过去时间，音视频应该被播放了
						/*if(checkMark(stp)) {
							// have mark, 组帧
						} else {
							// drop
						}*/
						resetRtpUnpack();
						// mark不一定有，看time stamp是否一样
						while(NULL != mVideoListHead && mVideoListHead->mRtpStp == stp) {
							unpackH264(mVideoListHead->mAVData + FIXRTPHEADLEN,
									mVideoListHead->mDataLen - FIXRTPHEADLEN);
							ptr = mVideoListHead;//需要删除
                            // sps/pps/I 帧放到一起。sei跟I/P帧放一起。所以，不看mark位了，只看时间戳。
							/*if(mVideoListHead->mMarker) {
								mVideoListHead = mVideoListHead->next;
								if(NULL != mVideoListHead){
									mVideoListHead->prev = NULL;
								}
								deleteRtpData(ptr);
								break;//mark位，双重判断
							}*/
							mVideoListHead = mVideoListHead->next;
							if(NULL != mVideoListHead){
								mVideoListHead->prev = NULL;
							}
							deleteRtpData(ptr);
						} // end while
						if(mMediaInfo.frame_len > 0) {
							//call back
							if(mCallBack) {
								mCallBack(mMediaInfo.memory_buf, mMediaInfo.frame_len, 0, stp);
							}
							/*LOGI("date len=%u, stp=%u, %x-%x-%x-%x-%x-%x", mMediaInfo.frame_len, stp,
									mMediaInfo.memory_buf[0], mMediaInfo.memory_buf[1],
									mMediaInfo.memory_buf[2], mMediaInfo.memory_buf[3],
									mMediaInfo.memory_buf[4], mMediaInfo.memory_buf[5]);*/
						}
					}
				}
				//unlock
				pthread_mutex_unlock(&mVideoLock);
			}
			/***  音频  ***/
			if(0 == pthread_mutex_lock(&mAudioLock)) {
				// lock
				if(mAudioListHead != NULL) {
					stp = mAudioListHead->mRtpStp;
					clock_gettime(CLOCK_MONOTONIC,&sys_time);
					curTime = sys_time.tv_sec * 1000 + sys_time.tv_nsec/1000000;
					if(curTime >= stp) {
						resetRtpUnpack();
						while(NULL != mAudioListHead && mAudioListHead->mRtpStp == stp) {
							unpackPCM(mAudioListHead->mAVData + FIXRTPHEADLEN,
									mAudioListHead->mDataLen - FIXRTPHEADLEN);
							ptr = mAudioListHead;//需要删除
							if(mAudioListHead->mMarker) {
								mAudioListHead = mAudioListHead->next;
								if(NULL != mAudioListHead) {
									mAudioListHead->prev = NULL;
								}
								deleteRtpData(ptr);
								break;//mark位，双重判断
							}
							mAudioListHead = mAudioListHead->next;
							if(NULL != mAudioListHead) {
								mAudioListHead->prev = NULL;
							}
							deleteRtpData(ptr);
						}
                        LOGE("audio info len=%d\n", mMediaInfo.frame_len);
						if(mMediaInfo.frame_len > 0) {
							//call back
							if(mCallBack) {
								mCallBack(mMediaInfo.memory_buf, mMediaInfo.frame_len, 1, stp);
							}
						}
					}
				}
				// unlock
				pthread_mutex_unlock(&mAudioLock);
			}
		} else {
			//LOGE("errno value :%d ,it means %s\n", errno, strerror(errno));
			// 超时
			// 清空队列?
		}
	}// end while
	// 释放资源，防止内存泄漏
	if(0 == pthread_mutex_lock(&mVideoLock)){
		while(NULL != mVideoListHead){
			ptr = mVideoListHead;
			mVideoListHead = mVideoListHead->next;
			if(NULL != mVideoListHead){
				mVideoListHead->prev = NULL;
			}
			deleteRtpData(ptr);
		}
		pthread_mutex_unlock(&mVideoLock);
	}
	if(0 == pthread_mutex_lock(&mAudioLock)) {
		while(NULL != mAudioListHead){
			ptr = mAudioListHead;
			mAudioListHead = mAudioListHead->next;
			if(NULL != mAudioListHead) {
				mAudioListHead->prev = NULL;
			}
			deleteRtpData(ptr);
		}
	}
}// thread
// 计算时间戳，入队列。同步由其他线程做。
// 时间戳和序号，不能跟上一个对比，会出问题。
int AVSyncer::putVideoData(unsigned char * data, int len, unsigned char * exData, int exLen){
	//sem_post(&mSyncSem);
	/*struct timespec ts;
	struct timeval  tt;
	gettimeofday(&tt, NULL);
	ts.tv_sec = tt.tv_sec;
	ts.tv_nsec = tt.tv_usec*1000 + WAITTIMEOUT * 1000 * 1000;
	ts.tv_sec += ts.tv_nsec/(1000 * 1000 *1000);
	ts.tv_nsec %= (1000 * 1000 *1000);
	pthread_mutex_timedlock(&mVideoLock, &ts);*/
	AVSyncRtpData * ptr, *ptr1 = 0;
	unsigned short rtpSeq = 0;
	unsigned long  rtpStp = 0;
	unsigned long  curTime = 0;
	struct timespec  sys_time = {0,0};
	RTP_FIXED_HEADER * header;
	if(len < FIXRTPHEADLEN || NULL == data) {
		return -1;
	}
	if(!mRecvReady) {
		return -1;
	}
	header = (RTP_FIXED_HEADER *)data;
	rtpSeq = ntohs(header->seq_no);
#ifdef __MACH__
    rtpStp = 0;
    rtpStp = rtpStp | (data[4] << 24);
    rtpStp = rtpStp | (data[5] << 16);
    rtpStp = rtpStp | (data[6] << 8);
    rtpStp = rtpStp | (data[7]);
#else
    rtpStp = ntohl(header->timestamp);
#endif
	LOGI("video date stp=%u\n", ntohl(header->timestamp));
    LOGE("video date=%x-%x-%x-%x\n", data[4], data[5], data[6], data[7]);
	clock_gettime(CLOCK_MONOTONIC,&sys_time);
	curTime = sys_time.tv_sec * 1000 + sys_time.tv_nsec/1000000; //ms
	//LOGI("cur time=%d\n", curTime);
	AVSyncRtpData * rtpData = newRtpData(data, len, exData, exLen);
	if(NULL == rtpData) {
		return -1;
	}
	rtpData->mSeqNum = rtpSeq; //序号
	if(data[1] & 0x80){
		rtpData->mMarker = true;
	} else {
		rtpData->mMarker = false;
	}
    LOGE("video date seq=%d, stp=%ld, marker=%d\n", rtpSeq, rtpStp, rtpData->mMarker);
	///////////  锁
	if(0 == pthread_mutex_lock(&mVideoLock)) {
		if(0 == mSysStartTm) { //可能在put audio执行
			mSysStartTm = curTime + BUFFERTIME;
			//mAVStartTm = curTime;
		}
		if(0 == mVideoStartStp) {//为什么需要这个判断，因为上个判断不一定在这个函数执行
			//第一个视频包
			mVideoStartStp = rtpStp;
		} else if(rtpStp < mVideoStartStp) {
			mSysStartTm = curTime + BUFFERTIME;
			//mAVStartTm = curTime;
			mVideoStartStp = rtpStp;
			mAudioStartStp = 0;
		}
		rtpData->mRtpStp = /*mAVStartTm*/mSysStartTm + (rtpStp - mVideoStartStp)/VIDEOSAMPLE; //时间戳
		// 放入队列
		if(NULL == mVideoListHead) {
			mVideoListHead = mVideoListTail = rtpData;
			//LOGE("first");
		} else {
			//按照seq排序，倒着排，大部分后来的，序号靠后
			ptr = mVideoListTail;
			while(ptr != NULL) {
				if(ptr->mSeqNum < rtpData->mSeqNum){
					//从后往前，所以这个直接入队即可。
					ptr1 = ptr->next;
					ptr->next = rtpData;
					rtpData->prev = ptr;
					rtpData->next = ptr1;
					if(NULL != ptr1) {
						ptr1->prev = rtpData;
						LOGD("video insert middle\n");
					} else {
						//rtp data is the tail
						mVideoListTail = rtpData;
						//LOGD("insert tail");
					}
					break;
				}
				//继续往前找
				ptr = ptr->prev;
			}
			// 最后需要check一下是否在队列前面
			if(mVideoListHead->mSeqNum > rtpData->mSeqNum) {
				rtpData->next = mVideoListHead;
				mVideoListHead->prev = rtpData;
				mVideoListHead = rtpData;
				//LOGE("insert head");
			}
		}
		//////  解锁
		pthread_mutex_unlock(&mVideoLock);
		// 给信号到线程
#ifdef __MACH__
        sem_post(mSyncSem);
#else
        sem_post(&mSyncSem);
#endif
	}
	return 0;
}
// 同视频
int AVSyncer::putAudioData(unsigned char * data, int len, unsigned char * exData, int exLen){
	//sem_post(&mSyncSem);
	AVSyncRtpData * ptr, *ptr1 = 0;
	unsigned short rtpSeq = 0;
	unsigned long  rtpStp = 0;
	unsigned long  curTime = 0;
	struct timespec  sys_time = {0,0};
	RTP_FIXED_HEADER * header;
	//LOGE("date len=%d", len);
	if(len < FIXRTPHEADLEN || NULL == data) {
		return -1;
	}
	if(!mRecvReady) {
		return -1;
	}
	header = (RTP_FIXED_HEADER *)data;
	rtpSeq = ntohs(header->seq_no);
	//rtpStp = ntohl(header->timestamp);
#ifdef __MACH__
    rtpStp = 0;
    rtpStp = rtpStp | (data[4] << 24);
    rtpStp = rtpStp | (data[5] << 16);
    rtpStp = rtpStp | (data[6] << 8);
    rtpStp = rtpStp | (data[7]);
#else
    rtpStp = ntohl(header->timestamp);
#endif
	LOGE("date=%x-%x\n", data[0], data[1]);
	clock_gettime(CLOCK_MONOTONIC,&sys_time);
	curTime = sys_time.tv_sec * 1000 + sys_time.tv_nsec/1000000; //ms
	AVSyncRtpData * rtpData = newRtpData(data, len, exData, exLen);
	if(NULL == rtpData) {
		return -1;
	}
	rtpData->mSeqNum = rtpSeq; //序号
    rtpData->mMarker = true;
    LOGE("date seq=%d, stp=%ld, marker=%d\n", rtpSeq, rtpStp, rtpData->mMarker);
	///////////  锁
	if(0 == pthread_mutex_lock(&mAudioLock)) {
		if(0 == mSysStartTm) {//可能在put video执行
			mSysStartTm = curTime + BUFFERTIME;
			//mAVStartTm = curTime;
		}
		if(0 == mAudioStartStp) {
			//第一个音频包
			mAudioStartStp = rtpStp;
		} else if(rtpStp < mAudioStartStp) {
			mSysStartTm = curTime + BUFFERTIME;
			//mAVStartTm = curTime;
			mAudioStartStp = rtpStp;
			mVideoStartStp = 0;
		}
		rtpData->mRtpStp = /*mAVStartTm*/mSysStartTm + (rtpStp - mAudioStartStp)/VIDEOSAMPLE; //时间戳
		// 放入队列
		if(NULL == mAudioListHead) {
			mAudioListHead = mAudioListTail = rtpData;
		} else {
			//按照seq排序，倒着排，大部分后来的，序号靠后
			ptr = mAudioListTail;
			while(ptr != NULL) {
				if(ptr->mSeqNum < rtpData->mSeqNum){
					//从后往前，所以这个直接入队即可。
					ptr1 = ptr->next;
					ptr->next = rtpData;
					rtpData->prev = ptr;
					rtpData->next = ptr1;
					if(NULL != ptr1) {
						ptr1->prev = rtpData;
                        LOGD("audio insert middle\n");
					} else {
						//rtp data is the tail
						mAudioListTail = rtpData;
					}
					break;
				}
				//继续往前找
				ptr = ptr->prev;
			}
			// 最后需要check一下是否在队列前面
			if(mAudioListHead->mSeqNum > rtpData->mSeqNum) {
				rtpData->next = mAudioListHead;
				mAudioListHead->prev = rtpData;
				mAudioListHead = rtpData;
			}
		}
		//////  解锁
		pthread_mutex_unlock(&mAudioLock);
		// 给信号到线程
#ifdef __MACH__
        sem_post(mSyncSem);
#else
        sem_post(&mSyncSem);
#endif
	}
	return 0;
}
int AVSyncer::setCallBack(void * cb){
	mCallBack = (StreamDataCallback)cb;
	return 0;
}

/*int main() {
	AVSyncer test;
	usleep(1000000);
	test.putVideoData(0, 10);
	usleep(1000000);
	test.putVideoData(0, 10);
	usleep(10000000);
	AVSyncer *test = new AVSyncer();
	usleep(1000000);
	test->putVideoData(0, 10);
	usleep(1000000);
	test->putVideoData(0, 10);
	usleep(10000000);
	delete test;
	return 0;
}*/

//  JNI测试使用，
/*************      JNI 全局变量        *****************/
#if 0
JavaVM *   g_CachedJvm;
//JNIEnv  *    g_CachedEvn;
AVSyncer * parseTest;
unsigned  char * g_DataArray;

// 假定rtp 包不会超过1500B
#define RTPRECVBUFLEN  (1500)

JNICALL void parseRtpPacket(JNIEnv * pEnv, jclass jClass, jbyteArray rtpData, jint dataLen) {
	int len;
	JNIEnv* env = NULL;

	if(g_CachedJvm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
		return;

	if(dataLen > RTPRECVBUFLEN) {
		len = RTPRECVBUFLEN;
	} else {
		len = dataLen;
	}

	env->GetByteArrayRegion(rtpData, 0, len, (jbyte*)(g_DataArray));
	//RTP_FIXED_HEADER * header = (RTP_FIXED_HEADER *)g_DataArray;
	//LOGE("date seq=%d\n, stp=%ld",
	//	ntohs(header->seq_no), ntohl(header->timestamp));
	parseTest->putVideoData(g_DataArray, len);
}


/******************  注册native函数  *******************/

static const JNINativeMethod gMethods[] = { //定义批量注册的数组，是注册的关键部分
		{"parseRtpPacket", "([BI)V", (void*)parseRtpPacket}
};

//这是JNI_OnLoad的声明，必须按照这样的方式声明
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void *reserved)
{
    JNIEnv* env = NULL; //注册时在JNIEnv中实现的，所以必须首先获取它
    jint result = -1;

    LOGE("JNI_OnLoad");
    //从JavaVM获取JNIEnv，一般使用1.4的版本
    if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
        return -1;
    //env->GetJavaVM(&(g_CachedJvm));
    //g_CachedEvn = env;//reinterpret_cast<JNIEnv>(env->NewGlobalRef(env));
    g_CachedJvm = vm;//reinterpret_cast<JavaVM*>(vm);

    jclass clazz;
    static const char* const kClassName="com/zws/nati/AVSyncNative";

    //这里可以找到要注册的类，前提是这个类已经加载到java虚拟机中。
    clazz = env->FindClass(kClassName);

    if(clazz == NULL)
    {
    	LOGE("cannot get class:%s\n", kClassName);
        return -1;
    }

    if(env->RegisterNatives(clazz, gMethods, sizeof(gMethods)/sizeof(gMethods[0]))!= JNI_OK)
    {
    	LOGE("register native method failed!\n");
        return -1;
    }

    parseTest = new AVSyncer();
    g_DataArray = new unsigned char[RTPRECVBUFLEN];

    return JNI_VERSION_1_4; //这里很重要，必须返回版本，否则加载会失败。
}
JNIEXPORT void JNI_OnUnload(JavaVM* vm, void* reserved) {
	LOGE("library was unload");
	JNIEnv* env = NULL;
	if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
		return;
	//env->DeleteGlobalRef(g_CachedJvm);
	delete parseTest;
	delete g_DataArray;
	parseTest = NULL;
}
#endif
