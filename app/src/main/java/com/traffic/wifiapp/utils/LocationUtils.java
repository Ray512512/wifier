package com.traffic.wifiapp.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.text.DecimalFormat;

/**
 * Created by Ray on 2017/5/11.
 * email：1452011874@qq.com
 */

public class LocationUtils {

    public static String getDistance(LatLng l1,LatLng l2){
        int location=(int) DistanceUtil.getDistance(l1, l2);
        if(location==-1D){
            return "暂无位置信息";
        }
        if(location>1000){
            String  a=new DecimalFormat("##0.0").format(((float) location)/1000);
            return a+"km";
        }else {
            return location+"m";
        }
    }
}
