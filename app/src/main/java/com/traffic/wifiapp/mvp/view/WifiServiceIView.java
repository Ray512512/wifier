package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.bean.response.WifiProvider;

import java.util.ArrayList;

/**
 * Created by Ray on 2017/5/6.
 * email：1452011874@qq.com
 */

public interface WifiServiceIView {
  void showWifiList(ArrayList<WifiProvider> wifiProviders);
  void wifiConnectSuccess(WifiProvider s);
  void wifiConnectCancle();
  void wifiNoConnect();

}
