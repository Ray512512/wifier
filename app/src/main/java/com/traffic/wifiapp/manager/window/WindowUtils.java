package com.traffic.wifiapp.manager.window;

import android.content.Context;
import android.widget.Toast;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.DeviceUtils;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_PAY;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_PAY;

/**
 * Created by ray on 2017/5/24.
 */

public class WindowUtils {

    /**
     * 根据信号强度获取对应显示文本内容
     * */
    public static String getXinHaoStr(int l) {
        if (l >= 3) return "强";
        if (l >= 2) return "中";
        return "弱";
    }

    public static void gotoApp(Context context) {
        try {
            if (DeviceUtils.isAppAtBackground(context)) {
                WifiProvider currentW = WifiApplication.getInstance().getCurrentWifi();
                MainActivity.jumpPay(context,currentW);
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void checkJumpAction(Context context,WifiProvider currentW){
        MainActivity activity= AppManager.getInstance(context).getMainActivity();
        if(activity!=null) {
            if (currentW != null) {
                if(currentW.getType()==TYPE_SHOPER_PAY||currentW.getType()==TYPE_SINGLE_PAY){
                    activity.getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,currentW);
                }else {
                    activity.getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,currentW);
                }
            }
        }
    }

}
