
package com.milton.common.demo.fragment;


import com.milton.common.demo.util.Constants;

public class Fragment3Common extends BaseFragment {

    @Override
    protected int getContentView() {
        return getArguments().getInt(Constants.RES_ID);
    }
}
