
package com.milton.common.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.milton.common.demo.R;
import com.milton.common.demo.activity.sliding.DrawerLayoutActivity;
import com.milton.common.demo.activity.sliding.SlidingMenuActivity;

public class Fragment1Message extends Fragment implements View.OnClickListener {

    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment2, null);
            Button btnDrawerLayout = (Button) root.findViewById(R.id.btn_drawerlayout);
            btnDrawerLayout.setOnClickListener(this);
            Button btnSlidingMenu = (Button) root.findViewById(R.id.btn_slidingmenu);
            btnSlidingMenu.setOnClickListener(this);
        }
        ViewGroup parent = (ViewGroup) root.getParent();
        if (parent != null)
        {
            parent.removeView(root);
        }
        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawerlayout:
                getActivity().startActivity(new Intent(getActivity(), DrawerLayoutActivity.class));
                break;
            case R.id.btn_slidingmenu:
                getActivity().startActivity(new Intent(getActivity(), SlidingMenuActivity.class));
                break;
            default:
                break;
        }
    }
}
