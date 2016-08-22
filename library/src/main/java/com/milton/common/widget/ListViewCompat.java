package com.milton.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


public class ListViewCompat extends ListView {

    private static final String TAG = ListViewCompat.class.getSimpleName();

    private SlideView mFocusedItemView;

    public ListViewCompat(Context context) {
        super(context);
    }

    public ListViewCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void shrinkListItem(int position) {
        View item = getChildAt(position);

        if (item != null) {
            try {
                ((SlideView) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean mIsXScrolled;
    private boolean mFirstCheck;
    private int mFirstX;
    private int mFirstY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mIsXScrolled = false;
                mFirstCheck = true;
                int x = (int) event.getX();
                int y = (int) event.getY();
                mFirstX = x;
                mFirstY = y;
                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    final View view = getChildAt(position - getFirstVisiblePosition());
                    if (view instanceof SlideView) {
                        mFocusedItemView = (SlideView) getChildAt(position - getFirstVisiblePosition());
                    }
                }
            }
            break;
            case MotionEvent.ACTION_MOVE:
//                final float deltaX2 = Math.abs(event.getX() - mFirstX);
//                final float deltaY2 = Math.abs(event.getY() - mFirstY);
//                LogUtil.e("alinmi37", "move mFirstCheck = " + mFirstCheck + " , mFirstX = " + mFirstX + " , mFirstY = " + mFirstY + " , event.getX() = " + event.getX() + " , event.getY() = " + event.getY() + " , deltaX2 = " + deltaX2 + " , deltaY2 = " + deltaY2);
                if (mFirstCheck) {
                    final float deltaX = Math.abs(event.getX() - mFirstX);
                    final float deltaY = Math.abs(event.getY() - mFirstY);
                    if (deltaX > 2 || deltaY > 2) {
                        mIsXScrolled = deltaX > deltaY;
                        mFirstCheck = false;
                    }
                }
                break;
            default:
                break;
        }
//        LogUtil.e("alinmi31", "mFocusedItemView = " + mFocusedItemView + "mIsXScrolled = " + mIsXScrolled);
        if (mFocusedItemView != null && mIsXScrolled) {
            mFocusedItemView.onRequireTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return (mFocusedItemView != null && mIsXScrolled) || super.onTouchEvent(event);
    }

}
