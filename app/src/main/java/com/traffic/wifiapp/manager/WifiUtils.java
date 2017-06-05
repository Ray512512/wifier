package com.traffic.wifiapp.manager;

import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ray on 2017/6/5.
 * emial:1452011874@qq.com
 */

public class WifiUtils {
    private static final java.lang.String TAG ="WifiUtils" ;

    public static ArrayList<WifiProvider> sortByLevel(ArrayList<WifiProvider> list) {
        for(int i=0;i<list.size();i++)
            for(int j=1;j<list.size();j++)
            {
                if(list.get(i).getLevel()<list.get(j).getLevel())    //level属性即为强度
                {
                    WifiProvider temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        return list;
    }

    public synchronized static boolean isEqual(List<String> l1, List<String> l2){
        boolean isEqual=false;
        if(l1==null||l2==null)return isEqual;
        int s1=l1.size(),s2=l2.size(),equalNum=0;
        if(s1==s2){
            if(s1==0)return isEqual;
            for (String r:l1){
                for (String t:l2){
                    if(r.equals(t)){
                        equalNum++;
                        break;
                    }
                }
            }
            if(equalNum==s1)isEqual= true;
        }
        L.v(TAG,equalNum==s1?"两次扫描结果一致，取消网络查询"+equalNum:"与上次扫描结果不一致，开始网络查询"+equalNum);
        return isEqual;
    }
}
