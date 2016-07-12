//
//  HonH264Decoder.h
//  NvrSdkDemo
//
//  Created by ted_mac on 10/14/14.
//  Copyright (c) 2014 HSG. All rights reserved.
//

#ifndef __NvrSdkDemo__HonH264Decoder__
#define __NvrSdkDemo__HonH264Decoder__

#include <iostream>
//#include "libavutil/opt.h"
#ifdef __cplusplus
extern "C" {
#endif
#include "../lib_ffmpeg/include/libavcodec/avcodec.h"
//#include "libavutil/channel_layout.h"
//#include "libavutil/common.h"
//#include "libavutil/imgutils.h"
//#include "libavutil/mathematics.h"
//#include "libavutil/samplefmt.h"
//#include "libswscale/swscale.h"
//#include <semaphore.h>
#ifdef __cplusplus
}
#endif

class HonH264Decoder
{
public:
    HonH264Decoder();
    virtual ~HonH264Decoder();
    
public:
    
//    static void initDecoderLock();
    static void lockDecoder();
    static void unlockDecoder();
//    static sem_t semLock;
//    static int   semInit;
    //pthread_mutex_t mutex;
    
public:
    int initDecoder();          //0:success
    int decodeframe(uint8_t *dataBuf, unsigned int dataSize);   //0:sucess
    int getWidth();
    int getHeight();
    
    int getYLinesize();
    int getULinesize();
    int getVLinesize();

    struct AVFrame * getDecodedFrame();     //in yuv420 formate
    uint8_t* copyDecodedYBytes();
    uint8_t* copyDecodedUBytes();
    uint8_t* copyDecodedVBytes();
    
    uint8_t* getYDataPtr();
    uint8_t* getUDataPtr();
    uint8_t* getVDataPtr();

    void setUserData(void *upper);
    void *getUserData();
    


    
    
private:
    uint8_t *copyFrameBytes(uint8_t *src, int linesize, int width, int height);
    
private:
    struct AVCodec *m_codec;			  // Codec
    struct AVCodecContext *m_ctx;		  // Codec Context
    struct AVFrame *m_picture;		  // Frame
    int m_frameCnt;
    AVPacket m_avpkt;                     //packet used in decoding
    int m_width;
    int m_height;
    int m_size;
    int m_comsumedSize;
    int m_gotPic;
    int m_showDebug;
    void *m_usrData;
    
};


#endif /* defined(__NvrSdkDemo__HonH264Decoder__) */
