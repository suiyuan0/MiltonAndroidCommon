package com.milton.common.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.milton.common.lib.R;
import com.milton.common.util.ResourceUtil;
import com.milton.common.util.ScreenUtil;
import com.milton.common.widget.MaxHeightListView;

import java.util.List;

/**
 * Created by milton on 16/5/27.
 */
public class BottomDialog extends Dialog {
    public interface ViewCreateListener {
        public void initTop(TextView top);

        public void initContent(ListView content);
    }

    public BottomDialog(Context context) {
        super(context, R.style.bottomDialogStyle);
    }

    private List<ItemBean> mDataList;
    private TextView mTitle;
    private MaxHeightListView mContent;
    private ViewCreateListener mViewCreateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_bottom);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimBottom);
        setCanceledOnTouchOutside(true);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mContent = (MaxHeightListView) findViewById(R.id.lv_content);
        mContent.setListViewHeight(ScreenUtil.getScreenHeight(getContext()) * 2 / 3);
        if (null != mViewCreateListener) {
            mViewCreateListener.initTop(mTitle);
            mViewCreateListener.initContent(mContent);
        }
    }

    public ViewCreateListener getmViewCreateListener() {
        return mViewCreateListener;
    }

    public void setViewCreateListener(ViewCreateListener iewCreateListener) {
        this.mViewCreateListener = iewCreateListener;
    }

    public TextView getTop() {
        return mTitle;
    }

    public ListView getContent() {
        return mContent;
    }

    public static class ListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<ItemBean> mDataList;
        private boolean mFirstSpecial;
        private OnItemClickListener mOnItemClickListener;
        private int mIndex;

        public interface OnItemClickListener {
            public void itemClick(View view, int position, int index);
        }

        public ListAdapter(Context context, int index, List<ItemBean> dataList, boolean firstSpecial, OnItemClickListener itemClickListener) {
            mInflater = LayoutInflater.from(context);
            mIndex = index;
            mDataList = dataList;
            mFirstSpecial = firstSpecial;
            mOnItemClickListener = itemClickListener;
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.dialog_list_one_line_text, null);
                tv = (TextView) convertView.findViewById(R.id.content);

                convertView.setTag(tv);
            } else {
                tv = (TextView) convertView.getTag();
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnItemClickListener) {
                        mOnItemClickListener.itemClick(v, position, mIndex);
                    }
                }
            });
            final Context context = convertView.getContext();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tv.getLayoutParams();
            lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 5), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 5));
            tv.setBackgroundResource(R.drawable.dialog_list_button_background);
            if (mFirstSpecial) {
                if (mDataList.size() == 1) {
                    tv.setBackgroundResource(R.drawable.dialog_list_button_special_background);
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20));
                } else if (position == 0) {
                    tv.setBackgroundResource(R.drawable.dialog_list_button_special_background);
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20));
                } else if (position == mDataList.size() - 1) {
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 5), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20));
                }
            } else {
                if (mDataList.size() == 1) {
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20));
                } else if (position == 0) {
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 5));
                } else if (position == mDataList.size() - 1) {
                    lp.setMargins(ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 5), ResourceUtil.dp2px(context, 20), ResourceUtil.dp2px(context, 20));
                }
            }
            tv.setLayoutParams(lp);
            tv.setText(mDataList.get(position).mText);
            return convertView;
        }

        public List<ItemBean> getDataList() {
            return mDataList;
        }

        public void setDataList(List<ItemBean> dataList) {
            this.mDataList = dataList;
        }

        public void setIndex(int index) {
            mIndex = index;
        }
    }

    public static class ItemBean {
        public String mText;
        public Object mData;

        public ItemBean(String text, Object object) {
            mText = text;
            mData = object;
        }
    }
}
