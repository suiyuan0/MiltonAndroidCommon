/**
* @file SPS.cpp 
* @brief H.264 SPS decode function implement.
* @author Lonsn
* @date 05/22/2012
* @version 0.1
*
* Detailed description for SPS.cpp 
* Decode the SPS define in H.264, Borrow from Aptus project
*/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <math.h>

#include "SPS.h"

static const uint8_t log2_tab[256]={
        0,0,1,1,2,2,2,2,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
        5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
        6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
        6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
        7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
        7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
        7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
        7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7
};

static const uint8_t zigzag_scan[16]={
 0+0*4, 1+0*4, 0+1*4, 0+2*4,
 1+1*4, 2+0*4, 3+0*4, 2+1*4,
 1+2*4, 0+3*4, 1+3*4, 2+2*4,
 3+1*4, 3+2*4, 2+3*4, 3+3*4,
};

static const uint8_t ff_zigzag_direct[64] = {
    0,   1,  8, 16,  9,  2,  3, 10,
    17, 24, 32, 25, 18, 11,  4,  5,
    12, 19, 26, 33, 40, 48, 41, 34,
    27, 20, 13,  6,  7, 14, 21, 28,
    35, 42, 49, 56, 57, 50, 43, 36,
    29, 22, 15, 23, 30, 37, 44, 51,
    58, 59, 52, 45, 38, 31, 39, 46,
    53, 60, 61, 54, 47, 55, 62, 63
};

static const uint8_t default_scaling4[2][16]={
{   6,13,20,28,
   13,20,28,32,
   20,28,32,37,
   28,32,37,42
},{
   10,14,20,24,
   14,20,24,27,
   20,24,27,30,
   24,27,30,34
}};

static const uint8_t default_scaling8[2][64]={
{   6,10,13,16,18,23,25,27,
   10,11,16,18,23,25,27,29,
   13,16,18,23,25,27,29,31,
   16,18,23,25,27,29,31,33,
   18,23,25,27,29,31,33,36,
   23,25,27,29,31,33,36,38,
   25,27,29,31,33,36,38,40,
   27,29,31,33,36,38,40,42
},{
    9,13,15,17,19,21,22,24,
   13,13,17,19,21,22,24,25,
   15,17,19,21,22,24,25,27,
   17,19,21,22,24,25,27,28,
   19,21,22,24,25,27,28,30,
   21,22,24,25,27,28,30,32,
   22,24,25,27,28,30,32,33,
   24,25,27,28,30,32,33,35
}};


static const AVRational pixel_aspect[17]={
 {0, 1},
 {1, 1},
 {12, 11},
 {10, 11},
 {16, 11},
 {40, 33},
 {24, 11},
 {20, 11},
 {32, 11},
 {80, 33},
 {18, 11},
 {15, 11},
 {64, 33},
 {160,99},
 {4, 3},
 {3, 2},
 {2, 1},
};

static const uint8_t golomb_vlc_len[512]={
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,
7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
};

static const uint8_t ue_golomb_vlc_code[512]={
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,
 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9,10,10,10,10,11,11,11,11,12,12,12,12,13,13,13,13,14,14,14,14,
 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
};

static const int8_t se_golomb_vlc_code[512]={
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  8, -8,  9, -9, 10,-10, 11,-11, 12,-12, 13,-13, 14,-14, 15,-15,
  4,  4,  4,  4, -4, -4, -4, -4,  5,  5,  5,  5, -5, -5, -5, -5,  6,  6,  6,  6, -6, -6, -6, -6,  7,  7,  7,  7, -7, -7, -7, -7,
  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3,
  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
};


static inline int av_log2(unsigned int v)
{
    int n = 0;
    if (v & 0xffff0000) {
        v >>= 16;
        n += 16;
    }
    if (v & 0xff00) {
        v >>= 8;
        n += 8;
    }
    n += log2_tab[v];

    return n;
}

static void skip_bits(GetBitContext *s, int n){
	s->index += n;
}

static void skip_bits1(GetBitContext *s){
	s->index++;
}

static unsigned int get_bits1(GetBitContext *s){
	int index= s->index;    

	uint8_t result;    
    result= s->buffer[ index>>3 ];
    result<<= (index&0x07);
    result>>= 8 - 1;

    s->index++;

    return result;
}

static unsigned int get_ue_golomb(GetBitContext *gb){
    unsigned int buf;
    int log;
	unsigned int re_index, re_cache;

    //OPEN_READER(re, gb);
	re_index = gb->index;
	re_cache = 0;
	
    //UPDATE_CACHE(re, gb);
	re_cache = AV_RB32(((const uint8_t *)gb->buffer)+(re_index>>3) ) << (re_index&0x07);
	
   // buf=GET_CACHE(re, gb);
	buf = re_cache;

    if(buf >= (1<<27)){
        buf >>= 32 - 9;
        LAST_SKIP_BITS(re, gb, golomb_vlc_len[buf]);
        CLOSE_READER(re, gb);

        return ue_golomb_vlc_code[buf];
    }else{
        log= 2*av_log2(buf) - 31;
        buf>>= log;
        buf--;
        LAST_SKIP_BITS(re, gb, 32 - log);
        CLOSE_READER(re, gb);

        return buf;
    }
}
static inline int get_se_golomb(GetBitContext *gb){
    unsigned int buf;
    int log;

    OPEN_READER(re, gb);
    UPDATE_CACHE(re, gb);
    buf=GET_CACHE(re, gb);

    if(buf >= (1<<27)){
        buf >>= 32 - 9;
        LAST_SKIP_BITS(re, gb, golomb_vlc_len[buf]);
        CLOSE_READER(re, gb);

        return se_golomb_vlc_code[buf];
    }else{
        log= 2*av_log2(buf) - 31;
        buf>>= log;

        LAST_SKIP_BITS(re, gb, 32 - log);
        CLOSE_READER(re, gb);

        if(buf&1) buf= -(buf>>1);
        else      buf=  (buf>>1);

        return buf;
    }
}

static int get_ue_golomb_31(GetBitContext *gb){
    unsigned int buf;

	int re_index= gb->index;
    int re_cache= 0;

	re_cache= AV_RB32( ((const uint8_t *)gb->buffer)+(re_index>>3) ) << (re_index&0x07);

	buf = ( (uint32_t)re_cache ) >> ( 32 - 9 );    

	re_index += golomb_vlc_len[buf];

	gb->index= re_index;

    return ue_golomb_vlc_code[buf];
}

static unsigned int get_bits(GetBitContext *s, int n){
    int tmp;

    int re_index = s->index;
    int re_cache;

    re_cache= AV_RB32( ((const uint8_t *)s->buffer)+(re_index>>3) ) << (re_index&0x07);
    tmp = (((uint32_t)(re_cache))>>(32-(n)));
    s->index += n;

    return tmp;
}

static unsigned int get_bits_long(GetBitContext *s, int n){
    if(n<=17) return get_bits(s, n);
    else{
        int ret= get_bits(s, 16) << (n-16);
        return ret | get_bits(s, n-16);
    }
}
static void decode_scaling_list(GetBitContext *gb, uint8_t *factors, int size,
                                const uint8_t *jvt_list, const uint8_t *fallback_list){
    int i, last = 8, next = 8;
    const uint8_t *scan = size == 16 ? zigzag_scan : ff_zigzag_direct;
    if(!get_bits1(gb)) // matrix not written, we use the predicted one 
        memcpy(factors, fallback_list, size*sizeof(uint8_t));
    else
    for(i=0;i<size;i++){
        if(next)
            next = (last + get_se_golomb(gb)) & 0xff;
        if(!i && !next){ // matrix not written, we use the preset one 
            memcpy(factors, jvt_list, size*sizeof(uint8_t));
            break;
        }
        last = factors[scan[i]] = next ? next : last;
    }
}

static void decode_scaling_matrices(GetBitContext *gb, SPS *sps, int is_sps,
                                   uint8_t (*scaling_matrix4)[16], uint8_t (*scaling_matrix8)[64]){
    int fallback_sps = !is_sps && sps->scaling_matrix_present;
    const uint8_t *fallback[4] = {
        fallback_sps ? sps->scaling_matrix4[0] : default_scaling4[0],
        fallback_sps ? sps->scaling_matrix4[3] : default_scaling4[1],
        fallback_sps ? sps->scaling_matrix8[0] : default_scaling8[0],
        fallback_sps ? sps->scaling_matrix8[1] : default_scaling8[1]
    };
    if(get_bits1(gb)){
        sps->scaling_matrix_present |= is_sps;
        decode_scaling_list(gb,scaling_matrix4[0],16,default_scaling4[0],fallback[0]); // Intra, Y
        decode_scaling_list(gb,scaling_matrix4[1],16,default_scaling4[0],scaling_matrix4[0]); // Intra, Cr
        decode_scaling_list(gb,scaling_matrix4[2],16,default_scaling4[0],scaling_matrix4[1]); // Intra, Cb
        decode_scaling_list(gb,scaling_matrix4[3],16,default_scaling4[1],fallback[1]); // Inter, Y
        decode_scaling_list(gb,scaling_matrix4[4],16,default_scaling4[1],scaling_matrix4[3]); // Inter, Cr
        decode_scaling_list(gb,scaling_matrix4[5],16,default_scaling4[1],scaling_matrix4[4]); // Inter, Cb
        if(is_sps){
            decode_scaling_list(gb,scaling_matrix8[0],64,default_scaling8[0],fallback[2]);  // Intra, Y
            decode_scaling_list(gb,scaling_matrix8[1],64,default_scaling8[1],fallback[3]);  // Inter, Y
        }
    }
}

static int avcodec_check_dimensions(unsigned int w, unsigned int h){
    if((int)w>0 && (int)h>0 && (w+128)*(uint64_t)(h+128) < INT_MAX/4)
        return 0;

    return -1;
}
static int decode_hrd_parameters(GetBitContext *gb, SPS *sps){
    int cpb_count, i;
    cpb_count = get_ue_golomb_31(gb) + 1;

    if(cpb_count > 32U)
		return -1;

    skip_bits(gb,8);       	

    for(i=0; i<cpb_count; i++){
        int cxm1 = get_ue_golomb(gb); // bit_rate_value_minus1 
        int cxm2 = get_ue_golomb(gb); // cpb_size_value_minus1 

        skip_bits1(gb);       	
    }
    sps->initial_cpb_removal_delay_length = get_bits(gb, 5) + 1;
    sps->cpb_removal_delay_length = get_bits(gb, 5) + 1;
    sps->dpb_output_delay_length = get_bits(gb, 5) + 1;
    sps->time_offset_length = get_bits(gb, 5);
    sps->cpb_cnt = cpb_count;
    return 0;
}

static inline int decode_vui_parameters(GetBitContext *gb, SPS *sps){
    int aspect_ratio_info_present_flag;
    unsigned int aspect_ratio_idc;

    aspect_ratio_info_present_flag= get_bits1(gb);

    if( aspect_ratio_info_present_flag ) {
        aspect_ratio_idc= get_bits(gb, 8);
        if( aspect_ratio_idc == 255 ) {
            sps->sar.num= get_bits(gb, 16);
            sps->sar.den= get_bits(gb, 16);
        }else if(aspect_ratio_idc < sizeof(pixel_aspect)/sizeof(*pixel_aspect)){
            sps->sar=  pixel_aspect[aspect_ratio_idc];
        }else{
            return -1;
        }
    }else{
        sps->sar.num = 0;
        sps->sar.den= 0;
    }

    if(get_bits1(gb))
		skip_bits1(gb);       

    if(get_bits1(gb)){      // video_signal_type_present_flag 
        skip_bits(gb, 4);    	
     
        if(get_bits1(gb))
            skip_bits(gb, 24);
    }

    if(get_bits1(gb)){      // chroma_location_info_present_flag 
        get_ue_golomb(gb);  // chroma_sample_location_type_top_field 
        get_ue_golomb(gb);  // chroma_sample_location_type_bottom_field 
    }

    sps->timing_info_present_flag = get_bits1(gb);
    if(sps->timing_info_present_flag){
        sps->num_units_in_tick = get_bits_long(gb, 32);
        sps->time_scale = get_bits_long(gb, 32);
        sps->fixed_frame_rate_flag = get_bits1(gb);
    }

#if 0
    sps->nal_hrd_parameters_present_flag = get_bits1(gb);
    if(sps->nal_hrd_parameters_present_flag)
        if(decode_hrd_parameters(gb, sps) < 0)
            return -1;
    sps->vcl_hrd_parameters_present_flag = get_bits1(gb);
    if(sps->vcl_hrd_parameters_present_flag)
        if(decode_hrd_parameters(gb, sps) < 0)
            return -1;
    if(sps->nal_hrd_parameters_present_flag || sps->vcl_hrd_parameters_present_flag)
        get_bits1(gb);     // low_delay_hrd_flag 
    sps->pic_struct_present_flag = get_bits1(gb);

    sps->bitstream_restriction_flag = get_bits1(gb);
    if(sps->bitstream_restriction_flag){
        get_bits1(gb);     // motion_vectors_over_pic_boundaries_flag 
        get_ue_golomb(gb); // max_bytes_per_pic_denom 
        get_ue_golomb(gb); // max_bits_per_mb_denom 
        get_ue_golomb(gb); // log2_max_mv_length_horizontal 
        get_ue_golomb(gb); // log2_max_mv_length_vertical 
        sps->num_reorder_frames= get_ue_golomb(gb);
        get_ue_golomb(gb); //max_dec_frame_buffering

        if(sps->num_reorder_frames > 16U )
			return -1;
        
    }
#endif 
    return 0;
}

/*-----------------------------------------------------------------------------
 * @Name:
 *   decode_seq_parameter_set
 *
 * @Description:
 *   decode raw sps data to a sps struct.
 *   To be optimized since we only need resolution information.
 * @Paramters:
 *   sps_raw_data: sps input data.
 *   sps: sps struct output
 * @Return values:
 *   0: succeed.
 *   -1: failed
 *-----------------------------------------------------------------------------
 */
int decode_seq_parameter_set(const uint8_t* sps_raw_data, SPS *sps)
{
	GetBitContext gb_context;
	memset(&gb_context, 0, sizeof(GetBitContext));
	GetBitContext *gb = &gb_context;
	gb->buffer = (const uint8_t*)sps_raw_data;
	gb->buffer_end = NULL;
	gb->index = 0;

    if (NULL == sps_raw_data || NULL == sps)
    {
        return -1;
    }
    
	int i;
	int sps_id;

	sps->profile_idc = get_bits(gb, 8);

	gb->index += 8;

	sps->level_idc = get_bits(gb, 8);
	sps_id = get_ue_golomb_31(gb);

	memset(sps->scaling_matrix4, 16, sizeof(sps->scaling_matrix4));
    memset(sps->scaling_matrix8, 16, sizeof(sps->scaling_matrix8));
    sps->scaling_matrix_present = 0;

	if(sps->profile_idc >= 100){ //high profile
        sps->chroma_format_idc= get_ue_golomb_31(gb);
        if(sps->chroma_format_idc == 3)
            sps->residual_color_transform_flag = get_bits1(gb);
        sps->bit_depth_luma   = get_ue_golomb(gb) + 8;
        sps->bit_depth_chroma = get_ue_golomb(gb) + 8;
        sps->transform_bypass = get_bits1(gb);
        decode_scaling_matrices(gb, sps, 1, sps->scaling_matrix4, sps->scaling_matrix8);
    }else{
        sps->chroma_format_idc= 1;
    }

    sps->log2_max_frame_num= get_ue_golomb(gb) + 4;
    sps->poc_type= get_ue_golomb_31(gb);

    if(sps->poc_type == 0)
        sps->log2_max_poc_lsb= get_ue_golomb(gb) + 4;
    else if(sps->poc_type == 1)
	{//FIXME #define
        sps->delta_pic_order_always_zero_flag = get_bits1(gb);
        sps->offset_for_non_ref_pic = get_se_golomb(gb);
        sps->offset_for_top_to_bottom_field = get_se_golomb(gb);
        sps->poc_cycle_length = get_ue_golomb(gb);

        if((unsigned)sps->poc_cycle_length >= FF_ARRAY_ELEMS(sps->offset_for_ref_frame))
            goto fail;
        
        for(i=0; i<sps->poc_cycle_length; i++)
            sps->offset_for_ref_frame[i]= get_se_golomb(gb);
    }
	else if(sps->poc_type != 2) 
        goto fail;
   

    sps->ref_frame_count= get_ue_golomb_31(gb);
    if(sps->ref_frame_count > MAX_PICTURE_COUNT-2 || sps->ref_frame_count >= 32U)
        goto fail;
    
    sps->gaps_in_frame_num_allowed_flag= get_bits1(gb);
    sps->mb_width = get_ue_golomb(gb) + 1;
    sps->mb_height= get_ue_golomb(gb) + 1;
    if((unsigned)sps->mb_width >= INT_MAX/16 || (unsigned)sps->mb_height >= INT_MAX/16 ||
       avcodec_check_dimensions(16*sps->mb_width, 16*sps->mb_height))
        goto fail;
    

    sps->frame_mbs_only_flag= get_bits1(gb);
    if(!sps->frame_mbs_only_flag)
        sps->mb_aff= get_bits1(gb);
    else
        sps->mb_aff= 0;

    /*Stop here, we only need these information!*/
#if 0
    sps->direct_8x8_inference_flag= get_bits1(gb);


    sps->crop= get_bits1(gb);
    if(sps->crop){
        sps->crop_left  = get_ue_golomb(gb);
        sps->crop_right = get_ue_golomb(gb);
        sps->crop_top   = get_ue_golomb(gb);
        sps->crop_bottom= get_ue_golomb(gb);
    }else{
        sps->crop_left  =
        sps->crop_right =
        sps->crop_top   =
        sps->crop_bottom= 0;
    }

    sps->vui_parameters_present_flag= get_bits1(gb);
    if( sps->vui_parameters_present_flag )
        decode_vui_parameters(gb, sps);
#endif

//sleep(1);
//printf(" line %d, time_scale=%d, tick=%d\n", __LINE__, sps->time_scale, sps->num_units_in_tick);

    return 0;
fail:
    return -1;
}



