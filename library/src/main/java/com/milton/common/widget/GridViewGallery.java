package com.milton.common.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.milton.common.lib.R;
import com.milton.common.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class GridViewGallery extends LinearLayout {
    int mColumns = 4;
    int mRows = 3;
    private Context context;
    private ViewPager viewPager;
    private LinearLayout ll_dot;
    private ImageView[] dots;
    private int currentIndex;
    private int viewPager_size;
    private int pageItemCount = 12;

    private List<View> list_Views;
    List<ViewPager_GV_ItemAdapter> mAdapters = new ArrayList<>();
    public static final String TAG = GridViewGallery.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private BaseAdapter mAdapter;

    int mHorizontalSpacing = -1;
    int mVerticalSpacing = -1;

    public GridViewGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gridview_gallery, this);
        viewPager = (ViewPager) view.findViewById(R.id.vPager);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_channel_dots);
    }

    private void setAdapter() {
        if (mAdapter == null) {
            return;
        }
        list_Views = new ArrayList<>();
        for (int i = 0; i < viewPager_size; i++) {
            list_Views.add(getViewPagerItem(i));
        }
        viewPager.setAdapter(new ViewPager_GridView_Adapter(list_Views));
    }

    private void initDots() {

        pageItemCount = mColumns * mRows;
        viewPager_size = mAdapter.getCount() / pageItemCount + 1;

        if (0 < viewPager_size) {
            ll_dot.removeAllViews();
            ll_dot.setVisibility(View.VISIBLE);
            for (int j = 0; j < viewPager_size; j++) {
                ImageView image = new ImageView(context);
                LayoutParams params = new LayoutParams(ResourceUtil.dp2px(getContext(), 6), ResourceUtil.dp2px(getContext(), 6));
                params.setMargins(ResourceUtil.dp2px(getContext(), 6), 0, ResourceUtil.dp2px(getContext(), 6), 0);
                image.setBackgroundResource(R.drawable.dot_unselected);
                ll_dot.addView(image, params);
            }
        }
//        if (viewPager_size != 1) {
        dots = new ImageView[viewPager_size];
        for (int i = 0; i < viewPager_size; i++) {
            dots[i] = (ImageView) ll_dot.getChildAt(i);
            // dots[i].setEnabled(true);
            // dots[i].setTag(i);
        }
        currentIndex = 0;
        // dots[currentIndex].setEnabled(false);
        dots[currentIndex].setBackgroundResource(R.drawable.dot_selected);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                setCurDot(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
//        }
    }

    private void setCurDot(int positon) {
        if (positon < 0 || positon > viewPager_size - 1 || currentIndex == positon) {
            return;
        }
        for (int i = 0; i < dots.length; i++) {
            dots[i].setBackgroundResource(R.drawable.dot_unselected);
        }
        // dots[positon].setEnabled(false);
        // dots[currentIndex].setEnabled(true);
        dots[positon].setBackgroundResource(R.drawable.dot_selected);
        currentIndex = positon;
    }

    private View getViewPagerItem(int index) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_viewpager_gridview, null);
        GridView gridView = (GridView) layout.findViewById(R.id.vp_gv);
        if (-1 != mHorizontalSpacing) {
            gridView.setHorizontalSpacing(mHorizontalSpacing);
        }
        if (-1 != mVerticalSpacing) {
            gridView.setVerticalSpacing(mVerticalSpacing);
        }
        gridView.setNumColumns(mColumns);

        ViewPager_GV_ItemAdapter adapter = new ViewPager_GV_ItemAdapter(context, mAdapter, index, pageItemCount);
        mAdapters.add(adapter);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onItemClick(parent, view, position + currentIndex * pageItemCount, id);
                }
            }
        });
        return gridView;
    }

    public void notifyDataSetChanged() {
        if (mAdapters != null) {
            for (int i = 0; i < mAdapters.size(); i++) {
                mAdapters.get(i).notifyDataSetChanged();
            }
        }
    }

    public void setCurrentIndex(int index) {
        viewPager.setCurrentItem(index);
    }

    public int getCurrentIndex() {
        return viewPager.getCurrentItem();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        setAdapter();
    }

    public void setAdapter(BaseAdapter adapter) {
        mAdapter = adapter;
        initDots();
        setAdapter();
    }

    public void setNumColumns(int columns) {
        mColumns = columns;
        initDots();
        setAdapter();
    }

    public void setNumRows(int rows) {
        mRows = rows;
        initDots();
        setAdapter();
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != mHorizontalSpacing) {
            mHorizontalSpacing = horizontalSpacing;
        }
    }

    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != mVerticalSpacing) {
            mVerticalSpacing = verticalSpacing;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public class ViewPager_GV_ItemAdapter extends BaseAdapter {

        BaseAdapter mAdapter;
        private Context context;
        private int index;
        private int pageItemCount;
        private int totalSize;
        private int listIndex;

        public ViewPager_GV_ItemAdapter(Context context, BaseAdapter adapter, int index, int pageItemCount) {
            this.context = context;
            this.index = index;
            this.pageItemCount = pageItemCount;
            totalSize = adapter.getCount();
            listIndex = index * pageItemCount;
            mAdapter = adapter;

        }

        @Override
        public int getCount() {
            int size = totalSize / pageItemCount;
            if (index == size)
                return totalSize - pageItemCount * index;
            else
                return pageItemCount;
        }

        @Override
        public Object getItem(int position) {
            return mAdapter.getItem(listIndex + position);
        }

        @Override
        public long getItemId(int position) {
            return listIndex + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return mAdapter.getView(listIndex + position, convertView, parent);
        }

    }

    public class ViewPager_GridView_Adapter extends PagerAdapter {

        private List<View> lists;

        public ViewPager_GridView_Adapter(List<View> data) {
            lists = data;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            // return false;
            return arg0 == (arg1);
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ViewGroup parent = (ViewGroup) lists.get(arg1).getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                // container.addView(v);
                ((ViewPager) arg0).addView(lists.get(arg1), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return lists.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            try {
                ((ViewPager) arg0).removeView(lists.get(arg1));
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

    }
}
