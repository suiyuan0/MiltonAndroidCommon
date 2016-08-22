package com.milton.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.milton.common.lib.R;


public class SlideView extends LinearLayout {

    private static final String TAG = SlideView.class.getSimpleName();

    private Context mContext;
    private LinearLayout mViewContent;
    private RelativeLayout mHolder;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;

    private int mHolderWidth = 120;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;
    private int mLayoutId = R.layout.layout_slide_view_default;

    private int mTouchSlop;
    private int mFirstX = 0;
    private boolean mSlidable = true;

    public interface OnSlideListener {
        public static final int SLIDE_STATUS_OFF = 0;
        public static final int SLIDE_STATUS_START_SCROLL = 1;
        public static final int SLIDE_STATUS_ON = 2;

        /**
         * @param view   current SlideView
         * @param status SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        public void onSlide(View view, int status);
    }

    public SlideView(Context context) {
        this(context, null);

    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        initView();
    }

    public SlideView(Context context, int layoutId) {
        super(context);
        if (layoutId > 0) {
            mLayoutId = layoutId;
        }
        initView();
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);

        setOrientation(LinearLayout.HORIZONTAL);
        View.inflate(mContext, mLayoutId, this);
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mHolder = (RelativeLayout) findViewById(R.id.holder);
        mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources().getDisplayMetrics()));
    }


    public void setContentView(View view) {
        mViewContent.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    public void shrinkImmediately() {
        if (getScrollX() != 0) {
            this.scrollTo(0, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHolder != null) {
            mHolderWidth = mHolder.getMeasuredWidth();
        }
    }

    public void onRequireTouchEvent(MotionEvent event) {
        if (!mSlidable) {
            return;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mFirstX = x;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_START_SCROLL);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (Math.abs(x - mFirstX) < mTouchSlop) {
                    break;
                }
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }

                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (Math.abs(x - mFirstX) < mTouchSlop) {
                    break;
                }
                int newScrollX = 0;
                if (scrollX - mHolderWidth * 0.4 > 0) {
                    newScrollX = mHolderWidth;
                }
                this.smoothScrollTo(newScrollX, 0);
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this,
                            newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
                                    : OnSlideListener.SLIDE_STATUS_ON);
                }
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        // 缓慢滚动到指定位置
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setSlidable(boolean slidable) {
        mSlidable = slidable;
    }

    public boolean isSlidable() {
        return mSlidable;
    }

    public void setSlided(boolean slided) {
        if (slided) {
            this.smoothScrollTo(mHolderWidth, 0);
        } else {
            shrink();
        }

    }

    public boolean isSlided() {
        return getScrollX() == mHolderWidth;
    }
}
