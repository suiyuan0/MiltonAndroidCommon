/**
* @file SPS.h 
* @brief H.264 SPS decode function header.
* @author Lonsn
* @date 05/22/2012
* @version 0.1
*
* Detailed description for SPS.h 
* Decode the SPS define in H.264, Borrow from Aptus project
*/

#ifndef _HONEYWELL_MEDIA_DATA_SRC_SPS_H
#define _HONEYWELL_MEDIA_DATA_SRC_SPS_H

typedef signed char      int8_t;
typedef unsigned char    uint8_t;
typedef unsigned int     uint32_t;
typedef unsigned long long  uint64_t_2;

typedef struct GetBitContext {
    const uint8_t *buffer, *buffer_end;
    int index;
    int size_in_bits;
} GetBitContext;

#ifndef AVRATIONALDEF_ZWS
#define AVRATIONALDEF_ZWS
typedef struct AVRational{
    int num; ///< numerator
    int den; ///< denominator
} AVRational;
#endif

typedef struct SPS{
    int profile_idc;
    int level_idc;
    int chroma_format_idc;
    int transform_bypass;              ///< qpprime_y_zero_transform_bypass_flag
    int log2_max_frame_num;            ///< log2_max_frame_num_minus4 + 4
    int poc_type;                      ///< pic_order_cnt_type
    int log2_max_poc_lsb;              ///< log2_max_pic_order_cnt_lsb_minus4
    int delta_pic_order_always_zero_flag;
    int offset_for_non_ref_pic;
    int offset_for_top_to_bottom_field;
    int poc_cycle_length;              ///< num_ref_frames_in_pic_order_cnt_cycle
    int ref_frame_count;               ///< num_ref_frames
    int gaps_in_frame_num_allowed_flag;
    int mb_width;                      ///< pic_width_in_mbs_minus1 + 1
    int mb_height;                     ///< pic_height_in_map_units_minus1 + 1
    int frame_mbs_only_flag;
    int mb_aff;                        ///<mb_adaptive_frame_field_flag
    int direct_8x8_inference_flag;
    int crop;                   ///< frame_cropping_flag
    unsigned int crop_left;            ///< frame_cropping_rect_left_offset
    unsigned int crop_right;           ///< frame_cropping_rect_right_offset
    unsigned int crop_top;             ///< frame_cropping_rect_top_offset
    unsigned int crop_bottom;          ///< frame_cropping_rect_bottom_offset
    int vui_parameters_present_flag;
    AVRational sar;
    int timing_info_present_flag;
    uint32_t num_units_in_tick;
    uint32_t time_scale;
    int fixed_frame_rate_flag;
    short offset_for_ref_frame[256]; //FIXME dyn aloc?
    int bitstream_restriction_flag;
    int num_reorder_frames;
    int scaling_matrix_present;
    uint8_t scaling_matrix4[6][16];
    uint8_t scaling_matrix8[2][64];
    int nal_hrd_parameters_present_flag;
    int vcl_hrd_parameters_present_flag;
    int pic_struct_present_flag;
    int time_offset_length;
    int cpb_cnt;                       ///< See H.264 E.1.2
    int initial_cpb_removal_delay_length; ///< initial_cpb_removal_delay_length_minus1 +1
    int cpb_removal_delay_length;      ///< cpb_removal_delay_length_minus1 + 1
    int dpb_output_delay_length;       ///< dpb_output_delay_length_minus1 + 1
    int bit_depth_luma;                ///< bit_depth_luma_minus8 + 8
    int bit_depth_chroma;              ///< bit_depth_chroma_minus8 + 8
    int residual_color_transform_flag; ///< residual_colour_transform_flag
}SPS;

#define OPEN_READER(name, gb)\
        int name##_index= (gb)->index;\
        int name##_cache= 0;\

#define UPDATE_CACHE(name, gb)\
        name##_cache= AV_RB32( ((const uint8_t *)(gb)->buffer)+(name##_index>>3) ) << (name##_index&0x07);\

#define GET_CACHE(name, gb)\
        ((uint32_t)name##_cache)

#define SKIP_COUNTER(name, gb, num)\
        name##_index += (num);\

#define LAST_SKIP_BITS(name, gb, num) SKIP_COUNTER(name, gb, num)

#define CLOSE_READER(name, gb)\
        (gb)->index= name##_index;\

#define AV_RB32(x)  ((((const uint8_t*)(x))[0] << 24) | \
                     (((const uint8_t*)(x))[1] << 16) | \
                     (((const uint8_t*)(x))[2] <<  8) | \
                      ((const uint8_t*)(x))[3])

#define FF_ARRAY_ELEMS(a) (sizeof(a) / sizeof((a)[0]))
#define MAX_PICTURE_COUNT 32
#ifndef INT_MAX
#define INT_MAX 2147483647    /* maximum (signed) int value */
#endif
#define ALLOW_INTERLACE

int decode_seq_parameter_set(const uint8_t* sps_raw_data, SPS *sps);

#endif /* _HONEYWELL_MEDIA_DATA_SRC_SPS_H */
