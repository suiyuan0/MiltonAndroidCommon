
package com.milton.common.demo.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabViewPagerAdapter extends FragmentPagerAdapter {
    private final static String SLIDING = "Sliding";
    private final static String DIALOG = "Dialog";
    private final static String LISTVIEW = "ListView";
    private final static String ACTIVITY = "Activity";
    private final static String IMAGELOADER = "ImageLoader";
    private final static String SOCKET = "Socket";
    private final static String DRAWABLE = "Drawable";
    private final static String VIEW = "View";
    private final static String ANIMATION = "Animation";
    private final static String UTIL = "Util";
    private final static String PREFERENCE = "Preference";
    private final static String HTML5 = "Html5";
    private final static String MUTILMEDIA = "MultiMedia";
    private final static String NOTIFICATION = "Notification";
    private final static String WIDGET = "Widget";
    private final static String OTHER = "Other";
    //    private String mTabTitle[] = new String[]{
//            "Sliding", "Dialog","ListView", "Activity", "ImageLoader", "Socket", "Drawable", "View", "Animation", "Util", "Preference", "Html5", "MultiMedia", "Notification", "Widget", "Other"
//    };
    private String mTabTitle[] = new String[]{
            SLIDING, DIALOG, LISTVIEW, ACTIVITY, IMAGELOADER, SOCKET, DRAWABLE, VIEW, ANIMATION, UTIL, PREFERENCE, HTML5, MUTILMEDIA, NOTIFICATION, WIDGET, OTHER
    };
//    private Context mContext;

    public TabViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
//        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    private Fragment getFragment(int position) {
        if (checkName(SLIDING, position)) {
            return new Fragment2Sliding();
        } else if (checkName(DIALOG, position)) {
            return new Fragment2Dialog();
        } else if (checkName(LISTVIEW, position)) {
            return new Fragment2ListView();
        } else if (checkName(ACTIVITY, position)) {
            return new Fragment2Activity();
        } else if (checkName(IMAGELOADER, position)) {
            return new Fragment2ImageLoader();
        } else if (checkName(SOCKET, position)) {
            return new Fragment2Socket();
        } else if (checkName(DRAWABLE, position)) {
            return BaseFragment.newInstance(position);
        } else if (checkName(VIEW, position)) {
            return BaseFragment.newInstance(position);
        } else if (checkName(ANIMATION, position)) {
            return BaseFragment.newInstance(position);
        } else if (checkName(UTIL, position)) {
            return new Fragment2Util();
        } else if (checkName(PREFERENCE, position)) {
            return BaseFragment.newInstance(position);
        } else if (checkName(HTML5, position)) {
            return new Fragment2Html5();
        } else if (checkName(MUTILMEDIA, position)) {
            return new Fragment2MultiMedia();
        } else if (checkName(NOTIFICATION, position)) {
            return new Fragment2Notification();
        } else if (checkName(WIDGET, position)) {
            return new Fragment2Widget();
        } else if (checkName(OTHER, position)) {
            return new Fragment2Other();
        } else {
            return BaseFragment.newInstance(position);
        }
    }

    private boolean checkName(String itemName, int position) {
        if (null == itemName || mTabTitle == null || mTabTitle.length < 1 || position < 0 || position > mTabTitle.length - 1) {
            return false;
        } else {
            return itemName.equalsIgnoreCase(mTabTitle[position]);
        }
    }
    //    public Fragment getFragment(int position) {
//        if ("View".equalsIgnoreCase(mTabTitle[position])) {
//            return BaseFragment.newInstance(position);
//        } else if ("Animation".equalsIgnoreCase(mTabTitle[position])) {
//            return BaseFragment.newInstance(position);
//        } else if ("Dialog".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Dialog();
//        } else if ("Drawable".equalsIgnoreCase(mTabTitle[position])) {
//            return BaseFragment.newInstance(position);
//        } else if ("Sliding".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Sliding();
//        } else if ("Util".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Util();
//        } else if ("Preference".equalsIgnoreCase(mTabTitle[position])) {
//            return BaseFragment.newInstance(position);
//        } else if ("Other".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Other();
//        } else if ("Activity".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Activity();
//        } else if ("ImageLoader".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2ImageLoader();
//        } else if ("Socket".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Socket();
//        } else if ("Widget".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Widget();
//        } else if ("Notification".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Notification();
//        } else if ("MultiMedia".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2MultiMedia();
//        } else if ("Html5".equalsIgnoreCase(mTabTitle[position])) {
//            return new Fragment2Html5();
//        } else {
//            return BaseFragment.newInstance(position);
//        }
//    }


    @Override
    public int getCount() {
        return mTabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle[position];
    }
}
