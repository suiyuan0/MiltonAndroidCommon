package com.milton.common.qrcode;

/**
 * Created by H157925 on 16/7/8. 08:58
 * Email:Shodong.Sun@honeywell.com
 * <p/>
 * CUBE设备 panelId
 * <p/>
 * 433设备 ｛id};
 */

//扫描之后需要存储的数据和显示的数据

/**
 * CUBE设备 panelId
 */


/**
 * 433设备 ｛id};
 */


/**
 * MAIA设备
 * {loop, type, model, id};
 */

import android.os.Parcel;
import android.os.Parcelable;


public class ScanUIItem implements Parcelable {
    public boolean isCorrect = false;//判断是否是有效二维码

    public int scanType=-1;//
    public String panelID = "";//CUBE

    public String id = "";

    public int loopCount = 0;
    public String type = "";
    public String model = "";


    public ScanUIItem() {
    }

    public ScanUIItem(Parcel parcel) {
        isCorrect = parcel.readInt()==1?true:false;
        scanType = parcel.readInt();
        panelID = parcel.readString();
        loopCount = parcel.readInt();
        type = parcel.readString();
        model = parcel.readString();
        id = parcel.readString();
    }


    @Override
    public String toString() {
        return "MenuRuleConditionRoom ";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(isCorrect ? 1 : 0);
        parcel.writeInt(scanType);
        parcel.writeString(panelID);
        parcel.writeInt(loopCount);
        parcel.writeString(type);
        parcel.writeString(model);
        parcel.writeString(id);
    }

    public static Creator<ScanUIItem> getCreator() {
        return CREATOR;
    }

    public static final Creator<ScanUIItem> CREATOR = new Creator<ScanUIItem>() {
        public ScanUIItem createFromParcel(Parcel source) {
            return new ScanUIItem(source);
        }

        public ScanUIItem[] newArray(int size) {
            return new ScanUIItem[size];
        }
    };

}
