
package com.milton.common.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milton.common.demo.R;

public class BaseFragment extends Fragment {
    private static final String DATA = "data";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_base, container, false);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText("Fragment#" + getArguments().getInt(DATA, 0));
        return textView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Fragment newInstance(int type) {
        Fragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(DATA, type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
