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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.milton.common.lib.R;
import com.milton.common.util.ResourceUtil;

/**
 * Created by milton on 16/5/27.
 */
public class WeekSelectDialog extends Dialog {
    public interface ViewCreateListener {
        public void initTitle(TextView title);

        public void initLeftIcon(ImageView left);

        public void initRightIcon(ImageView right);

        public void initContent(ListView content);
    }

    public WeekSelectDialog(Context context) {
        super(context, R.style.selectWeekDialogStyle);
    }

    private TextView mTitle;
    private ImageView mLeft;
    private ImageView mRight;
    private ListView mContent;
    private ViewCreateListener mViewCreateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.dialog_select_week);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.AnimSelectWeek);
        setCanceledOnTouchOutside(false);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mLeft = (ImageView) findViewById(R.id.iv_left);
        mRight = (ImageView) findViewById(R.id.iv_right);
        mContent = (ListView) findViewById(R.id.lv_content);
//        mContent.setListViewHeight(ScreenUtil.getScreenHeight(getContext()) * 2 / 3);

        if (null != mViewCreateListener) {
            mViewCreateListener.initLeftIcon(mLeft);
            mViewCreateListener.initTitle(mTitle);
            mViewCreateListener.initRightIcon(mRight);
            mViewCreateListener.initContent(mContent);
        }
    }

    public ViewCreateListener getmViewCreateListener() {
        return mViewCreateListener;
    }

    public void setViewCreateListener(ViewCreateListener iewCreateListener) {
        this.mViewCreateListener = iewCreateListener;
    }

    public ImageView getLeftIcon() {
        return mLeft;
    }

    public ImageView getRightIcon() {
        return mRight;
    }

    public TextView getmTitle() {
        return mTitle;
    }

    public ListView getContent() {
        return mContent;
    }

    public static class ListAdapter extends BaseAdapter {
        private boolean[] mSelected = new boolean[]{false, false, false, false, false, false, false};
        private String[] mWeek = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        private LayoutInflater mInflater;


        public interface OnItemClickListener {
            public void itemClick(View view, int position, int index);
        }

        public ListAdapter(Context context, boolean[] selected) {
            mInflater = LayoutInflater.from(context);
            mSelected = selected;
            mWeek = ResourceUtil.getStringArray(context.getResources(), R.array.week);
        }

        @Override
        public int getCount() {
            return mWeek.length;
        }

        @Override
        public Object getItem(int position) {
            return mSelected[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ItemHolder holder;
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.dialog_list_select_week, null);
                holder = new ItemHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ItemHolder) convertView.getTag();
            }
            holder.mSelect.setChecked(mSelected[position]);
            holder.mText.setText(mWeek[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mSelect.setChecked(!holder.mSelect.isChecked());
                    mSelected[position] = holder.mSelect.isChecked();
                }
            });
            return convertView;
        }

        public boolean[] getDataList() {
            return mSelected;
        }

        public void setDataList(boolean[] selected) {
            this.mSelected = selected.clone();
        }
    }

    public static class ItemHolder {
        CheckBox mSelect;
        TextView mText;

        public ItemHolder(View view) {
            mText = (TextView) view.findViewById(R.id.tv_text);
            mSelect = (CheckBox) view.findViewById(R.id.cb_selected);
        }
    }
}
