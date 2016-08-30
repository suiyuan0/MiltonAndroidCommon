package com.milton.common.demo.receiver;

import android.content.Context;

import com.milton.common.util.LogUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

/**
 * Created by milton on 16/8/25.
 */
public class XGMessageReceiver extends XGPushBaseReceiver {
    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        LogUtil.e("alinmi22", "onRegisterResult token = " + xgPushRegisterResult.getToken());
    }

    @Override
    public void onUnregisterResult(Context context, int i) {
        LogUtil.e("alinmi22", "onUnregisterResult");
    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        LogUtil.e("alinmi22", "onSetTagResult");
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        LogUtil.e("alinmi22", "onDeleteTagResult");
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        LogUtil.e("alinmi22", "onTextMessage");
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        LogUtil.e("alinmi22", "onNotifactionClickedResult");
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        LogUtil.e("alinmi22", "onNotifactionShowedResult");
    }
}
