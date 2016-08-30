
package com.milton.common.demo.activity.multimedia;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, OnClickListener {
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private ImageView mShutter;
    private Camera mCamera;
    private boolean isPreviewing;
    private Parameters mParams;

    @Override
    public void initView() {
        setContentView(R.layout.activity_camera);
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mShutter = (ImageView) findViewById(R.id.shutter);
        mHolder = mPreview.getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        mCamera = Camera.open();
        startPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPreview();
    }

    private void startPreview() {
        if (mCamera == null) {
            return;
        }
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        mParams = mCamera.getParameters();
        // mParams.set...
        mParams.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式
        // CamParaUtil.getInstance().printSupportPictureSize(mParams);
        // CamParaUtil.getInstance().printSupportPreviewSize(mParams);
        // 设置PreviewSize和PictureSize
        // Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
        // mParams.getSupportedPictureSizes(), previewRate, 800);
        // mParams.setPictureSize(pictureSize.width, pictureSize.height);
        // Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
        // mParams.getSupportedPreviewSizes(), previewRate, 800);
        mParams.setPreviewSize(800, 480);

        // CamParaUtil.getInstance().printSupportFocusMode(mParams);
        List<String> focusModes = mParams.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            mParams.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.setParameters(mParams);

        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();// 开启预览
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        isPreviewing = true;

        mParams = mCamera.getParameters();

    }

    private void stopPreview() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        // TODO Auto-generated method stub
        mHolder = arg0;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        stopPreview();
    }

    @Override
    public void onClick(View arg0) {
        if (isPreviewing && (mCamera != null)) {
            // mCamera.takePicture(mShutterCallback, null, mJpegPictureCallback);
            mCamera.takePicture(null, null, null);
        }

    }

}
