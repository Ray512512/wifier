package com.traffic.wifiapp.manager.window;

import android.content.Context;
import android.widget.Toast;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.DeviceUtils;

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
                MainActivity.start(context);
                MainActivity activity=AppManager.getInstance(context).getMainActivity();
                if(activity!=null) {
                    WifiProvider currentW = activity.getPresenter().getWifiFragment().getConnectWifi();
                    if (currentW != null) {
                        activity.getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS, currentW);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
