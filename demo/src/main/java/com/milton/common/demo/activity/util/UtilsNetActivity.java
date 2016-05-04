
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;

import com.milton.common.util.NetUtil;
import com.milton.common.util.ToastUtil;

public class UtilsNetActivity extends UtilsBaseActivity {

    @Override
    public String[] getItemNames() {
        return new String[] {
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
                ToastUtil.showShort(this, "3G ip is  " + NetUtil.getIpAddress());
                break;
            case 1:
                ToastUtil.showShort(this, "WIFI ip is  " + NetUtil.getLocalIpAddress(this));
                break;
            case 2:
                ToastUtil.showShort(this, "Local mac address is  " + NetUtil.getLocalMac(this));
                break;
            case 3:
                ToastUtil.showShort(this, "ip:192.168.68.128 =>hex :" + NetUtil.ip2Hex("192.168.68.128"));
                break;
            case 4:
                ToastUtil.showShort(this, "hex:c0a84480 =>ip :" + NetUtil.getLocalIpAddress(this));
                break;
            case 5:
                ToastUtil.showShort(this, "NetworkType is  " + NetUtil.getNetworkTypeName(this));
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
