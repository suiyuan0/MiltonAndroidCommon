package com.milton.common.demo.activity.other;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.milton.common.activity.TitleBarActivity;
import com.milton.common.demo.R;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.widget.MultiPickResultView;

public class PhotoPickerActivity extends TitleBarActivity {

    MultiPickResultView recyclerView;
    MultiPickResultView recyclerViewShowOnly;
    ArrayList<String> pathslook;

    @Override
    protected void initView() {
        super.initView();
        pathslook = new ArrayList<>();

        recyclerView = (MultiPickResultView) findViewById(R.id.recycler_view);
        recyclerView.init(this, MultiPickResultView.ACTION_SELECT, null);

        recyclerViewShowOnly = (MultiPickResultView) findViewById(R.id.recycler_onlylook);
        recyclerViewShowOnly.init(this, MultiPickResultView.ACTION_ONLY_SHOW, pathslook);
        ArrayList<String> photos = new ArrayList<>();
        photos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2545179197,2573899739&fm=21&gp=0.jpg");
        photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1471325032244&di=71570ed352a1b823584c3b3b1b5bd57f&imgtype=jpg&src=http%3A%2F%2Ffile27.mafengwo.net%2FM00%2FB2%2F12%2FwKgB6lO0ahWAMhL8AAV1yBFJDJw20.jpeg");
        photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1471325032243&di=67dfaed98491c3a94965571ed4343951&imgtype=jpg&src=http%3A%2F%2Fwww.5068.com%2Fu%2Ffaceimg%2F20140725173411.jpg");
        photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1471325032243&di=d40f796d46782144ba0adf798253f080&imgtype=jpg&src=http%3A%2F%2Fimglf0.ph.126.net%2F1EnYPI5Vzo2fCkyy2GsJKg%3D%3D%2F2829667940890114965.jpg");
        photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1471325032243&di=bbb10b09ddb5338b53432af1c3789c39&imgtype=jpg&src=http%3A%2F%2Ffile25.mafengwo.net%2FM00%2F0A%2FAA%2FwKgB4lMC256AYLqGAAGklurKzyM52.rbook_comment.w1024.jpeg");


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.Button);
            }
        });


        findViewById(R.id.button_no_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.ButtonNoCamera);
            }
        });

        findViewById(R.id.button_one_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.ButtonOnePhoto);
            }
        });

        findViewById(R.id.button_photo_gif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.ButtonPhotoGif);
            }
        });

        findViewById(R.id.button_multiple_picked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(RequestCode.ButtonMultiplePicked);
            }

        });
    }

    @Override
    protected int getContent() {
        return R.layout.activity_photo_picker;
    }

    @Override
    protected String getCustomTitle() {
        return "PhotoPicker";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        recyclerView.onActivityResult(requestCode, resultCode, data);


        recyclerViewShowOnly.showPics(recyclerView.getPhotos());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay!
            onClick(RequestCode.values()[requestCode].mViewId);

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.CAMERA:
                // No need to explain to user as it is obvious
                return false;
            default:
                return true;
        }
    }

    private void checkPermission(@NonNull RequestCode requestCode) {

        int readStoragePermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        boolean readStoragePermissionGranted = readStoragePermissionState != PackageManager.PERMISSION_GRANTED;
        boolean cameraPermissionGranted = cameraPermissionState != PackageManager.PERMISSION_GRANTED;

        if (readStoragePermissionGranted || cameraPermissionGranted) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                String[] permissions;
                if (readStoragePermissionGranted && cameraPermissionGranted) {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                } else {
                    permissions = new String[]{
                            readStoragePermissionGranted ? Manifest.permission.READ_EXTERNAL_STORAGE
                                    : Manifest.permission.CAMERA
                    };
                }
                ActivityCompat.requestPermissions(this,
                        permissions,
                        requestCode.ordinal());
            }

        } else {
            // Permission granted
            onClick(requestCode.mViewId);
        }

    }

    private void onClick(@IdRes int viewId) {

        switch (viewId) {
            case R.id.button: {
                PhotoPickUtils.startPick(this, null);
                break;
            }

            case R.id.button_no_camera: {
                PhotoPicker.builder()
                        .setPhotoCount(7)
                        .setShowCamera(false)
                        .setPreviewEnabled(false)
                        .start(this);
                break;
            }

            case R.id.button_one_photo: {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .start(this);
                break;
            }

            case R.id.button_photo_gif: {
                PhotoPicker.builder()
                        .setShowCamera(true)
                        .setShowGif(true)
                        .start(this);
                break;
            }

            case R.id.button_multiple_picked: {
                //Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
                //PhotoPickerIntent.setPhotoCount(intent, 4);
                //PhotoPickerIntent.setShowCamera(intent, true);
                //PhotoPickerIntent.setSelected(intent,selectedPhotos);
                //startActivityForResult(intent, REQUEST_CODE);
//                PhotoPicker.builder()
//                        .setPhotoCount(4)
//                        .setShowCamera(true)
//                        .setSelected(selectedPhotos)
//                        .start(this);
//                break;
            }
        }
    }

    enum RequestCode {
        Button(R.id.button),
        ButtonNoCamera(R.id.button_no_camera),
        ButtonOnePhoto(R.id.button_one_photo),
        ButtonPhotoGif(R.id.button_photo_gif),
        ButtonMultiplePicked(R.id.button_multiple_picked);

        @IdRes
        final int mViewId;

        RequestCode(@IdRes int viewId) {
            mViewId = viewId;
        }
    }
}
