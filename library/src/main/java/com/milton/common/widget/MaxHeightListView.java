package com.milton.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class MaxHeightListView extends ListView {

    private static final String TAG = MaxHeightListView.class.getSimpleName();


    public MaxHeightListView(Context context) {
        super(context);
    }

    public MaxHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * listview高度
     */
    private int listViewHeight = -1;
    private int listViewWidth = -1;

    public int getListViewHeight() {
        return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }

    public void setListViewWidth(int listViewwidth) {
        this.listViewWidth = listViewWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (listViewHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight, MeasureSpec.AT_MOST);
        }
        if (listViewWidth > -1) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(listViewWidth, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
