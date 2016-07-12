/*
 * AVSyncApi.hh
 *
 *  Created on: Nov 18, 2015
 *      Author: e562450
 */

#include <errno.h>
#include <unistd.h>
#include <semaphore.h>
#include <pthread.h>
#include <sys/time.h>
#include <time.h>

#ifdef __MACH__
#include <mach/mach_time.h>
#define CLOCK_REALTIME 0
#define CLOCK_MONOTONIC 0
int clock_gettime(int clk_id, struct timespec *t);
int sem_timedwait(sem_t *sem, const struct timespec *abs_timeout);
#endif

#ifndef AVSYNCAPI_HH_
#define AVSYNCAPI_HH_

// call back, type:0->video,1->audio
typedef int (* StreamDataCallback)(unsigned char * pBuf, int size,
		int type, int timestamp);

typedef struct _AVSyncRtpData_ {
	unsigned short mSeqNum; // sequence number
	unsigned long  mRtpStp; // time stamp
	int  mDataLen; // rtp data len
	int  mExDataLen; // extra data len, ex. uuid
	bool mMarker; // mark bit
	unsigned char * mAVData;
	unsigned char * mExData; // ex. uuid
	//void setRtpData();
	struct _AVSyncRtpData_ * next;
	struct _AVSyncRtpData_ * prev;
}AVSyncRtpData;

typedef struct _hw_rtp_unpack_264 {
	unsigned char* memory_buf;
	unsigned int write_pos;
	unsigned int frame_len;
} hw_rtp_unpack_264;


class AVSyncer{
private:
	// 双向队列
	AVSyncRtpData * mVideoListHead;
	AVSyncRtpData * mVideoListTail;
	AVSyncRtpData * mAudioListHead;
	AVSyncRtpData * mAudioListTail;
	pthread_mutex_t mVideoLock;
	pthread_mutex_t mAudioLock;
	// ios不允许使用无名信号量
#ifdef __MACH__
	sem_t         * mSyncSem; //信号量
#else
	sem_t           mSyncSem; //信号量
#endif

	volatile  bool  mNeedSync;// 是否同步，false只排序，不同步。//当前无效，始终同步。
	volatile  bool  mRecvReady; //是否可以接收数据了，因为putdata跟其他操作可能不在一个线程。
								  //析构的时候，释放资源，不允许再put数据了。
	unsigned short  mNextVideoSeq;
	unsigned short  mNextAudioSeq;
	//音视频同步使用时间计算
	unsigned long mSysStartTm;   //同步，跟系统时间对比, 收到数据包就开始
	//unsigned long mAVStartTm;    //同步AV到一个时间，然后跟系统时间对比
	unsigned long mVideoStartStp; //计算stp
	unsigned long mAudioStartStp; //计算stp
	//unsigned long mVideoCurTm; //计算stp, 当前视频
	//unsigned long mAudioCurTm; //计算stp, 当前音频

	// thread
	pthread_t     mThread;
	volatile bool mThdRun;

	// 组帧
	hw_rtp_unpack_264   mMediaInfo;

	// data call back for nal and audio
	StreamDataCallback mCallBack;

	//检查mark位
	//bool checkMark(unsigned long stp);
	// rtp data
	AVSyncRtpData * newRtpData(unsigned char * data, int len, unsigned char * exData, int exLen);
	int deleteRtpData(AVSyncRtpData * rtp);
	// 组帧
	int unpackH264(unsigned char* stream, unsigned int stream_len);
	int unpackPCM(unsigned char* stream, unsigned int stream_len);
	int resetRtpUnpack();
	int unpackSingleNalu(unsigned char* stream, unsigned int stream_len);
	int unpackStapaNalu(unsigned char* stream, unsigned int stream_len, unsigned int& nalu_type);
	int unpackFuaNalu(unsigned char* stream, unsigned int stream_len);

public:
	AVSyncer();
	AVSyncer(bool bSync);
	AVSyncer(void * cb, bool bSync = false);
	~AVSyncer();
	// put video and autio data
	//int putVideoData(unsigned char * data, int len); // rtp data with head
	int putVideoData(unsigned char * data, int len, unsigned char * exData, int exLen); // rtp data with head
	//int putAudioData(unsigned char * data, int len); // rtp data with head
	int putAudioData(unsigned char * data, int len, unsigned char * exData, int exLen); // rtp data with head
	void ThdRun();
	int setCallBack(void * cb);
};


#endif /* AVSYNCAPI_HH_ */
