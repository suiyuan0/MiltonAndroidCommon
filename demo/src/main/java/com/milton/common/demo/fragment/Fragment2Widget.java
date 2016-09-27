
package com.milton.common.demo.fragment;

public class Fragment2Widget extends Fragment2Base {

    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public String[] getItemTypes() {
        return new String[]{
                FragmentHelper.WIDGET_LABEL_VIEW,
                FragmentHelper.WIDGET_SLIDE_SWITCH,
                FragmentHelper.WIDGET_MOVING_VIEW,
                FragmentHelper.WIDGET_SMOOTH_ROUND_PROGRESS_BAR,
                FragmentHelper.WIDGET_MIXTURE_TEXT_VIEW,
                FragmentHelper.WIDGET_STICKY_NAV_LAYOUT,
                FragmentHelper.WIDGET_COLOR_TRACK_VIEW,
                FragmentHelper.WIDGET_COLOR_IMAGE_VIEW,
                FragmentHelper.WIDGET_SIGNATURE_PAD,
                FragmentHelper.WIDGET_AUTOCHECKEDITTEXT,
                FragmentHelper.WIDGET_SEAT_TABLE,
        };
    }

}
