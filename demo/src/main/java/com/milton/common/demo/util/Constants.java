
package com.milton.common.demo.util;

import com.milton.common.demo.bean.CityEntity;

import java.util.ArrayList;

public class Constants {
    public static final String RES_ID = "resID";
    public static final String UTIL_ACTION = "android.intent.action.UtilsActivity";

    /*
     * 获取城市列表
     */
    public static ArrayList<CityEntity> getCityList() {
        ArrayList<CityEntity> cityList = new ArrayList<CityEntity>();
        CityEntity city1 = new CityEntity(1, "安吉", 'A');
        CityEntity city2 = new CityEntity(2, "北京", 'B');
        CityEntity city3 = new CityEntity(3, "长春", 'C');
        CityEntity city4 = new CityEntity(4, "长沙", 'C');
        CityEntity city5 = new CityEntity(5, "大连", 'D');
        CityEntity city6 = new CityEntity(6, "哈尔滨", 'H');
        CityEntity city7 = new CityEntity(7, "杭州", 'H');
        CityEntity city8 = new CityEntity(8, "金沙江", 'J');
        CityEntity city9 = new CityEntity(9, "江门", 'J');
        CityEntity city10 = new CityEntity(10, "山东", 'S');
        CityEntity city11 = new CityEntity(11, "三亚", 'S');
        CityEntity city12 = new CityEntity(12, "义乌", 'Y');
        CityEntity city13 = new CityEntity(13, "舟山", 'Z');
        cityList.add(city1);
        cityList.add(city2);
        cityList.add(city3);
        cityList.add(city4);
        cityList.add(city5);
        cityList.add(city6);
        cityList.add(city7);
        cityList.add(city8);
        cityList.add(city9);
        cityList.add(city10);
        cityList.add(city11);
        cityList.add(city12);
        cityList.add(city13);
        return cityList;
    }
}
