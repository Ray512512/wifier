package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.baidu.mapapi.map.BaiduMap;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.manager.bdmap.BaiduMapManager;
import com.traffic.wifiapp.mvp.view.BaiduMapIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/5/8.
 * email：1452011874@qq.com
 */

public class BaiduMapPresenter extends BasePresenter<BaiduMapIView> {
    private BaiduMapManager mapManager;
    public BaiduMapPresenter(Activity mContext, BaiduMapIView mView, BaiduMap b) {
        super(mContext, mView);
        this.mContext=mContext;
        mapManager=new BaiduMapManager(mContext,b);
    }

    public void initLocation(){
        mapManager.initLocation();
    }


    public void onStop(){
        mapManager.onStop();
    }

    public void onResume(){
        mapManager.onResume();
    }

    public void getSlideUrls(){
        ApiManager.mApiService.getMapPageSlide().compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<List<SlideImageUrls>>() {
                    @Override
                    protected void _onNext(List<SlideImageUrls> wifiProviders) {
                        mView.showSlide(wifiProviders);
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
                });
    }

    public void showWifiPoint(ArrayList<WifiProvider> wifiProviders){
        if(wifiProviders==null)return;
        mapManager.showWifiPoint(wifiProviders);
    }

    public void setMyLocation(){
        mapManager.showMyLoc();
    }
}
