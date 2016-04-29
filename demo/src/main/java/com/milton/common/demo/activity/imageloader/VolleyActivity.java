
package com.milton.common.demo.activity.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.milton.common.demo.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.milton.common.demo.adapter.ImageLoaderBaseAdaper;

public class VolleyActivity extends ImageLoaderBaseActivity {

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

        public GridAdapter(Context context, String[] data) {
            super(context, data);
        }

        @Override
        public void initImageView(String uri, ImageView imageView) {
            super.initImageView(uri, imageView);
            VolleyLoadPicture vlp = new VolleyLoadPicture(imageView.getContext(), imageView);
            vlp.getmImageLoader().get(uri, vlp.getOne_listener());
        }
    }

    public class VolleyLoadPicture {

        private ImageLoader mImageLoader = null;
        private BitmapCache mBitmapCache;

        private ImageListener one_listener;

        public VolleyLoadPicture(Context context, ImageView imageView) {
            one_listener = ImageLoader.getImageListener(imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);

            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
            mBitmapCache = new BitmapCache();
            mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache);
        }

        public ImageLoader getmImageLoader() {
            return mImageLoader;
        }

        public ImageListener getOne_listener() {
            return one_listener;
        }

        class BitmapCache implements ImageCache {
            private LruCache<String, Bitmap> mCache;
            private int sizeValue;

            public BitmapCache() {
                int maxSize = 10 * 1024 * 1024;
                mCache = new LruCache<String, Bitmap>(maxSize) {
                    @Override
                    protected int sizeOf(String key, Bitmap value) {
                        sizeValue = value.getRowBytes() * value.getHeight();
                        return sizeValue;
                    }

                };
            }

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        }

    }
}
