
package com.milton.common.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.milton.common.demo.R;
import com.milton.common.demo.util.Constants;

public class FragmentHelper {
    public final static String WIDGET_SLIDE_SWITCH = "LabelView";
    public final static String WIDGET_LABEL_VIEW = "SlideSwitch";
    public final static String WIDGET_MOVING_VIEW = "MovingView";
    public final static String WIDGET_SMOOTH_ROUND_PROGRESS_BAR = "SmoothRoundProgressBar";
    public final static String WIDGET_MIXTURE_TEXT_VIEW = "MixtureTextView";
    public final static String WIDGET_STICKY_NAV_LAYOUT = "StickyNavLayout";

    public static Fragment generateFragment(String type) {
        Fragment fragment = null;
        switch (type) {
            case WIDGET_SLIDE_SWITCH:
                fragment = new Fragment3SlideSwitch();
                break;
            case WIDGET_LABEL_VIEW:
                fragment = generateCommonFragment(R.layout.fragment3_labelview);
                break;
            case WIDGET_MOVING_VIEW:
                fragment = new Fragment3MovingView();
                break;
            case WIDGET_SMOOTH_ROUND_PROGRESS_BAR:
                fragment = generateCommonFragment(R.layout.fragment3_smooth_round_progress_bar);
                break;
            case WIDGET_MIXTURE_TEXT_VIEW:
                fragment = generateCommonFragment(R.layout.fragment3_mixture_text_view);
                break;
            case WIDGET_STICKY_NAV_LAYOUT:
                fragment = new Fragment3StickyNavLayout();
                break;
            default:
                break;
        }
        return fragment;
    }

    public static Fragment generateCommonFragment(int resId) {
        Fragment fragment = new Fragment3Common();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.RES_ID, resId);
        fragment.setArguments(bundle);
        return fragment;
    }
}
