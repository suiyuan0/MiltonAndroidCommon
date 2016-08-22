package com.milton.common.qrcode;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.milton.common.lib.R;
import com.milton.common.qrcode.Listener.FinishListener;
import com.milton.common.qrcode.Manager.BeepManager;
import com.milton.common.qrcode.Manager.CameraManager;
import com.milton.common.qrcode.handler.CaptureActivityHandler;
import com.milton.common.qrcode.widgets.ViewfinderView;
import com.milton.common.util.ScreenUtil;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Vector;


/**
 * Created by junyu.zhu@Honeywell.com on 16/7/7.
 */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private Context mContext = CaptureActivity.this;
    //surface 是否打开
    private boolean hasSurface;
    private BeepManager beepManager;// 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
    public SharedPreferences mSharedPreferences;// 存储二维码条形码选择的状态
    public static String currentState;// 条形码二维码选择状态

    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    //    private TextView statusView;
    private TextView scanTextView;
    private ImageView mImageViewBack;
    private ImageView mImageViewRight;
    public static final int REQUEST_CODE_SUCCESS = 200;
    public static final int REQUEST_CODE = 100;
    public static int CAPTURE_TYPE;


    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;
    private Vector<BarcodeFormat> decodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet
            .of(ResultMetadataType.ISSUE_NUMBER,
                    ResultMetadataType.SUGGESTED_PRICE,
                    ResultMetadataType.ERROR_CORRECTION_LEVEL,
                    ResultMetadataType.POSSIBLE_COUNTRY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("~~~~~~~~","time"+System.currentTimeMillis());
        setTheme();
        initSetting();
        setContentView(R.layout.activity_capture);
        initComponent();
        initView();
    }

    private void setTheme() {
        setTheme(R.style.CubeBaseTheme);
    }

    /**
     * 初始化窗口设置
     */
    private void initSetting() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
////        //不显示系统的标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 初始化功能组件
     */
    private void initComponent() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        currentState = this.mSharedPreferences.getString("currentState",
                "qrcode");
        cameraManager = new CameraManager(getApplication());
        Intent intent = getIntent();
        CAPTURE_TYPE = intent.getIntExtra("capture_type", 0);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        scanTextView = (TextView) findViewById(R.id.tv_title);
//        statusView = (TextView) findViewById(R.id.status_view);
        mImageViewBack = (ImageView) findViewById(R.id.iv_left);
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
        mImageViewRight = (ImageView) findViewById(R.id.iv_right);
        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop(v);
            }
        });
    }

    private void showPop(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.pop_window, null);
        TextView mTextViewManualInput = (TextView) contentView.findViewById(R.id.tv_input_manual);
        TextView mTextViewFlashLight = (TextView) contentView.findViewById(R.id.flash_light);
        mTextViewManualInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CaptureActivity.this, CaptureInputActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        mTextViewFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraManager.openFlashLight();

            }
        });

        final PopupWindow popupWindow = new PopupWindow(contentView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popupWindow.showAsDropDown(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String result = data == null ? "" : data.getStringExtra("result");
        if ("success".equals(result)) {
            finish();
        } else if (resultCode == CaptureInputActivity.RESULT_CODE) {
            String text = data.getStringExtra(CaptureInputActivity.QRCODE_ONELINE);

            ScanUIItem scanUIItem = CommonUtils.checkScanResult(text);
            if (!scanUIItem.isCorrect) {
                Toast.makeText(CaptureActivity.this, "无效的结果", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            ComponentName componentName;


            bundle.putString("resultString", text);
            bundle.putInt("capture_type", CAPTURE_TYPE);
            componentName = new ComponentName("com.honeywell.cube", "com.honeywell.cube.activities.CaptureResultActivity");

            intent.setComponent(componentName);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_SUCCESS);
            CaptureActivity.this.finish();
        }

    }

    /**
     * 初始设置扫描类型（最后一次使用类型）
     */
    private void setScanType() {
        viewfinderView.setVisibility(View.VISIBLE);
        qrcodeSetting();
    }

    /**
     * 主要对相机进行初始化工作
     */
    @Override
    protected void onResume() {
        super.onResume();
        inactivityTimer.onActivity();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        surfaceHolder = surfaceView.getHolder();
        setScanType();
        resetStatusView();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
        }
        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();
        // 恢复活动监控器
        inactivityTimer.onResume();
    }

    /**
     * 展示状态视图和扫描窗口，隐藏结果视图
     */
    private void resetStatusView() {
//        statusView.setVisibility(View.GONE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化摄像头。打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureActivityHandler(this, decodeFormats,
                        null, cameraManager);
            }
            // decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 若摄像头被占用或者摄像头有问题则跳出提示对话框
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        // 暂停活动监控器
        inactivityTimer.onPause();
        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        // 停止活动监控器
        inactivityTimer.shutdown();
        saveScanTypeToSp();
        super.onDestroy();
    }

    /**
     * 保存退出进程前选中的二维码条形码的状态
     */
    private void saveScanTypeToSp() {
        SharedPreferences.Editor localEditor = this.mSharedPreferences.edit();
        localEditor.putString("currentState", CaptureActivity.currentState);
        localEditor.commit();
    }

    /**
     * 获取扫描结果
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        ScanUIItem scanUIItem = CommonUtils.checkScanResult(rawResult.getText());
        if (!scanUIItem.isCorrect) {
            Toast.makeText(CaptureActivity.this, "无效的结果", Toast.LENGTH_SHORT).show();
            restartPreviewAfterDelay(0L);
            return;
        }

        inactivityTimer.onActivity();
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        Map<ResultMetadataType, Object> metadata = rawResult
                .getResultMetadata();
        StringBuilder metadataText = new StringBuilder(20);
        if (metadata != null) {
            for (Map.Entry<ResultMetadataType, Object> entry : metadata
                    .entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }

        //绑定，替换CUBE

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", barcode);
        bundle.putString("barcodeFormat", rawResult.getBarcodeFormat()
                .toString());
        bundle.putString("decodeDate",
                formatter.format(new Date(rawResult.getTimestamp())));
        bundle.putCharSequence("metadataText", metadataText);
        bundle.putString("resultString", rawResult.getText());
        bundle.putInt("capture_type", CAPTURE_TYPE);
        ComponentName componentName = new ComponentName("com.honeywell.cube", "com.honeywell.cube.activities.CaptureResultActivity");
        intent.setComponent(componentName);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);

    }


    private void qrcodeSetting() {

        decodeFormats = new Vector<BarcodeFormat>(2);
        decodeFormats.clear();
        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        scanTextView.setText(R.string.scanr);
//        mImageViewBack.setVisibility(View.VISIBLE);
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }
        cameraManager.setManualFramingRect(ScreenUtil.getScreenWidth(mContext) * 2 / 3, ScreenUtil.getScreenWidth(mContext) * 2 / 3);
        viewfinderView.refreshDrawableState();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: // 拦截返回键


                if (viewfinderView.getVisibility() == View.GONE) {
                    restartPreviewAfterDelay(0L);
                    return true;
                }

        }
        return super.onKeyDown(keyCode, event);
    }

}
