
package com.milton.common.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.milton.common.demo.R;
import com.milton.common.util.ViewUtil;

public class ImageLoaderBaseAdaper extends BaseAdapter {
    private String mData[];
    private LayoutInflater mInflater;

    public ImageLoaderBaseAdaper(Context context, String[] data) {
        if (context != null) {
            mInflater = LayoutInflater.from(context);
        }
        mData = data;

    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.length;
    }

    @Override
    public Object getItem(int arg0) {
        return mData == null ? null : mData[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("milton6", "position = " + position);
        if (convertView == null)
        {
            if (mInflater != null) {
                convertView = mInflater.inflate(R.layout.griditem_image_loader_base, parent, false);
            }
        }
        ImageView thumnailView = ViewUtil.getAdapterView(convertView, R.id.image);
        // thumnailView.setImageResource(R.drawable.ic_drawer_mobile);
        initImageView(mData[position], thumnailView);
        Log.e("milton", "convertView = " + convertView);
        return convertView;
    }

    public void initImageView(String uri, ImageView imageView) {

    }
}
