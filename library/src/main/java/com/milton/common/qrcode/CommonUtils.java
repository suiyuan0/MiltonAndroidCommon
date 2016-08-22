package com.milton.common.qrcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Junyu.zhu@Honeywell.com on 16/7/19.
 */
public class CommonUtils {


    /**
     * 扫描的设备类型
     */
    public static final int SCAN_TYPE_CUBE = 0;
    public static final int SCAN_TYPE_433 = 1;
    public static final int SCAN_TYPE_MAIA = 2;


    /**
     * Cube统一Loop类型
     */
    public static final int LOOP_TYPE_LIGHT_INT = 1; // light
    public static final int LOOP_TYPE_CURTAIN_INT = 2; // curtain
    public static final int LOOP_TYPE_SWITCH_INT = 4; // switch

    /**
     * 检测扫描到的结果是否是正确的扫描结果
     * <p/>
     * 判断条件是 ScanUIItem.isCorrect
     * 设备类型 分为Cube ,Maia, 433
     *
     * @param scanStr
     * @return
     */
    public static ScanUIItem checkScanResult(String scanStr) {
        ScanUIItem uiItem = new ScanUIItem();
        if (scanStr == null || "".equalsIgnoreCase(scanStr)) {
            return uiItem;
        }

        if (scanStr.indexOf("001F55") >= 0 || scanStr.indexOf("001f55") >= 0) {
            //扫描到CUBE
            uiItem.scanType = SCAN_TYPE_CUBE;
            uiItem.panelID = scanStr;
            uiItem.isCorrect = true;
            return uiItem;
        }

        if (scanStr.length() == 7) {
            //扫描到 433 Sensor
            uiItem.scanType = SCAN_TYPE_433;
            uiItem.id = scanStr;
            uiItem.isCorrect = true;
            return uiItem;
        }

        //Maia 过滤
        Map map = getMaiaBody(scanStr);
        if (map == null) return uiItem;

        String number = (String) map.get("number");
        if (number == null || "".equalsIgnoreCase(number)) {
            return uiItem;
        }

        //type
        String type = (String) map.get("type");

        //Model
        String model = (String) map.get("model");

        uiItem.scanType = SCAN_TYPE_MAIA;
        uiItem.loopCount = Integer.parseInt(String.valueOf(number.charAt(3)));
        uiItem.type = type;
        uiItem.model = model;
        uiItem.id = number;
        uiItem.isCorrect = true;
        return uiItem;
    }


    /**
     * 解析Maia 主体
     *
     * @param str
     * @return
     */
    private static Map getMaiaBody(String str) {
        Map<String, Object> map = new HashMap<>();

        if (str.length() < 12) return null;

        //2开头
        if (!String.valueOf(str.charAt(0)).equalsIgnoreCase("2")) {
            return null;
        }
        //开关 Switch
        if (String.valueOf(str.charAt(2)).equalsIgnoreCase("1")) {
            map.put("type", "" + LOOP_TYPE_SWITCH_INT);
        } else if (String.valueOf(str.charAt(2)).equalsIgnoreCase("2")) {
            map.put("type", "" + LOOP_TYPE_LIGHT_INT);
        } else if (String.valueOf(str.charAt(2)).equalsIgnoreCase("3")) {
            map.put("type", "" + LOOP_TYPE_CURTAIN_INT);
        } else {
            return null;
        }
        //Add MainId 前四后八
        String numberStr = str.substring(0, 4) + str.substring(str.length() - 8, str.length());
        map.put("number", numberStr);

        //设备型号
        String modelStr = str.substring(0, 5).toUpperCase();

        //不是“D" 或者 "S" 就只能取前4位
        String lastChara = String.valueOf(modelStr.charAt(modelStr.length() - 1)).toUpperCase();
        if (!"D".equalsIgnoreCase(lastChara) && !"S".equalsIgnoreCase(lastChara)) {
            modelStr = modelStr.substring(0, 4);
        }
        map.put("model", modelStr);
        return map;
    }

}
