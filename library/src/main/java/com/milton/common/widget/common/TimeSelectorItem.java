package com.milton.common.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.milton.common.lib.R;
import com.milton.common.pickerview.TimePickerView;
import com.milton.common.util.time.TimeUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by milton on 16/6/7.
 */
public class TimeSelectorItem extends RelativeLayout {
    public static final int TYPE_SECOND = 0;
    public static final int TYPE_HOUR_MINUTE = 1;
    private int mType = TYPE_SECOND;
    private TextView mName;
    private TextView mContent;
    private TimePickerView mTimeSelector;
    private int mTimeSecond = 0;
    private String mTime = "";
    private OnTimeSelectListener mOnTimeSelectListener;

    public TimeSelectorItem(Context context) {
        super(context);
    }

    public TimeSelectorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_time_selector_item, this);

    }

    public TimeSelectorItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mName = (TextView) findViewById(R.id.tv_name);
        mContent = (TextView) findViewById(R.id.tv_content);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });
    }

    public void setName(int resId) {
        setName(getResources().getString(resId));
    }

    public void setName(String name) {
        initName();
        if (mName != null) {
            mName.setText(name);
        }
    }

    public void setTime(int time) {
        mType = TYPE_SECOND;
        mTimeSecond = time;
        initTime();
        if (mContent != null) {
            mContent.setText(mTimeSecond + "s");
        }
    }

    public int getTimeSecond() {
        return mTimeSecond;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mType = TYPE_HOUR_MINUTE;
        mTime = time;
        initTime();
        if (mContent != null) {
            mContent.setText(mTime);
        }
    }

    public TextView getContent() {
        return mContent;
    }


    public void setOnTimeSelectListener(OnTimeSelectListener listener) {
        mOnTimeSelectListener = listener;
    }

    private void showTimeSelector() {
        if (null == mTimeSelector) {
            mTimeSelector = new TimePickerView(getContext(), mType == TYPE_SECOND ? TimePickerView.Type.MIN_SECOND : TimePickerView.Type.HOURS_MINS);
            mTimeSelector.setCyclic(true);
            mTimeSelector.setCancelable(true);
            mTimeSelector.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date) {
                    if (mType == TYPE_SECOND) {
                        final int seconds = getTimeSeconds(date);
                        if (mOnTimeSelectListener != null) {
                            mOnTimeSelectListener.timeSelect(seconds);
                        }
                        setTime(seconds);
                    } else if (mType == TYPE_HOUR_MINUTE) {
                        final String time = TimeUtil.dateToStr(date, TimeUtil.FORMAT_HOUR_MINUTE);
                        if (mOnTimeSelectListener != null) {
                            mOnTimeSelectListener.timeSelect(time);
                        }
                        setTime(time);

                    }
                }
            });
        }
        if (mType == TYPE_SECOND) {
            mTimeSelector.setTime(mTimeSecond);
        } else if (mType == TYPE_HOUR_MINUTE) {
            mTimeSelector.setTime(TimeUtil.str2Date(mTime, TimeUtil.FORMAT_HOUR_MINUTE));
        }
        mTimeSelector.show();
    }

    private int getTimeSeconds(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null)
            calendar.setTimeInMillis(System.currentTimeMillis());
        else {
            calendar.setTime(date);
        }
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return minute * 60 + second;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    private void initName() {
        if (mName == null) {
            mName = (TextView) findViewById(R.id.tv_name);
        }
    }

    private void initTime() {
        if (mContent == null) {
            mContent = (TextView) findViewById(R.id.tv_content);
        }
    }

    public interface OnTimeSelectListener {
        public void timeSelect(int seconds);

        public void timeSelect(String time);
    }
}
