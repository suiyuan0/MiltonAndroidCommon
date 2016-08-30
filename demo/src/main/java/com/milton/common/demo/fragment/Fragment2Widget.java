
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
        };
    }

    @Override
    public String[] getItemNames() {
        return new String[]{
                FragmentHelper.WIDGET_LABEL_VIEW,
                FragmentHelper.WIDGET_SLIDE_SWITCH,
                FragmentHelper.WIDGET_MOVING_VIEW,
        };
    }

}
