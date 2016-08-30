
package com.milton.common.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milton.common.demo.R;
import com.milton.common.util.ToastUtil;
import com.milton.common.widget.SlideSwitch;

public abstract class BaseFragment extends Fragment {

    protected View rootView;// 缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getContentView(), null);
            initView();
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    protected abstract int getContentView();

    protected void initView() {

    }

    public void showToastShort(int res) {
        showToastShort(getString(res));
    }

    public void showToastShort(String text) {
        ToastUtil.showShort(getActivity(), text, true);
    }

    public void showToastLong(int res) {
        showToastLong(getString(res));
    }

    public void showToastLong(String text) {
        ToastUtil.showLong(getActivity(), text, true);
    }
}
