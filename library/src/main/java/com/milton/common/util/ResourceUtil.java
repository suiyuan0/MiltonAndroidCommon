
package com.milton.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.widget.ImageView;

//import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;

public class ResourceUtil {
    public static float getTextWidth(String text, float size) {
        TextPaint FontPaint = new TextPaint();
        FontPaint.setTextSize(size);
        // 注意如果设置了textStyle，还需要进一步设置TextPaint。
        return FontPaint.measureText(text);
    }

    /**
     * 使用TransitionDrawable实现渐变效果 ,比使用AlphaAnimation效果要好，可避免出现闪烁问题。
     * 
     * @param imageView
     * @param bitmap
     */
//    public void setImageBitmap(ImageView imageView, Bitmap bitmap) {
//        // Use TransitionDrawable to fade in.
//        final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
//                new ColorDrawable(android.R.color.transparent), new BitmapDrawable(imageView.getResources(), bitmap)
//        });
//        // noinspection deprecation
//        imageView.setBackgroundDrawable(imageView.getDrawable());
//        imageView.setImageDrawable(td);
//        td.startTransition(200);
//    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().density);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

//    /** 从assets 文件夹中读取文本数据 */
//    public static String getTextFromAssets(final Context context, String fileName) {
//        String result = "";
//        try {
//            InputStream in = context.getResources().getAssets().open(fileName);
//            // 获取文件的字节数
//            int lenght = in.available();
//            // 创建byte数组
//            byte[] buffer = new byte[lenght];
//            // 将文件中的数据读到byte数组中
//            in.read(buffer);
//            result = EncodingUtils.getString(buffer, "UTF-8");
//            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

    /** 从assets 文件夹中读取图片 */
    public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            return Drawable.createFromStream(is, null);
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
