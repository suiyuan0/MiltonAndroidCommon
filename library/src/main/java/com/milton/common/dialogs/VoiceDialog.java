package com.milton.common.dialogs;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.speech.VoiceRecognitionService;
import com.milton.common.lib.R;
import com.milton.common.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by milton on 16/5/27.
 */
public class VoiceDialog extends Dialog implements RecognitionListener {
//    public interface ViewCreateListener {
//        public void initTip(TextView tip);
//
//        public void initVoice(CheckBox voice);
//
//        public void initClose(ImageView close);
//    }

    public interface RequestVoiceListener {
        public boolean requestVoice(String result);
    }

    public VoiceDialog(Context context) {
        super(context, R.style.bottomDialogStyle);
    }

    private TextView mTip;
    private CheckBox mVoice;
    private ImageView mClose;
    //    private ViewCreateListener mViewCreateListener;
    private RequestVoiceListener mRequestVoiceListener;
    public static final String TAG = VoiceDialog.class.getSimpleName();
    private SpeechRecognizer speechRecognizer;
    private static final int EVENT_ERROR = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_voice);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimBottom);
        setCanceledOnTouchOutside(false);
        mTip = (TextView) findViewById(R.id.tv_tip);
        mVoice = (CheckBox) findViewById(R.id.cb_voice);
        mClose = (ImageView) findViewById(R.id.iv_close);
//        if (null != mViewCreateListener) {
//            mViewCreateListener.initTip(mTip);
//            mViewCreateListener.initVoice(mVoice);
//            mViewCreateListener.initClose(mClose);
//        }
        // 创建识别器
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext(), new ComponentName(getContext(), VoiceRecognitionService.class));
        // 注册监听器
        speechRecognizer.setRecognitionListener(this);
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();
                mTip.setText(isChecked ? R.string.voice_tip : R.string.voice_tip2);
                if (isChecked) {
                    startASR();
                } else {
                    speechRecognizer.stopListening();
                    speechRecognizer.cancel();
                }
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mVoice != null) {
                    mVoice.setChecked(true);
                    mTip.setText(R.string.voice_tip);
                }
                startASR();
                LogUtil.e("11111", "onShow");
            }
        });
    }

    // 开始识别
    void startASR() {
        Intent intent = new Intent();
        bindParams(intent);
        speechRecognizer.startListening(intent);
    }

    void bindParams(Intent intent) {
        // 设置识别参数
        intent.putExtra("language", "cmn-Hans-CN");
        intent.putExtra("sample", 8000);
        intent.putExtra("vad", "input");
        intent.putExtra("nlu", "disable");
    }

//    public ViewCreateListener getmViewCreateListener() {
//        return mViewCreateListener;
//    }
//
//    public void setViewCreateListener(ViewCreateListener iewCreateListener) {
//        this.mViewCreateListener = iewCreateListener;
//    }

    public void setRequestVoiceListener(RequestVoiceListener listener) {
        this.mRequestVoiceListener = listener;
    }

    public TextView getTip() {
        return mTip;
    }

    public CheckBox getVoice() {
        return mVoice;
    }

    public ImageView getClose() {
        return mClose;
    }

    @Override
    public void dismiss() {
        speechRecognizer.stopListening();
        speechRecognizer.cancel();
        super.dismiss();
    }

    public void destroy() {
        dismiss();
        speechRecognizer.destroy();
    }

    // =======================================================================================================================================
    @Override
    public void onReadyForSpeech(Bundle params) {
        LogUtil.e(TAG, "onReadyForSpeech");

    }

    @Override
    public void onBeginningOfSpeech() {
        LogUtil.e(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // Log.e(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // Log.e(TAG, "onBufferReceived");
    }

    @Override
    public void onEndOfSpeech() {
        LogUtil.e(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        StringBuilder sb = new StringBuilder();
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                sb.append("音频问题");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                sb.append("没有语音输入");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                sb.append("其它客户端错误");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                sb.append("权限不足");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                sb.append("网络问题");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                sb.append("没有匹配的识别结果");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                sb.append("引擎忙");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                sb.append("服务端错误");
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                sb.append("连接超时");
                break;
        }
        sb.append(":" + error);
//        ToastUtil.showShort(getContext(), sb);
        mVoice.setChecked(false);
        LogUtil.e(TAG, "onError  " + sb, true);
        // print("识别失败：" + sb.toString());
        // btn.setText("开始");
    }

    @Override
    public void onResults(Bundle results) {
        mVoice.setChecked(false);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // TODO Auto-generated method stub

        ArrayList<String> nbest = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (nbest.size() > 0) {
            // print("~临时识别结果：" + Arrays.toString(nbest.toArray(new String[0])));
            LogUtil.e(TAG, "onPartialResults  text = " + nbest.get(nbest.size() - 1));
            if (mRequestVoiceListener != null) {
                if (mRequestVoiceListener.requestVoice(nbest.get(0))) {
                    dismiss();
                }
            }
//            ToastU.makeText(getContext(), "-----> " + nbest.get(0), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        // TODO Auto-generated method stub
        LogUtil.e(TAG, "onEvent");
        switch (eventType) {
            case EVENT_ERROR:
                String reason = params.get("reason") + "";
                LogUtil.e(TAG, "onEvent = " + reason);
                // print("EVENT_ERROR, " + reason);
                break;
            case VoiceRecognitionService.EVENT_ENGINE_SWITCH:
                int type = params.getInt("engine_type");
                // print("*引擎切换至" + (type == 0 ? "在线" : "离线"));
                LogUtil.e(TAG, "onEvent = " + "*引擎切换至" + (type == 0 ? "在线" : "离线"));
                break;
        }
    }

}
