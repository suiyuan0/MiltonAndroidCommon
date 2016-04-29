
package com.milton.common.demo.activity.imageloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.milton.common.demo.R;
import com.milton.common.demo.adapter.ImageLoaderBaseAdaper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpLoaderActivity extends ImageLoaderBaseActivity {
    private GridAdapter mAdapter;

    @Override
    public void initData() {
        final int size = 100;
        String data[] = new String[size];
        for (int i = 0; i < size; i++) {
            data[i] = "http://img.quwenjiemi.com/2014/0701/thumb_420_234_20140701112917406.jpg";
        }
        mAdapter = new GridAdapter(this, data);
        mGridImage.setAdapter(mAdapter);
    }

    public class GridAdapter extends ImageLoaderBaseAdaper {
        private NormalLoadPictrue mLoader;

        private void setImageLoader(NormalLoadPictrue loader) {
            mLoader = loader;
        }

        public GridAdapter(Context context, String[] data, NormalLoadPictrue loader) {
            this(context, data);
            mLoader = loader;
        }

        public GridAdapter(Context context, String[] data) {
            super(context, data);
        }

        @Override
        public void initImageView(String uri, ImageView imageView) {
            super.initImageView(uri, imageView);
            new NormalLoadPictrue(uri, imageView);
            // mLoader.getPicture(uri, imageView);
        }
    }

    public class NormalLoadPictrue {

        private String uri;
        private ImageView imageView;
        private byte[] picByte;

        public NormalLoadPictrue(String uri, ImageView imageView) {
            getPicture(uri, imageView);
        }

        public void getPicture(String uri, ImageView imageView) {
            this.uri = uri;
            this.imageView = imageView;
            // imageView.setImageResource(R.drawable.ic_drawer_mobile);
            new Thread(runnable).start();
        }

        @SuppressLint("HandlerLeak")
        Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e("milton", "picByte = " + picByte);
                if (msg.what == 1) {

                    if (picByte != null) {
                        imageView.setImageResource(R.drawable.ic_drawer_mobile);
                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(picByte, 0,
                                        picByte.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.ic_drawer_mobile);
                    }
                }
            }
        };

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Message message = new Message();
                // message.what = 1;
                // handle.sendMessage(message);
                try {
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(10000);

                    if (conn.getResponseCode() == 200) {
                        InputStream fis = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length = -1;
                        while ((length = fis.read(bytes)) != -1) {
                            bos.write(bytes, 0, length);
                        }
                        picByte = bos.toByteArray();
                        bos.close();
                        fis.close();

                        Message message = new Message();
                        message.what = 1;
                        handle.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

    }
}
