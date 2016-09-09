
package com.milton.common.demo.fragment;


import android.content.Intent;
import android.view.View;

import com.milton.common.demo.R;
import com.milton.common.demo.activity.widget.SimpleUseActivity;
import com.milton.common.demo.activity.widget.ViewPagerUseActivity;

public class Fragment3ColorTrackView extends BaseFragment {


    @Override
    protected int getContentView() {
        return R.layout.fragment3_color_track_view;
    }

    @Override
    protected void initView() {
        super.initView();
        rootView.findViewById(R.id.btn_simple_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SimpleUseActivity.class));
            }
        });
        rootView.findViewById(R.id.btn_viewpager_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ViewPagerUseActivity.class));
            }
        });
    }


}
