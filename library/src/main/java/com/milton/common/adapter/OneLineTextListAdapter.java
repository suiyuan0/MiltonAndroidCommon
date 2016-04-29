
package com.milton.common.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milton.common.lib.R;

public class OneLineTextListAdapter extends BaseAdapter {
    String mData[];
    private LayoutInflater mInflater;

    public OneLineTextListAdapter(String[] data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.length;
    }

    @Override
    public Object getItem(int i) {
        return mData[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        TextView tv;
        if (view == null) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(viewgroup.getContext());
            }
            tv = (TextView) mInflater.inflate(R.layout.list_one_line_text, null);
        } else {
            tv = (TextView) view;
        }
        tv.setText(mData[position]);
        tv.setBackgroundColor(Color.parseColor(position % 2 == 0 ? "#FFFFFF" : "#F5F5F5"));
        return tv;
    }

    public String[] getData() {
        return mData;
    }

    public void setData(String[] mData) {
        this.mData = mData;
    }

}
