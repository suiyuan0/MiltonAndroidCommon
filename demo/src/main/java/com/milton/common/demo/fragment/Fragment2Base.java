
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

public class Fragment2Base extends Fragment implements AdapterView.OnItemClickListener {
    private ListView mList;
    private Class mClassList[];

    public Class[] getItemClass() {
        return new Class[] {
                DrawerLayoutActivity.class,
                SlidingMenuActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "DrawerLayout",
                "SlidingMenu",
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mClassList = getItemClass();
        View root = inflater.inflate(R.layout.activity_util, null);
        mList = (ListView) root.findViewById(R.id.util_list);
        mList.setOnItemClickListener(this);
        mList.setDivider(null);
        mList.setAdapter(new OneLineTextListAdapter(getItemNames()));
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        getActivity().startActivity(new Intent(getActivity(), mClassList[i]));
    }

}
