package com.milton.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.milton.common.lib.R;
import com.milton.common.util.ResourceUtil;

public class CircleSeekBar extends View {
    private final static String TAG = CircleSeekBar.class.getSimpleName();
    private final static String UNIT = "℃";
    private Context mContext;
    private AttributeSet mAttrs;

    private Drawable mPointDrawable;
    private Drawable mThumbDrawable;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;

    private Paint mstartEndPaint;

    private Paint mCirclePaint = null;
    private int mProgressMax = 25;
    private int mProgressMin = 15;
    private Paint mSeekBarBackgroundPaint = null;
    private Paint mSeekBarFrontPaint = null;
    private RectF mArcRectF = null;

    private boolean mIsShowProgressText = false;
    private Paint mProgressTextPaint = null;
    private int mProgressTextSize;

    private int mViewHeight = 0;
    private int mViewWidth = 0;
    private int mSeekBarSize = 0;
    private int mSeekBarRadius = 0;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    private float mThumbLeft = 0;
    private float mThumbTop = 0;


    private float mPointLeft = 0;
    private float mPointTop = 0;

    private float mSeekBarDegree = 0.0F;
    private int mCurrentProgress = -1;

    private OnSeekBarChangeListener onSeekBarChangeListener;

    double mDelta;
    int mTextWidth;
    int mTextHeight;
    private float startAngle = 150F;
    private float sweepAngle = 240F;
    private int minTextWidth = 0;
    private int minTextHeight = 0;
    private int maxTextWidth = 0;
    private int maxTextHeight = 0;

    private int deltaRadius = 0;

    int slop = 10;

    public CircleSeekBar(Context context) {
        this(context, null);
    }

    public CircleSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CircleSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAttrs = attrs;
        initView();
    }

    private void initView() {

        TypedArray ta = mContext.obtainStyledAttributes(mAttrs, R.styleable.CircleSeekBar);
        //thumb的属性是使用android:thumb属性进行设置的。
        //返回的Drawable为一个StateListDrawable类型，即可以实现选中效果的Drawable_list
        //mthumbnNormal和mThumbPressed则是用于设置不同状态的效果，当点击thumb时设置mThumbPressed，否则设置mThumbNormal

        mThumbDrawable = ta.getDrawable(R.styleable.CircleSeekBar_android_thumb);
        mThumbWidth = mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = mThumbDrawable.getIntrinsicHeight();
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, android.R.attr.state_selected, android.R.attr.state_checked};

        mPointDrawable = ta.getDrawable(R.styleable.CircleSeekBar_android_background);

        float progressWidth = ta.getDimensionPixelOffset(R.styleable.CircleSeekBar_progress_width, 5);
        int progressBackgroundColor = ta.getColor(R.styleable.CircleSeekBar_progress_backgroud, Color.GRAY);
        int progressFrontColor = ta.getColor(R.styleable.CircleSeekBar_progress_front, Color.BLUE);
        int circleColor = ta.getColor(R.styleable.CircleSeekBar_progress_circle_color, Color.GRAY);

        mProgressMax = ta.getInteger(R.styleable.CircleSeekBar_progress_max, 30);
        mProgressMin = ta.getInteger(R.styleable.CircleSeekBar_progress_min, 10);

        mstartEndPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mstartEndPaint.setColor(Color.GRAY);
        mstartEndPaint.setTextAlign(Paint.Align.CENTER);
        mstartEndPaint.setTextSize(ResourceUtil.dp2px(getContext(), 15));

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekBarFrontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeekBarBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCirclePaint.setColor(circleColor);
        mSeekBarFrontPaint.setColor(progressFrontColor);
        mSeekBarBackgroundPaint.setColor(progressBackgroundColor);
        //去除锯齿
        mCirclePaint.setAntiAlias(true);
        mSeekBarFrontPaint.setAntiAlias(true);
        mSeekBarBackgroundPaint.setAntiAlias(true);

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mSeekBarFrontPaint.setStyle(Paint.Style.STROKE);
        mSeekBarBackgroundPaint.setStyle(Paint.Style.STROKE);
        //空心线宽
        mCirclePaint.setStrokeWidth(progressWidth);
        mSeekBarFrontPaint.setStrokeWidth(progressWidth);
        mSeekBarBackgroundPaint.setStrokeWidth(progressWidth);
        //构造一个矩形
        mArcRectF = new RectF();

        mIsShowProgressText = ta.getBoolean(R.styleable.CircleSeekBar_show_progress_text, false);

        int progressTextStroke = (int) ta.getDimension(R.styleable.CircleSeekBar_progress_text_stroke_width, 5);
        int progressTextColor = ta.getColor(R.styleable.CircleSeekBar_progress_text_color, Color.GREEN);

        mProgressTextSize = ta.getDimensionPixelSize(R.styleable.CircleSeekBar_progress_text_size, 50);

        mProgressTextPaint = new Paint();
        mProgressTextPaint.setColor(progressTextColor);
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mProgressTextPaint.setStrokeWidth(progressTextStroke);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        String text = "3333" + UNIT;
        Rect textBounds = new Rect();
        mProgressTextPaint.getTextBounds(text, 0, text.length(), textBounds);
        mDelta = Math.sqrt(textBounds.width() * textBounds.width() + textBounds.height() * textBounds.height()) / 2;
        mTextHeight = textBounds.height();
        mTextWidth = textBounds.width();

        text = mProgressMax + UNIT;
        mstartEndPaint.getTextBounds(text, 0, text.length(), textBounds);
        maxTextHeight = textBounds.height();
        maxTextWidth = textBounds.width();

        text = mProgressMin + UNIT;
        mstartEndPaint.getTextBounds(text, 0, text.length(), textBounds);
        minTextHeight = textBounds.height();
        minTextWidth = textBounds.width();

        deltaRadius = ResourceUtil.dp2px(getContext(), 25);

        mSeekBarDegree = startAngle;
        ta.recycle();
//        setLayoutTextView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //确定绘制view的大小，留点距离 是为了 外围的指针能够放进来
        mViewWidth = getWidth() - mTextWidth * 8 / 5;
        mViewHeight = getHeight() - mTextWidth * 8 / 5;

        //取小的值
        mSeekBarSize = mViewWidth > mViewHeight ? mViewHeight : mViewWidth;

        mSeekBarCenterX = getWidth() / 2;
        mSeekBarCenterY = getHeight() / 2;
        mSeekBarRadius = mSeekBarSize / 2 - mThumbWidth / 2;

        int left = mSeekBarCenterX - mSeekBarRadius;
        int right = mSeekBarCenterX + mSeekBarRadius;

        int top = mSeekBarCenterY - mSeekBarRadius;
        int bottom = mSeekBarCenterY + mSeekBarRadius;

        mArcRectF.set(left, top, right, bottom);
        //设置从左下开始
        setThumbPositon(Math.toRadians(mSeekBarDegree + startAngle));
        setCursorPositon(Math.toRadians(mSeekBarDegree + startAngle));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景圆环
        canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius - deltaRadius, mCirclePaint);
        //绘制进度条
        canvas.drawArc(this.mArcRectF, startAngle, sweepAngle, false, mSeekBarBackgroundPaint);
        //绘制进度条
        canvas.drawArc(this.mArcRectF, startAngle, mSeekBarDegree, false, mSeekBarFrontPaint);
        //绘制指针
        drawProgressCursor(canvas);
        //绘制光标
        drawThumBitmap(canvas);
        drawStartEndTemp(canvas);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                seekTo(eventX, eventY, false, false);
                break;
            case MotionEvent.ACTION_UP:
                seekTo(eventX, eventY, true, false);
                break;
            case MotionEvent.ACTION_MOVE:
                seekTo(eventX, eventY, false, true);
                break;
        }

        return true;
    }

    private void seekTo(float eventX, float eventY, boolean isUp, boolean isMove) {
        if (isPointOnThumb(eventX, eventY) && !isUp) {
            mThumbDrawable.setState(mThumbPressed);
            double radian = Math.atan2(eventY - mSeekBarCenterY, eventX - mSeekBarCenterX);
            /*
             * 由于atan2返回的值为[-pi,pi]
             * 因此需要将弧度值转换一下，使得区间为[0,2*pi]
             */
            if (radian < 0) {
                radian = radian + 2 * Math.PI;
            }

            float temp = (float) Math.toDegrees(radian);
            if (temp > 0 && temp < 90) {
                temp = temp + 360;
            }
            mSeekBarDegree = temp - startAngle;

//            if (mSeekBarDegree > sweepAngle + slop) {
//                mSeekBarDegree = sweepAngle + 1;
//                return;
//            } else
            if (mSeekBarDegree > sweepAngle) {
                mSeekBarDegree = sweepAngle;
                radian = Math.toRadians(startAngle + sweepAngle);
            }
//            else if (mSeekBarDegree < 0 - slop) {
//                mSeekBarDegree = -1;
//                return;
//            }
            else if (mSeekBarDegree < 0) {
                mSeekBarDegree = 0;
                radian = Math.toRadians(startAngle);
            }
            setThumbPositon(radian);
            setCursorPositon(radian);
            mCurrentProgress = (int) ((mProgressMax - mProgressMin) * mSeekBarDegree / sweepAngle + mProgressMin + 0.5);

            //move 和 down 事件
            if (isMove) {
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(this, mCurrentProgress);
                }
            } else {
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onStartTrackingTouch(this);
                }
            }
            invalidate();
        } else if (isUp) {
//            LogUtil.e("alinmi21", " isUp = true mSeekBarDegree = " + mSeekBarDegree);
            if (mSeekBarDegree > sweepAngle) {
//                mSeekBarDegree = sweepAngle;
                return;
            } else if (mSeekBarDegree < 0) {
//                mSeekBarDegree = 0;
                return;
            }
            mThumbDrawable.setState(mThumbNormal);
            if (onSeekBarChangeListener != null) {
                onSeekBarChangeListener.onStopTrackingTouch(this, mCurrentProgress);
            }
            invalidate();
        }
    }

    private String getTemperature() {
        if (mCurrentProgress == -1) {
            return "";
        } else {
            return mCurrentProgress + UNIT;
        }
    }

    private boolean isPointOnThumb(float eventX, float eventY) {
        boolean result = false;
        double distance = Math.sqrt(Math.pow(eventX - mSeekBarCenterX, 2) + Math.pow(eventY - mSeekBarCenterY, 2));
        if (distance < mSeekBarSize && distance > (mSeekBarSize / 2 - mThumbWidth)) {
            result = true;
        }
        return result;

    }

    private void setThumbPositon(double radian) {
        double X = mSeekBarCenterX + mSeekBarRadius * Math.cos(radian);
        double Y = mSeekBarCenterY + mSeekBarRadius * Math.sin(radian);
        mThumbLeft = (float) (X - mThumbWidth / 2);
        mThumbTop = (float) (Y - mThumbHeight / 2);
    }

    private void setCursorPositon(double radian) {
        double X = mSeekBarCenterX + (mSeekBarRadius + mDelta) * Math.cos(radian);
        double Y = mSeekBarCenterY + (mSeekBarRadius + mDelta) * Math.sin(radian) + (radian < Math.PI ? 0.0 : maxTextHeight * (1 + Math.cos(radian - Math.PI)));
        mPointLeft = (float) (X);
        mPointTop = (float) (Y);
    }


    private void drawThumBitmap(Canvas canvas) {
        if (mCurrentProgress != -1) {
            this.mThumbDrawable.setBounds((int) mThumbLeft, (int) mThumbTop, (int) (mThumbLeft + mThumbWidth), (int) (mThumbTop + mThumbHeight));
            this.mThumbDrawable.draw(canvas);
        }
    }

    private void drawStartEndTemp(Canvas canvas) {
        double radians = Math.toRadians(180 - startAngle);
        double X = mSeekBarCenterX + mSeekBarRadius * Math.cos(radians);
        double Y = mSeekBarCenterY + mSeekBarRadius * Math.sin(radians) + maxTextHeight * 1.5;

        //终点
        canvas.drawText(mProgressMax + UNIT, ((float) X), (float) Y, mstartEndPaint);
        double M = mSeekBarCenterX - mSeekBarRadius * Math.cos(radians);
        double N = mSeekBarCenterY + mSeekBarRadius * Math.sin(radians) + minTextHeight * 1.5;
        //起点
        canvas.drawText(mProgressMin + UNIT, ((float) M), (float) N, mstartEndPaint);
    }

    private void drawProgressCursor(Canvas canvas) {
        if (mCurrentProgress != -1) {
            canvas.drawText(mCurrentProgress + UNIT, mPointLeft, mPointTop, mProgressTextPaint);
        }
    }


    public void setProgress(int progress) {
        if (progress > mProgressMax) {
            progress = mProgressMax;

        }
        if (progress < mProgressMin) {
            progress = mProgressMin;
        }
        mCurrentProgress = progress;
        mSeekBarDegree = ((progress - mProgressMin) * 1.0f / (mProgressMax - mProgressMin) * sweepAngle);
//        LogUtil.e("alinmi25", "mSeekBarDegree = " + mSeekBarDegree + " , Math.toRadians(mSeekBarDegree+startAngle) = " + Math.toRadians(mSeekBarDegree + startAngle));
        setThumbPositon(Math.toRadians(mSeekBarDegree + startAngle));
        setCursorPositon(Math.toRadians(mSeekBarDegree + startAngle));
        invalidate();
    }

    public void setProgressFrontColor(int color) {
        mSeekBarFrontPaint.setColor(color);
    }

    public int getCurrentProgress() {
        return mCurrentProgress;
    }

    public void setSeekBarChangerListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.onSeekBarChangeListener = onSeekBarChangeListener;
    }


    public interface OnSeekBarChangeListener {

        void onProgressChanged(CircleSeekBar seekBar, int progress);


        void onStartTrackingTouch(CircleSeekBar seekBar);


        void onStopTrackingTouch(CircleSeekBar seekBar, int progress);
    }


}

