
package com.milton.common.demo.fragment;


public class Fragment3UtilPhrase extends Fragment2Base {

    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public String[] getItemTypes() {
        return new String[]{
                FragmentHelper.FOUR_Util_COLORPHRASE,
        };
    }
}
