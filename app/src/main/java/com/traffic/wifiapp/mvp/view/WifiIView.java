package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.base.BaseIView;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WifiProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/5/6.
 * email：1452011874@qq.com
 */

public interface WifiIView extends BaseIView {
  void showWifiList(ArrayList<WifiProvider> wifiProviders);
  void showSlide(List<SlideImageUrls> urls);
  void wifiConnectSuccess(WifiProvider s);
  void wifiConnectCancle();
}
