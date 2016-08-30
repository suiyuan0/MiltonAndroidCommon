
package com.milton.common.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewUtil {
    public static void saveCurrentPosition(ListView listView, int[] saveInfo) {
        if (listView != null) {
            saveInfo[0] = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            saveInfo[1] = (v == null) ? 0 : v.getTop();
            // 保存position和top
        }
    }

    public static void restorePosition(ListView listView, int[] saveInfo) {
        if (listView != null) {
            listView.setSelectionFromTop(saveInfo[0], saveInfo[1]);
        }
    }

    /**
     * 代码设置TextView的样式
     * 
     * @param context
     * @param style
     * @return
     */
    public static TextView getCustomTextView(Context context, int style) {
        return new TextView(new ContextThemeWrapper(context, style));
    }

    /**
     * WebView保留缩放功能但隐藏缩放控件
     * 
     * @param webView
     */
    public static void customWebView(WebView webView) {
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        // if (DeviceUtils.hasHoneycomb())
        // setDisplayZoomControls是在API Level 11中新增。
        webView.getSettings().setDisplayZoomControls(false);
    }

    /**
     * ListView使用ViewHolder极简写法 用法 用起来非常简练，将ViewHolder隐于无形。
     * 
     * @Override public View getView(int position, View convertView, ViewGroup
     *           parent) { if (convertView == null) { convertView =
     *           LayoutInflater
     *           .from(getActivity()).inflate(R.layout.fragment_feed_item,
     *           parent, false); } ImageView thumnailView =
     *           getAdapterView(convertView, R.id.video_thumbnail); ImageView
     *           avatarView = getAdapterView(convertView, R.id.user_avatar);
     * @param convertView
     * @param id
     * @return
     */
    public static <T extends View> T getAdapterView(View convertView, int id) {
        if (convertView == null) {
            return null;
        }
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
    //onCreate中强行获取View的宽高
    public static int[] forceGetViewSize(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }
}
