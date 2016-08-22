package com.milton.common.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mark on 10/13/14.
 */
public class TimeTickerView extends TextView {

    private TickTimer timer;
    private int totalLeavingTime;

    public TimerListener listener;
    int mStartTime;
    int mTotalTime;

    public TimeTickerView(Context context) {
        super(context);
    }

    public TimeTickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(int startTime, int totalTime) {
        mStartTime = startTime;
        mTotalTime = totalTime;
        totalLeavingTime = totalTime - startTime;
    }

    /**
     * call in onDisplay()
     */
    public void start() {
        if (timer != null) timer.cancel();
        if (totalLeavingTime > 0L) {
            timer = new TickTimer(totalLeavingTime * 1000, 1000, mTotalTime);
            timer.start();
        }
    }

    public void setOnTimerListener(TimerListener listener) {
        this.listener = listener;
    }

    public void start(int startTime, int totalTime) {
        init(startTime, totalTime);
        start();
    }

    public void start(int startTime, int totalTime, TimerListener listener) {
        start(startTime, totalTime);
        setOnTimerListener(listener);
    }


    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void cancel() {
        if (timer != null) {
            totalLeavingTime = timer.leaving;
            mStartTime = mTotalTime - totalLeavingTime;
            timer.cancel();
            timer = null;
        }
    }

    private class TickTimer extends CountDownTimer {
        int leaving;
        int totalTime;

        public TickTimer(long millisInFuture, long countDownInterval, int totalTime) {
            super(millisInFuture, countDownInterval);
            this.totalTime = totalTime;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leaving = (int) (millisUntilFinished / 1000);
            setShow(leaving);
            if (listener != null) {
                listener.onProgress((totalTime - leaving) * 100 / totalTime, totalTime - leaving);
            }
        }

        @Override
        public void onFinish() {
            stop();
            if (listener != null) {
                listener.onFinish();
            }
        }

        private void setShow(int left) {

            setText(formatTime(mTotalTime - left) + "|" + formatTime(mTotalTime));
        }

    }


    public static String formatTime(int t) {
        final int minute = t / 60;
        final int second = t % 60;
        StringBuffer buffer = new StringBuffer((minute < 10 ? "0" : "") + minute).append(":").append((second < 10 ? "0" : "") + second);
        return buffer.toString();
    }

    public interface TimerListener {
        public void onFinish();

        public void onProgress(int progress, int currentTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }
}
