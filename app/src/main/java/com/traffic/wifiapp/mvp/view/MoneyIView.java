package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.base.BaseIView;
import com.traffic.wifiapp.bean.response.Goods;

import java.util.ArrayList;

/**
 * Created by Ray on 2017/5/6.
 * email：1452011874@qq.com
 */

public interface MoneyIView extends BaseIView {
//    void showSlide(List<SlideImageUrls> urls);
    void showGoods(ArrayList<Goods> goodses);
    void payFailed();
    void paySuccess();
}
