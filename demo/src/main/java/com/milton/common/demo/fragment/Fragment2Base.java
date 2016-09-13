
package com.milton.common.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.milton.common.adapter.OneLineTextListAdapter;
import com.milton.common.demo.R;
import com.milton.common.demo.activity.sliding.DrawerLayoutActivity;
import com.milton.common.demo.activity.sliding.SlidingMenuActivity;
import com.milton.common.demo.activity.CommonActivity;
import com.milton.common.util.Constants;

public class Fragment2Base extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView mList;
    private Class mClassList[];
    private String mTypeList[];
    private String mTitleList[];

    public Class[] getItemClass() {
        return new Class[]{
                DrawerLayoutActivity.class,
                SlidingMenuActivity.class,
        };
    }

    public String[] getItemTypes() {
        return new String[]{
                FragmentHelper.WIDGET_LABEL_VIEW,
                FragmentHelper.WIDGET_SLIDE_SWITCH,
        };
    }

    public String[] getItemNames() {
        return getItemTypes();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        if (isActivity()) {
            getActivity().startActivity(new Intent(getActivity(), mClassList[i]));
        } else {
            Intent intent = new Intent(getActivity(), CommonActivity.class);
            intent.putExtra(Constants.TYPE_STRING, mTypeList[i]);
            intent.putExtra(Constants.TITLE, mTitleList[i]);
            getActivity().startActivity(intent);
        }
    }

    public boolean isActivity() {
        return true;
    }

    protected int getContentView() {
        return R.layout.activity_util;
    }

    protected void initView() {
        mClassList = getItemClass();
        mTypeList = getItemTypes();
        mTitleList = getItemNames();
        mList = (ListView) rootView.findViewById(R.id.util_list);
        mList.setOnItemClickListener(this);
        mList.setDivider(null);
        mList.setAdapter(new OneLineTextListAdapter(mTitleList));
    }
}
