
package com.milton.common.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.milton.common.demo.R;
import com.milton.common.widget.slidetablayout.SlidingTabLayout;

public class Fragment1Home extends Fragment {
    private View rootView;// 缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment1, null);
            ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
            viewPager.setAdapter(new TabViewPagerAdapter(getActivity().getSupportFragmentManager(), getActivity()));
            SlidingTabLayout slidingTabLayout = (SlidingTabLayout) rootView.findViewById(R.id.sliding_tabs);
            slidingTabLayout.setDistributeEvenly(true);// 是否填充满屏幕的宽度
            // slidingTabLayout.setTabViewBackground(R.drawable.milton_background_selector);
            slidingTabLayout.setTabTextColor(R.color.alinmi_text_color);
            // slidingTabLayout.setTabTextSize(20);
            slidingTabLayout.setViewPager(viewPager);

            slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return Color.RED;
                }
            });
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable
    Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("milton6", "onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("milton6", "onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("milton6", "onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("milton6", "onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable
                                    Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("milton6", "onViewStateRestored");
    }
}
