
package com.milton.common.demo.fragment;


public class Fragment2Util extends Fragment2Base {
    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public String[] getItemTypes() {
        return new String[]{
                FragmentHelper.UTIL_SCREEN,
                FragmentHelper.UTIL_CONTEXT,
                FragmentHelper.UTIL_RESOURCE,
                FragmentHelper.UTIL_STRING,
                FragmentHelper.UTIL_NET,
                FragmentHelper.UTIL_NOTIFICATION,
                FragmentHelper.UTIL_PHRASE,
        };
    }
}
