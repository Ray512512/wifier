package com.traffic.wifiapp.data;

import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.mvp.presenter.MoneyPresenter;

import static com.traffic.wifiapp.common.ConstantField.H1;

/**
 * Created by ray on 2017/5/30.
 * emial:1452011874@qq.com
 */

public class WifiUtils {

    /**
     * 判断wifi是否是免费wifi
     * */
    public static boolean isFreeWifiAndOpenIt(WifiProvider w,boolean isOpen){
        if(w==null)return false;
        if(w.getType()==WifiProvider.TYPE_SHOPER_FREE||w.getType()==WifiProvider.TYPE_SINGLE_FREE){
            if(isOpen)
            MoneyPresenter.openWifi(24*H1);//打开24小时
            return true;
        }
        return false;
    }
}
