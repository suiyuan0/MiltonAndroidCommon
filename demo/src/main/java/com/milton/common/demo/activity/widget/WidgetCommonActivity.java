package com.milton.common.demo.activity.widget;


import android.support.v4.app.Fragment;

import com.milton.common.activity.TitleBarActivity;
import com.milton.common.demo.R;
import com.milton.common.demo.fragment.FragmentHelper;

public class WidgetCommonActivity extends TitleBarActivity {

    @Override
    protected int getContent() {
        return R.layout.activity_widget_common;
    }

    @Override
    protected void initView() {
        super.initView();
        Fragment fragment = FragmentHelper.generateFragment(mStringType);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_layout, fragment).commit();
    }

}
