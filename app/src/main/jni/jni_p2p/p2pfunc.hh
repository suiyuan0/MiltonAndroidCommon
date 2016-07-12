/*
 * File Name:       CM.h
 *
 * Reference:
 *
 * Author:          leon jun
 *
 * Description:
 *      Connect Manager (CM) interface file.
 *
 * History:
 *      please record the history in the following format:
 *      verx.y.z    date        author      description or bug/cr number
 *      ----------------------------------------------------------------
 *      ver0.0.1    08/31/2015  leon	   first draft
 *          
 *  
 *CodeReview Log:
 *      please record the code review log in the following format:
 *      verx.y.z    date        author      description
 *      ----------------------------------------------------------------
 * 
 */

#ifndef _CM_H_
#define _CM_H_

#include "p2ptypes.hh"


#ifdef POS_DLL  // create or using dll library
#   ifdef CM_EXPORTS   // dll support
#       define CM_API  __declspec(dllexport)
#   else    // dll using
#       define CM_API  __declspec(dllimport)
#   endif
#else   // create or using static library
#   define CM_API
#endif

#define interface	class
#define PURE = 0

#define POS_UUID_LEN 16

#ifdef WIN32
#define POS_LOG printf
#elif defined __ANDROID__
#include <android/log.h>
#define ANDROID_LOG_TAG "p2p"
#define POS_LOG(...) __android_log_print(ANDROID_LOG_INFO, ANDROID_LOG_TAG, __VA_ARGS__)
#else
#define POS_LOG printf
#endif

namespace cricket {

typedef enum Ecm_addr_type{
	CM_ADDR_TYPE_UUID = 0, //uuid
	CM_ADDR_TYPE_SOCK,     //ip,port
	CM_ADDR_TYPE_UNID      //unknown id
}ECM_ADDR_TYPE;

typedef struct{
	int32 type;
	//uint32 uuid;
	uint8 uuid[POS_UUID_LEN];
	uint32 ip;
	uint16 port;
	FILLER2;
}Tp2pAddr;

typedef enum Ecm_result_type{
	CM_RESULT_SERVER = 0, //server status result
	CM_RESULT_CONNECT,	  //destination client connect status
	CM_RESULT_NAT		  //NAT treaverse result
}ECM_RESULT_TYPE;

typedef enum Ecm_status_type{
	CM_STATUS_FALSE = 0, 
	CM_STATUS_TRUE		 
}ECM_STATUS_TYPE;

typedef struct{
	int32 type;
	int32 status;
}Tp2pResult;
CM_API void UUIDExpand(int8 * uuid_expand, int8 * uuid_compact);
CM_API void DectoHex(uint8 *dstBuf, uint8 *srcBuf, uint32 dstSize);

/*
*	Call back of cm, used for receiving data from network.
*/
interface ICMClientCallBack
{
public:

	virtual void HandleCmEvent(
		uint32 msgID,	//msg type
		uint32 wParam,	//msg length
		int32* pParam,	//msg buffer
		void *srcAddr,	//network source address of the msg
		void *user = NULL
		) PURE;

};

class NetCallBack : public ICMClientCallBack
{
public:
    NetCallBack(){};
	void HandleCmEvent(uint32 msgID, uint32 wParam, int32* pParam, void *srcAddr, void *user = 0);
};

/************************************************************************************************
 *                                                                                              *
 *                  CONNECT MANAGER SERVER (CMS) INTERFACE                                      *
 *                                                                                              *
 ************************************************************************************************/

/*
 *    Name: P2P_init
 *    Description:
 *        Initialize connect manager.
 *    Parameters:
 *        pCallBack : callback func pointer.
 *    Return:
 *        < 0 : failed, check the error number form ECM_ERROR_CODE.
 *        otherwise : successful.
 *    Remark:
 *
 */
CM_API uint8* P2P_init(ICMClientCallBack *pCallBack, void * user, int8 *configPath);


/*
 *    Name: P2P_uninit
 *    Description:
 *        Uninitialize connect manager.
 *    Parameters:
 *        
 *    Return:
 *        All successful.
 *    Remark:
 *
 */
CM_API int32 P2P_uninit(void);


/*
 *    Name: P2P_connect
 *    Description:
 *        connect to other client.
 *    Parameters:
 *        dstAddr : the Address of destination client.
 *    Return:
 *        < 0 : failed, check the error number form ECM_ERROR_CODE.
 *        otherwise : successful, return the register id.
 *    Remark:
 *
 */
CM_API int32 P2P_connect(Tp2pAddr *dstAddr);


/*
 *    Name: P2P_disconnect
 *    Description:
 *        disconnect to other client.
 *    Parameters:
 *        uuid : the unique id of other client.
 *    Return:
 *        < 0 : failed, check the error number form ECM_ERROR_CODE.
 *        otherwise : successful.
 *    Remark:
 *
 */
CM_API int32 P2P_disconnect(Tp2pAddr *dstAddr);

/*
 *    Name: cms_send
 *    Description:
 *        Send data to destination without success validate.
 *    Parameters:
 *        pBuf : data buffer.
 *        iLen : data length.
 *        pDstAddr : destination address.
 *    Return:
 *        == iLen : send success
 *        otherwise : failed send.
 *    Remark:
 *
 */
CM_API int32 P2P_sendto(int8 *pBuf, int32 iLen, Tp2pAddr *dstAddr, uint16 msgID=1);

}//namespace cricket

#endif //_CM_H_
