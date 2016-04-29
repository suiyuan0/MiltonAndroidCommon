
package com.milton.common.demo.activity;

import android.app.ActionBar;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.milton.common.demo.R;
import com.milton.common.demo.Shakespeare;
import com.milton.common.demo.fragment.Fragment1Friends;
import com.milton.common.demo.fragment.Fragment1Home;
import com.milton.common.demo.fragment.Fragment1Message;
import com.milton.common.demo.fragment.Fragment1More;
import com.milton.common.demo.fragment.Fragment1Search;

/**
 * @author zqy
 */
public class MainActivity extends FragmentActivity implements android.widget.TabHost.OnTabChangeListener {
    private DrawerLayout mDrawerLayout;
    private ListView mStartDrawer;
    private ListView mEndDrawer;
    private ActionBarHelper mActionBar;

    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * FragmentTabhost
     */
    private FragmentTabHost mTabHost;

    /**
     * 布局填充器
     */
    private LayoutInflater mLayoutInflater;

    /**
     * Fragment数组界面
     */
    private Class mFragmentArray[] = {
            Fragment1Home.class, Fragment1Message.class,
            Fragment1Friends.class, Fragment1Search.class, Fragment1More.class
    };
    /**
     * 存放图片数组
     */
    private int mImageArray[] = {
            R.drawable.tab_home_btn,
            R.drawable.tab_message_btn, R.drawable.tab_selfinfo_btn,
            R.drawable.tab_square_btn, R.drawable.tab_more_btn
    };

    /**
     * 选修卡文字
     */
    private String mTextArray[] = {
            "首页", "消息", "好友", "搜索", "更多"
    };

    /**
  * 
     * 
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mStartDrawer = (ListView) findViewById(R.id.start_drawer);
        mEndDrawer = (ListView) findViewById(R.id.end_drawer);
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
        mDrawerLayout.setDrawerShadow(getResources().getDrawable(R.drawable.drawer_shadow), GravityCompat.START);

        // The drawer title must be set in order to announce state changes when
        // accessibility is turned on. This is typically a simple description,
        // e.g. "Navigation".
        mDrawerLayout.setDrawerTitle(GravityCompat.START, getString(R.string.drawer_title));

        mStartDrawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Shakespeare.TITLES));
        mStartDrawer.setOnItemClickListener(new DrawerItemClickListener());
        mEndDrawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Shakespeare.TITLES));
        mEndDrawer.setOnItemClickListener(new DrawerItemClickListener2());
        mActionBar = createActionBarHelper();
        mActionBar.init();

        // ActionBarDrawerToggle provides convenient helpers for tying together
        // the
        // prescribed interactions between a top-level sliding drawer and the
        // action bar.
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
        initView();
        // mTabHost.setCurrentTab(2);
        mTabHost.setOnTabChangedListener(this);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        // 找到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 得到fragment的个数
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            // 设置Tab按钮的背景
            // mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextArray[index]);

        return view;
    }

    @Override
    public void onTabChanged(String arg0) {
        Log.e("milton", "onTabChanged -> " + arg0);

    }

    /**
     * Create a compatible helper that will manipulate the action bar if
     * available.
     */
    private ActionBarHelper createActionBarHelper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return new ActionBarHelperICS();
        } else {
            return new ActionBarHelper();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * The action bar home/up action should open or close the drawer.
         * mDrawerToggle will take care of this.
         */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * This list item click listener implements very simple view switching by
     * changing the primary content text. The drawer is closed when a selection
     * is made.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // mContent.setText(Shakespeare.DIALOGUE[position]);
            mActionBar.setTitle(Shakespeare.TITLES[position]);
            mDrawerLayout.closeDrawer(mStartDrawer);
        }
    }

    private class DrawerItemClickListener2 implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // mContent.setText(Shakespeare.DIALOGUE[position]);
            mActionBar.setTitle(Shakespeare.TITLES[position]);
            mDrawerLayout.closeDrawer(mEndDrawer);
        }
    }

    /**
     * A drawer listener can be used to respond to drawer events such as
     * becoming fully opened or closed. You should always prefer to perform
     * expensive operations such as drastic relayout when no animation is
     * currently in progress, either before or after the drawer animates. When
     * using ActionBarDrawerToggle, all DrawerLayout listener methods should be
     * forwarded if the ActionBarDrawerToggle is not used as the DrawerLayout
     * listener directly.
     */
    private class DemoDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBar.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBar.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    /**
     * Stub action bar helper; this does nothing.
     */
    private class ActionBarHelper {
        public void init() {
        }

        public void onDrawerClosed() {
        }

        public void onDrawerOpened() {
        }

        public void setTitle(CharSequence title) {
        }
    }

    /**
     * Action bar helper for use on ICS and newer devices.
     */
    private class ActionBarHelperICS extends ActionBarHelper {
        private final ActionBar mActionBar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;

        ActionBarHelperICS() {
            mActionBar = getActionBar();
        }

        @Override
        public void init() {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = mDrawerTitle = getTitle();
        }

        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        @Override
        public void onDrawerClosed() {
            super.onDrawerClosed();
            mActionBar.setTitle(mTitle);
        }

        /**
         * When the drawer is open we set the action bar to a generic title. The
         * action bar should only contain data relevant at the top level of the
         * nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        @Override
        public void onDrawerOpened() {
            super.onDrawerOpened();
            mActionBar.setTitle(mDrawerTitle);
        }

        @Override
        public void setTitle(CharSequence title) {
            mTitle = title;
        }
    }
}
