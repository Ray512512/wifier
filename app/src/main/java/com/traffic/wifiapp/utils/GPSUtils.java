package com.traffic.wifiapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by Ray on 2017/5/13.
 * email：1452011874@qq.com
 */

public class GPSUtils {

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     */

    public static void openGPS(Activity activity) {
        /*Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        activity.startActivityForResult(intent,0);*/

        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, 0);
    }
}