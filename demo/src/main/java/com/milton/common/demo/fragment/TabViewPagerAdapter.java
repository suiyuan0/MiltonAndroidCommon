
package com.milton.common.demo.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private String mTabTitle[] = new String[] {
            "Activity", "ImageLoader", "Sliding", "Dialog", "Drawable", "View", "Animation", "Util", "Preference", "Other"
    };
    private Context mContext;

    public TabViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    public Fragment getFragment(int position) {
        if ("View".equalsIgnoreCase(mTabTitle[position])) {
            return BaseFragment.newInstance(position);
        } else if ("Animation".equalsIgnoreCase(mTabTitle[position])) {
            return BaseFragment.newInstance(position);
        } else if ("Dialog".equalsIgnoreCase(mTabTitle[position])) {
            return BaseFragment.newInstance(position);
        } else if ("Drawable".equalsIgnoreCase(mTabTitle[position])) {
            return BaseFragment.newInstance(position);
        } else if ("Sliding".equalsIgnoreCase(mTabTitle[position])) {
            return new Fragment2Sliding();
        } else if ("Util".equalsIgnoreCase(mTabTitle[position])) {
            return new Fragment2Util();
        } else if ("Preference".equalsIgnoreCase(mTabTitle[position])) {
            return BaseFragment.newInstance(position);
        } else if ("Other".equalsIgnoreCase(mTabTitle[position])) {
            return new Fragment2Other();
        } else if ("Activity".equalsIgnoreCase(mTabTitle[position])) {
            return new Fragment2Activity();
        } else if ("ImageLoader".equalsIgnoreCase(mTabTitle[position])) {
            return new Fragment2ImageLoader();
        } else {
            return BaseFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mTabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }
}
