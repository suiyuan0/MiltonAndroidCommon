package com.milton.common.widget.refreshlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.milton.common.lib.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshHeadListView extends ListView implements OnScrollListener {

    private int downY;        // 按下时y轴的偏移量
    private View headerView;        // 头布局
    private int headerViewHeight;    // 头布局的高度
    private int firstVisibleItemPosition;        // 滚动时界面显示在顶部的item的position
    private DisplayMode currentState = DisplayMode.Pull_Down;        // 头布局当前的状态, 缺省值为下拉状态
    private Animation upAnim;        // 向上旋转的动画
    private Animation downAnim;        // 向下旋转的动画
    private ImageView ivArrow;        // 头布局的箭头
    private TextView tvState;        // 头布局刷新状态
    private ProgressBar mProgressBar;    // 头布局的进度条
    private TextView tvLastUpdateTime;    // 头布局的最后刷新时间
    private OnRefreshListener mOnRefreshListener;
    private boolean isScroll2Bottom = false;    // 是否滚动到底部
    private View footerView;        // 脚布局
    private int footerViewHeight;    // 脚布局的高度
    private boolean isLoadMoving = false;    // 是否正在加载更多中
    OnScrollListener mOnScrollListener = null;

    public RefreshHeadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
        this.setOnScrollListener(this);
    }

    public void setOnMyScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    /**
     * 初始化脚布局
     */
    private void initFooter() {
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview_refresh_footer, null);

        measureView(footerView);        // 测量一下脚布局的高度

        footerViewHeight = footerView.getMeasuredHeight();

        footerView.setPadding(0, -footerViewHeight, 0, 0);        // 隐藏脚布局

        this.addFooterView(footerView);
    }

    /**
     * 初始化头布局
     */
    private void initHeader() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.activity_listview_refresh_header, null);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_down_arrow);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_listview_header_progress);
        tvState = (TextView) headerView.findViewById(R.id.tv_listview_header_state);
        tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_listview_header_last_update_time);

        ivArrow.setMinimumWidth(50);
        tvLastUpdateTime.setText("最后更新: " + getLastUpdateTime());

        measureView(headerView);
        headerViewHeight = headerView.getMeasuredHeight();
        Log.i("RefreshListView", "头布局的高度: " + headerViewHeight);

        headerView.setPadding(0, -headerViewHeight, 0, 0);

        this.addHeaderView(headerView);

        initAnimation();
    }

    /**
     * 获得最后刷新的时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnim = new RotateAnimation(
                0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        upAnim.setDuration(500);
        upAnim.setFillAfter(true);

        downAnim = new RotateAnimation(
                -180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        downAnim.setDuration(500);
        downAnim.setFillAfter(true);
    }

    /**
     * 测量给定的View的宽和高, 测量之后, 可以得到view的宽和高
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);

        int lpHeight = lp.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                if (currentState == DisplayMode.Refreshing) {
                    // 当前的状态是正在刷新中, 不执行下拉操作
                    break;
                }

                int moveY = (int) ev.getY();    // 移动中的y轴的偏移量

                int diffY = moveY - downY;

                int paddingTop = -headerViewHeight + (diffY / 2);

                if (firstVisibleItemPosition == 0
                        && paddingTop > -headerViewHeight) {

                    /**
                     * paddingTop > 0   完全显示
                     * currentState == DisplayMode.Pull_Down 当是在下拉状态时
                     */
                    if (paddingTop > 0    // 完全显示
                            && currentState == DisplayMode.Pull_Down) {        // 完全显示, 进入到刷新状态
                        Log.i("RefreshListView", "松开刷新");
                        currentState = DisplayMode.Release_Refresh;        // 把当前的状态改为松开刷新
                        refreshHeaderViewState();
                    } else if (paddingTop < 0
                            && currentState == DisplayMode.Release_Refresh) {        // 没有完全显示, 进入到下拉状态
                        Log.i("RefreshListView", "下拉刷新");
                        currentState = DisplayMode.Pull_Down;
                        refreshHeaderViewState();
                    }

                    headerView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downY = -1;

                if (currentState == DisplayMode.Pull_Down) {        // 松开时, 当前显示的状态为下拉状态, 执行隐藏headerView的操作

                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                } else if (currentState == DisplayMode.Release_Refresh) {    // 松开时, 当前显示的状态为松开刷新状态, 执行刷新的操作
                    headerView.setPadding(0, 0, 0, 0);
                    currentState = DisplayMode.Refreshing;
                    refreshHeaderViewState();

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onRefresh();
                    }
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 当刷新任务执行完毕时, 回调此方法, 去刷新界面
     */
    public void onRefreshFinish() {
        if (isLoadMoving) {    // 隐藏脚布局
            isLoadMoving = false;
            isScroll2Bottom = false;
            footerView.setPadding(0, -footerViewHeight, 0, 0);
        } else {    // 隐藏头布局
            headerView.setPadding(0, -headerViewHeight, 0, 0);
            mProgressBar.setVisibility(View.GONE);
            ivArrow.setVisibility(View.VISIBLE);
            tvLastUpdateTime.setText("最后更新: " + getLastUpdateTime());
            currentState = DisplayMode.Pull_Down;
            mHeaderViewVisible = true;
            getPinnedHeaderView().setVisibility(View.VISIBLE);
        }
    }

    /**
     * 刷新头布局的状态
     */
    private void refreshHeaderViewState() {
        if (currentState == DisplayMode.Pull_Down) {    // 当前进入下拉状态
            ivArrow.startAnimation(downAnim);
            tvState.setText("下拉可以刷新");
            mHeaderViewVisible = false;
            getPinnedHeaderView().setVisibility(View.INVISIBLE);
        } else if (currentState == DisplayMode.Release_Refresh) { //当前进入松开刷新状态
            ivArrow.startAnimation(upAnim);
            tvState.setText("松开立即刷新");
        } else if (currentState == DisplayMode.Refreshing) {  //当前进入正在刷新中
            ivArrow.clearAnimation();
            ivArrow.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            tvState.setText("正在刷新数据中...");
        }
    }

    /**
     * 当ListView滚动状态改变时回调
     * SCROLL_STATE_IDLE		// 当ListView滚动停止时
     * SCROLL_STATE_TOUCH_SCROLL // 当ListView触摸滚动时
     * SCROLL_STATE_FLING		// 快速的滚动(手指快速的触摸移动)
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
            if (isScroll2Bottom && !isLoadMoving) {        // 滚动到底部
                // 加载更多
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());        // 滚动到ListView的底部
                isLoadMoving = true;

                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onLoadMoring();
                }
            }
        }
    }

    /**
     * 当ListView滚动时触发
     * firstVisibleItem 屏幕上显示的第一个Item的position
     * visibleItemCount 当前屏幕显示的总个数
     * totalItemCount   ListView的总条数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        Log.i("RefreshListView", "onScroll: " + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount);

        if ((firstVisibleItem + visibleItemCount) >= totalItemCount
                && totalItemCount > 0) {
//			Log.i("RefreshListView", "加载更多");
            isScroll2Bottom = true;
        } else {
            isScroll2Bottom = false;
        }
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * @author andong
     *         下拉头部的几种显示状态
     */
    public enum DisplayMode {
        Pull_Down, // 下拉刷新的状态
        Release_Refresh, // 松开刷新的状态
        Refreshing    // 正在刷新中的状态
    }

    /**
     * 设置刷新的监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public interface HeaderAdapter {
        public static final int HEADER_GONE = 0;
        public static final int HEADER_VISIBLE = 1;
        public static final int HEADER_PUSHED_UP = 2;

        int getHeaderState(int position);

        void configureHeader(View header, int position, int alpha);
    }

    private static final int MAX_ALPHA = 255;

    private HeaderAdapter mAdapter;
    private View mHeaderView;
    protected boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;


    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mHeaderView != null) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        if (mHeaderView != null) {
            // listview的上边和下边有黑色的阴影。xml中： android:fadingEdge="none"
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    public View getPinnedHeaderView() {
        return mHeaderView;
    }

    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (HeaderAdapter) adapter;
    }

    public void configureHeaderView(int position) {

        if (mHeaderView == null) {
            return;
        }
        //Header
        if (position == 0) {
            mHeaderViewVisible = false;
            return;
        }
        position = position - 1;
        int state = mAdapter.getHeaderState(position);
        switch (state) {
            case HeaderAdapter.HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case HeaderAdapter.HEADER_VISIBLE: {
                mAdapter.configureHeader(mHeaderView, position, MAX_ALPHA);
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case HeaderAdapter.HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                int alpha;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }
                mAdapter.configureHeader(mHeaderView, position, alpha);
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}
