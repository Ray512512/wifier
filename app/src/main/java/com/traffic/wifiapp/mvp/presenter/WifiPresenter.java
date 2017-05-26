package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.entry.MacStr;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.manager.WifiAdmin;
import com.traffic.wifiapp.mvp.view.WifiIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.FileUtils;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.NetUtil;
import com.traffic.wifiapp.utils.NetworkTools;
import com.traffic.wifiapp.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ray on 2017/5/7.
 * email：1452011874@qq.com
 */

public class WifiPresenter extends BasePresenter<WifiIView>{
    private static final String TAG ="WifiPresenter" ;

    public WifiPresenter(Activity mContext, WifiIView mView) {
        super(mContext, mView);
        init();
    }


    private WifiAdmin wifiAdmin ;

    private List<ScanResult> list;//附近wifi列表
    private List<ScanResult> listCanUse=new ArrayList<>();//附近wifi列表中 可用的指定路由器
    private List<String> wifiMacList;//服务器的mac池
    private ArrayList<WifiProvider> mWifiProviders;
    public boolean isSearching=false;//是否正在更新wifi列表

    private void init(){
        wifiAdmin=WifiAdmin.getIntance(mContext);
    }
    /**
     * 扫描获取附近wifi列表
     * */
    public synchronized void getScanWifiList() {
        if(isSearching|| !NetUtil.checkNetWork()){
            L.v(TAG,"正在搜索或者网络不可用，终止此次搜索");
            return;
        }
        isSearching=true;
        Observable.just(wifiAdmin.getWifiList()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {
            list=r;
            if(list!=null&&list.size()!=0){
                L.v(TAG,"getScanWifiList"+list.toString());
                if(wifiMacList==null){
                    getWifiMacList();
                }else {
                    getShowWifiList(wifiMacList);
                }
            }else {
                mView.showDataView();
                showToast("附近暂无可用wifi");
            }
        });
    }

    /**
     * 获取服务器wifi mac池
     * 获取到后再与搜索到的wifi进行匹配出指定路由器wifi
     * */
    private void getWifiMacList(){
        ApiManager.mApiService.getAllMacList().compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<List<String>>() {
                    @Override
                    protected void _onNext(List<String> wifiMac) {
                        FileUtils.writeTxtToFile("获取服务器mac池："+wifiMac.toString(),FileUtils.path,"wifi首页日志");
                        getShowWifiList(wifiMac);
                        L.v(TAG,"getAllMacList"+wifiMac.toString());
//                        getWifiMsg(wifiMac);
                    }

                    @Override
                    protected void _onError(String message) {
//                        showToast(message);
                        wifiMacList=SPUtils.getObject(ConstantField.WIFI_MAC_LIST);
                        getShowWifiList(wifiMacList);
                    }
                });
    }

    private void getShowWifiList(List<String> wifiMacs){
        if (wifiMacs != null&&wifiMacs.size()!=0) {
            wifiMacList=wifiMacs;
            List<String > showMaclist=new ArrayList<>();
            SPUtils.setObject(ConstantField.WIFI_MAC_LIST,wifiMacs);
            for (ScanResult scanResult:list){
                if(wifiMacs.contains(scanResult.BSSID)){
                    showMaclist.add(scanResult.BSSID);
                    listCanUse.add(scanResult);
                }
            }
            L.v(TAG,"getWifiMacList:"+ showMaclist.toString());
            if(showMaclist.size()>0) getWifiMsg(showMaclist);
            else {
                mView.showDataView();
                showToast("附近暂无可用wifi");
            }
        }else {
            L.v(TAG, "服务器返回mac池为空:");
            mView.showDataView();
            showToast("附近暂无可用wifi");
        }
    }
    //得到可用wifi
    private void getWifiMsg(List<String> list){
        MacStr macStr=new MacStr(list);
        ApiManager.mApiService.getUserInfoByMac(macStr).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<ArrayList<WifiProvider>>() {
                    @Override
                    protected void _onNext(ArrayList<WifiProvider> wifiProviders) {
                        ArrayList<WifiProvider> wifiProviders2=new ArrayList<WifiProvider>();
                        for (WifiProvider w:wifiProviders){
                            for (ScanResult s:listCanUse){
                                if(s.BSSID.equals(w.getMac())){
                                    L.v(TAG, "可用wifi的服务器信息:"+s.BSSID+"\n"+s.SSID);
                                    w.setSSID(s.SSID);
                                    w.setBSSID(s.BSSID);
//                                    w.setIpAddr(s.);
                                    w.setLevel(WifiManager.calculateSignalLevel(s.level, 5));
                                    wifiProviders2.add(w);
                                    break;
                                }
                            }
                            
                            /*w.setBSSID("a6:5e:60:78:e6:ad");
                            wifiProviders2.add(w);*/
                        }
                        mWifiProviders=sortByLevel(wifiProviders2);
                        mView.showWifiList((mWifiProviders));
                        checkCurrentContect();
                        L.v(TAG,"搜索完成，打开搜索开关");
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, "获取用户信息接口异常:"+message);
                        showToast(message);
                        mView.showDataView();
                    }
                });
    }
    private ArrayList<WifiProvider> sortByLevel(ArrayList<WifiProvider> list) {
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

    public void registBordCast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mContext.registerReceiver(mRecaiver, filter);
    }

    public void unregistBordCast(){
        if(mRecaiver!=null)
            mContext.unregisterReceiver(mRecaiver);
    }
    private BroadcastReceiver mRecaiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action){
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION://wifi已成功扫描到可用wifi。
                    L.v(TAG, "成功扫描到可用wifi");
                    getScanWifiList();
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION://wifi开关状态变化
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_ENABLED:
                            L.v(TAG, "WiFi已启用");
                            getScanWifiList();
                            break;
                        case WifiManager.WIFI_STATE_DISABLED:
                            L.v(TAG, "Wifi已关闭");
                            mView.wifiConnectCancle();
                            break;
                    }
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION://网络切换
                    int state= NetworkTools.getNetWorkType(context);
                    switch (state){
                        case NetworkTools.NETWORKTYPE_INVALID://断网
                            L.v(TAG,"当前网络状态："+ "断网");
                            mView.wifiConnectCancle();
                            break;
                        case NetworkTools.NETWORKTYPE_2G://2g
                            L.v(TAG,"当前网络状态："+ "2G");
                            mView.wifiConnectCancle();
                            break;
                        case NetworkTools.NETWORKTYPE_3G://3g以上
                            L.v(TAG,"当前网络状态："+ "3G及以上");
                            mView.wifiConnectCancle();
                            getScanWifiList();
                            break;
                        case NetworkTools.NETWORKTYPE_WIFI://wifi
                            L.v(TAG,"当前网络状态："+ "WIFI");
                            checkCurrentContect();
                            getScanWifiList();
                            break;
                        default:
                            checkCurrentContect();
                    }
                    break;
            }

        }
    };

    public void checkCurrentContect(){
            WifiInfo wi=wifiAdmin.getConnectWifi();
            if(mWifiProviders!=null&&mWifiProviders.size()>0&&wi!=null){
                L.v(TAG,"当前已连接的网络状态："+wi.getBSSID());
                boolean isCancle=false;//是否有连接到商家wifi
                for (WifiProvider w:mWifiProviders){
                    L.v(TAG,"服务器可用wifi："+w.getBSSID());
                    if(w.getBSSID().equals(wi.getBSSID())){
                        L.v(TAG,"匹配到服务器wifi且已连接");
                        isCancle=true;
                        mView.wifiConnectSuccess(w);
                        break;
                    }
                }
                if(!isCancle){
                    mView.wifiConnectCancle();
                }
            }
    }

    public void getSlideUrls(){
        ApiManager.mApiService.getHomePageSlide().compose(RxHelper.handleResult())
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

    public void connect(String ssid){
//        wifiAdmin.connect("“Administrator”的 iPhone", "78534plxx", 3);
        wifiAdmin.connect(ssid);
    }

}
