package com.milton.common.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.milton.common.lib.R;
import com.milton.common.pickerview.adapter.NumericWheelAdapter;
import com.milton.common.pickerview.lib.WheelView;
import com.milton.common.pickerview.view.BasePickerView;


/**
 * Created by Milton Lin.
 */
public class NumberPickerView extends BasePickerView implements View.OnClickListener {

    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private OnNumberSelectListener mNumberSelectListener;
    private WheelView mNumber;

    public NumberPickerView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.pickerview_number, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        mNumber = (WheelView) findViewById(R.id.number);

    }

    /**
     * 设置可以数字的范围
     *
     * @param start
     * @param end
     */
    public void setRange(int start, int end) {
        mNumber.setAdapter(new NumericWheelAdapter(start, end));
    }

    /**
     * 设置选中时间
     *
     * @param value
     */
    public void setNumber(int value) {
        mNumber.setCurrentItem(value - (int) mNumber.getAdapter().getItem(0));

    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        mNumber.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
            return;
        } else {
            if (mNumberSelectListener != null) {
                mNumberSelectListener.onNumberSelect((int) mNumber.getAdapter().getItem(mNumber.getCurrentItem()));
//                try {
//                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
//                    timeSelectListener.onTimeSelect(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }
            dismiss();
            return;
        }
    }

    public interface OnNumberSelectListener {
        public void onNumberSelect(int num);
    }

    public void setOnNubmerSelectListener(OnNumberSelectListener numberSelectListener) {
        this.mNumberSelectListener = numberSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }
}
