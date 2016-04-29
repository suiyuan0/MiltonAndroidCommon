
package com.milton.common.demo.activity.imageloader;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;

import com.milton.common.demo.R;
import com.milton.common.demo.adapter.ImageLoaderBaseAdaper;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class UniversalImageLoaderActivity extends ImageLoaderBaseActivity {
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
            ImageLoader.getInstance().displayImage(uri, imageView, ImageLoaderPicture.getOptions(imageView.getContext()), new SimpleImageLoadingListener());
        }
    }

    public static class ImageLoaderPicture {

        private static DisplayImageOptions options;

        public static DisplayImageOptions initImageLoaderPicture(Context context) {
            // 1.完成ImageLoaderConfiguration的配置
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                    // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
                    // .taskExecutor(...)
                    // .taskExecutorForCachedImages(...)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 1) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                    // .memoryCache(new WeakMemoryCache())
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    // .discCache(new UnlimitedDiscCache(cacheDir))// default
                    .discCacheSize(50 * 1024 * 1024) // 缓冲大小
                    .discCacheFileCount(100) // 缓冲文件数目
                    .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                    // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                    .imageDownloader(new BaseImageDownloader(context)) // default
                    .imageDecoder(new BaseImageDecoder(true)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .writeDebugLogs()
                    .build();
            // 2.单例ImageLoader类的初始化
            ImageLoader.getInstance().init(config);
            // 3.DisplayImageOptions实例对象的配置
            // 以下的设置再调用displayImage()有效，使用loadImage()无效
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher) // image在加载过程中，显示的图片
                    .showImageForEmptyUri(R.drawable.ic_launcher) // empty URI时显示的图片
                    .showImageOnFail(R.drawable.ic_launcher) // 不是图片文件 显示图片
                    .resetViewBeforeLoading(false) // default
                    .delayBeforeLoading(1000)
                    .cacheInMemory(false) // default 不缓存至内存
                    .cacheOnDisc(false) // default 不缓存至手机SDCard
                    // .preProcessor(...)
                    // .postProcessor(...)
                    // .extraForDownloader(...)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// default
                    // .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .bitmapConfig(android.graphics.Bitmap.Config.ARGB_8888) // default
                    // .bitmapConfig(android.graphics.Bitmap.Config.RGB_565)
                    // .decodingOptions(...)
                    // .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                    // .handler(new Handler()) // default
                    .build();
            // // 4图片加载
            // // 4.1 调用displayImage
            // imageLoader.displayImage(
            // uri, /*
            // String imageUri = "http://site.com/image.png"; // from Web
            // String imageUri = "file:///mnt/sdcard/image.png"; // from SD card
            // String imageUri = "content://media/external/audio/albumart/13"; // from content provider
            // String imageUri = "assets://image.png"; // from assets
            // */
            // imageView, // 对应的imageView控件
            // options); // 与之对应的image显示方式选项
            //
            // // 4.2 调用loadImage
            // // 对于部分DisplayImageOptions对象的设置不起作用
            // imageLoader.loadImage(
            // uri,
            // options,
            // new MyImageListener()); //ImageLoadingListener
            // class MyImageListener extends SimpleImageLoadingListener{
            //
            // @Override
            // public void onLoadingStarted(String imageUri, View view) {
            // imageView.setImageResource(R.drawable.loading);
            // super.onLoadingStarted(imageUri, view);
            // }
            //
            // @Override
            // public void onLoadingFailed(String imageUri, View view,
            // FailReason failReason) {
            // imageView.setImageResource(R.drawable.no_pic);
            // super.onLoadingFailed(imageUri, view, failReason);
            // }
            //
            // @Override
            // public void onLoadingComplete(String imageUri, View view,
            // Bitmap loadedImage) {
            // imageView.setImageBitmap(loadedImage);
            // super.onLoadingComplete(imageUri, view, loadedImage);
            // }
            //
            // @Override
            // public void onLoadingCancelled(String imageUri, View view) {
            // imageView.setImageResource(R.drawable.cancel);
            // super.onLoadingCancelled(imageUri, view);
            // }
            //
            // }
            return options;
        }

        public static DisplayImageOptions getOptions(Context context) {
            if (options == null) {
                options = initImageLoaderPicture(context);
            }
            return options;
        }

    }
}
