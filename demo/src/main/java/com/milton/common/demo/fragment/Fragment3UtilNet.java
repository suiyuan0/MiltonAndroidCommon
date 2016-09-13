
package com.milton.common.demo.fragment;


import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.NetUtil;
import com.milton.common.util.ScreenUtil;
import com.milton.common.util.ToastUtil;

public class Fragment3UtilNet extends Fragment2Base {

    @Override
    public String[] getItemNames() {
        return new String[]{
                "getIpAddress",
                "getLocalIpAddress",
                "getLocalMac",
                "ip2Hex",
                "hex2Ip",
                "getNetworkTypeName",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                showToastShort("3G ip is  " + NetUtil.getIpAddress());
                break;
            case 1:
                showToastShort("WIFI ip is  " + NetUtil.getLocalIpAddress(getActivity()));
                break;
            case 2:
                showToastShort("Local mac address is  " + NetUtil.getLocalMac(getActivity()));
                break;
            case 3:
                showToastShort("ip:192.168.68.128 =>hex :" + NetUtil.ip2Hex("192.168.68.128"));
                break;
            case 4:
                showToastShort("hex:c0a84480 =>ip :" + NetUtil.getLocalIpAddress(getActivity()));
                break;
            case 5:
                showToastShort("NetworkType is  " + NetUtil.getNetworkTypeName(getActivity()));
                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;
            case 9:

                break;
            case 10:

                break;

            default:
                break;
        }
    }
}
