
package com.milton.common.demo.fragment;

import android.widget.TextView;

import com.milton.common.demo.R;
import com.milton.common.widget.SlideSwitch;

public class Fragment3SlideSwitch extends BaseFragment implements SlideSwitch.SlideListener {
    TextView txt;
    SlideSwitch slide;
    SlideSwitch slide2;

    @Override
    protected int getContentView() {
        return R.layout.fragment3_slideswitch;
    }

    @Override
    protected void initView() {
        super.initView();
        slide = (SlideSwitch) rootView.findViewById(R.id.swit);
        slide2 = (SlideSwitch) rootView.findViewById(R.id.swit2);

        slide.setState(false);
        txt = (TextView) rootView.findViewById(R.id.txt);
        slide.setSlideListener(this);
    }

    @Override
    public void open() {
        txt.setText("first switch is opend, and set the second one is 'slideable'");
        slide2.setSlideable(true);
    }

    @Override
    public void close() {
        txt.setText("first switch is closed,and set the second one is 'unslideable'");
        slide2.setSlideable(false);
    }

}
