//
//  HonH264Decoder.cpp
//  NvrSdkDemo
//
//  Created by ted_mac on 10/14/14.
//  Copyright (c) 2014 HSG. All rights reserved.
//

#include "HonH264Decoder.h"
#include "jni.h"
#include <android/log.h>

//sem_t HonH264Decoder::semLock = sem_init(&semLock,0,1);
//int HonH264Decoder::semInit;

HonH264Decoder::HonH264Decoder()
{
    m_codec = NULL;			  // Codec
    m_ctx = NULL;		  // Codec Context
    m_picture = NULL;		  // Frame
    m_frameCnt = 0;
    m_width = 0;
    m_height = 0;
    m_size = 0;
    m_comsumedSize = 0;
    m_gotPic = 0;
    m_showDebug = 0;
    //sem_init(&semLock, 0, 1);
    //pthread_mutex_init(&mutex, NULL);
    initDecoder();
}

HonH264Decoder::~HonH264Decoder()
{
	__android_log_print(ANDROID_LOG_WARN, "rtsp_client", "before destructor\n");
	//should lock before delete, put here is too late
	//pthread_mutex_lock(&mutex);
    if(m_ctx)
    {
        avcodec_close(m_ctx);
        m_ctx = NULL;
    }
    if(m_picture)
    {
        free(m_picture);
        m_picture = NULL;
    }
    
    av_free_packet(&m_avpkt);
    //pthread_mutex_unlock(&mutex);
    __android_log_print(ANDROID_LOG_WARN, "rtsp_client", "end destructor\n");
    //pthread_mutex_destroy(&mutex);
    
}

//void HonH264Decoder::initDecoderLock()
//{if(!semInit) sem_init(&semLock,0,1);}


void HonH264Decoder::lockDecoder()
{/*sem_wait(&semLock);*/}

void HonH264Decoder::unlockDecoder()
{/*sem_post(&semLock);*/}



int HonH264Decoder::initDecoder()
{
    //initDecoderLock();
	//pthread_mutex_lock(&mutex);
	avcodec_register_all();
    av_init_packet(&m_avpkt);
    m_codec = avcodec_find_decoder(AV_CODEC_ID_H264);
    if (!m_codec)
    {
    	//pthread_mutex_unlock(&mutex);
        printf("H264 Codec not found\n");
        return -1;
    }
    m_codec->type = AVMEDIA_TYPE_VIDEO;
    m_codec->id = AV_CODEC_ID_H264;
    
    m_codec->capabilities |= CODEC_CAP_DELAY;
    
    //create context
    m_ctx = avcodec_alloc_context3(m_codec);
    if (!m_ctx)
    {
    	//pthread_mutex_unlock(&mutex);
        printf("create context failed!\n");
        return -1;
    }
    m_ctx->pix_fmt = PIX_FMT_YUV420P;
    m_ctx->codec_type = AVMEDIA_TYPE_VIDEO;
    m_ctx->bit_rate = 0;
    m_ctx->debug_mv = 0;
    m_ctx->debug = 0;
    m_ctx->workaround_bugs = 0;
    m_ctx->lowres = 0;
    m_ctx->idct_algo = FF_IDCT_AUTO;
    m_ctx->skip_frame = AVDISCARD_DEFAULT;
    m_ctx->skip_idct = AVDISCARD_DEFAULT;
    m_ctx->skip_loop_filter = AVDISCARD_DEFAULT;
    m_ctx->codec_id = AV_CODEC_ID_H264;
    m_ctx->width = 0;
    m_ctx->height = 0;
    m_ctx->thread_count = 2;
    m_ctx->thread_type = 0;
    m_ctx->thread_type |= FF_THREAD_SLICE;
    m_ctx->thread_type |= FF_THREAD_FRAME;
    m_ctx->flags = 0;
    m_ctx->flags2 |= CODEC_FLAG2_FAST;
    
    if (avcodec_open2(m_ctx, m_codec, NULL) < 0)
    {
    	//pthread_mutex_unlock(&mutex);
        printf("Could not open codec\n");
        return -1;
    }
    
    m_picture = avcodec_alloc_frame();
    if (!m_picture)
    {
    	//pthread_mutex_unlock(&mutex);
        printf("Could not allocate video frame\n");
        return -1;
    }
    //pthread_mutex_unlock(&mutex);
    
    m_frameCnt = 0;
    
    return 0;
}

int HonH264Decoder::getWidth()
{
    return m_width;
}

int HonH264Decoder::getHeight()
{
    return m_height;
}

struct AVFrame * HonH264Decoder::getDecodedFrame()
{
    return m_picture;
}



uint8_t* HonH264Decoder::copyDecodedYBytes()
{
    return copyFrameBytes(m_picture->data[0],
                         m_picture->linesize[0],
                         m_width,
                         m_height);
}

uint8_t* HonH264Decoder::copyDecodedUBytes()
{
    return copyFrameBytes(m_picture->data[1],
                          m_picture->linesize[1],
                          m_width/2,
                          m_height/2);
}

uint8_t* HonH264Decoder::copyDecodedVBytes()
{
    return copyFrameBytes(m_picture->data[2],
                          m_picture->linesize[2],
                          m_width/2,
                          m_height/2);
}

/*******************  raw data from decoder ********************/
uint8_t* HonH264Decoder::getYDataPtr()
{
	return m_picture->data[0];
}
uint8_t* HonH264Decoder::getUDataPtr()
{
	return m_picture->data[1];
}
uint8_t* HonH264Decoder::getVDataPtr()
{
	return m_picture->data[2];
}
int HonH264Decoder::getYLinesize()
{
	return m_picture->linesize[0];
}
int HonH264Decoder::getULinesize()
{
	return m_picture->linesize[1];
}
int HonH264Decoder::getVLinesize()
{
	return m_picture->linesize[2];
}
/***************************************************************/

uint8_t* HonH264Decoder::copyFrameBytes(uint8_t *src, int linesize, int width, int height)
{
    int i;
    width = linesize < width ? linesize:width;//MIN(linesize, width);
    uint8_t *data = (uint8_t *)malloc(width * height);
    uint8_t *dst = 0;
    dst = data;
    for (i = 0; i < height; ++i) {
        memcpy(dst, src, width);
        dst += width;
        src += linesize;
    }
    return data;
}

int HonH264Decoder::decodeframe(uint8_t *dataBuf, unsigned int dataSize)
{
    if((!dataBuf)||(!dataSize))
    {
        printf("Invalid data to decode!\n");
        return -1;
    }
    //pthread_mutex_lock(&mutex);
    AVPacket avpkt;
    av_init_packet(&avpkt);
  
    avpkt.size = dataSize;
    avpkt.data = dataBuf;
    
    //lockDecoder();
    //av_frame_unref(m_picture);
    //avcodec_get_frame_defaults(m_picture);
    m_comsumedSize = avcodec_decode_video2(m_ctx, m_picture, &m_gotPic, &avpkt);
    
    //unlockDecoder();
    
    av_free_packet(&avpkt);
    //pthread_mutex_unlock(&mutex);
    
    if (m_comsumedSize < 0)
    {
        printf("decode failed!\n");
        return -1;
    }

    if (m_gotPic)
    {
        /* the picture is allocated by the decoder. no need to
         free it */
        m_width = m_ctx->width;
        m_height = m_ctx->height;
        
        if(!m_showDebug)
        {
            printf("resolution:%u * %u \n",m_width,m_height);
            m_showDebug = 1;
        }
        
        m_frameCnt++;
        return 0;
    }

    return -1;
}

void HonH264Decoder::setUserData(void *upper)
{
    m_usrData = upper;
}

void * HonH264Decoder::getUserData()
{
    return m_usrData;
}

