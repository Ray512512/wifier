package com.traffic.wifiapp.mvp.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.entry.MacStr;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.manager.WifiAdmin;
import com.traffic.wifiapp.mvp.view.WifiIView;
import com.traffic.wifiapp.mvp.view.WifiServiceIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.NetUtil;
import com.traffic.wifiapp.utils.NetworkTools;
import com.traffic.wifiapp.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_FREE;
import static com.traffic.wifiapp.common.ConstantField.H1;
import static com.traffic.wifiapp.manager.WifiUtils.isEqual;
import static com.traffic.wifiapp.manager.WifiUtils.sortByLevel;

/**
 * Created by Ray on 2017/5/7.
 * email：1452011874@qq.com
 */

public class WifiAppPresenter {
    private static final String TAG ="WifiAppPresenter" ;

    private WifiIView mFragmentView;//view接口
    private WifiServiceIView mServiceIView;
    private Context mContext;
    private boolean isInited =false;

    public void setmFragmentView(WifiIView mFragmentView) {
        this.mFragmentView = mFragmentView;
    }

    public void setmServiceIView(WifiServiceIView mServiceIView) {
        this.mServiceIView = mServiceIView;
    }

    public WifiAppPresenter(Context mContext) {
        this.mContext=mContext;
        init();
    }


    private WifiAdmin wifiAdmin ;
    private List<ScanResult> list;//附近wifi列表
    private List<ScanResult> listCanUse=new ArrayList<>();//附近wifi列表中 可用的指定路由器
    private List<String> wifiMacList,showMacList;//wifiMacList 服务器的mac池  ;  showMacList展示在首页wifi列表中的mac地址集合
    private ArrayList<WifiProvider> mWifiProviders;
    public boolean isSearching=false;//是否正在更新wifi列表

    private Handler mHandler;
    private Runnable scanWifiRunnable=new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, 5000);
            getScanWifiList();
        }
    };
    private void init(){
        wifiAdmin=WifiAdmin.getIntance(mContext);
        registBordCast();
        mHandler=new Handler(Looper.getMainLooper());
        mHandler.postDelayed(scanWifiRunnable,5000);
    }
    private void showToast(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
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
                if(!isInited)
                showToast(mContext.getString(R.string.no_wifi_msg));
                if(mFragmentView!=null)
                    mFragmentView.showDataView();
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
                        getShowWifiList(wifiMac);
                    }

                    @Override
                    protected void _onError(String message) {
                        wifiMacList=SPUtils.getObject(ConstantField.WIFI_MAC_LIST);
                        getShowWifiList(wifiMacList);
                    }
                });
    }

    private void getShowWifiList(List<String> wifiMacs){
        if (wifiMacs != null&&wifiMacs.size()!=0) {
            wifiMacList=wifiMacs;
            List<String > showMaclist=new ArrayList<>();//最新的展示在wifi列表的mac数据集合
            listCanUse.clear();
            for (ScanResult scanResult:list){
                if(wifiMacs.contains(scanResult.BSSID)){
                    showMaclist.add(scanResult.BSSID);
                    listCanUse.add(scanResult);
               }
            }
            if(isInited &&isEqual(showMacList,showMaclist)){//过滤查询重复数据
                if(mFragmentView!=null)
                    mFragmentView.showDataView();
                return;
            }
            SPUtils.setObject(ConstantField.WIFI_MAC_LIST,wifiMacs);
            showMacList=showMaclist;
            L.v(TAG,"getWifiMacList:"+ showMaclist.toString());
            if(showMaclist.size()>0) getWifiMsg(showMaclist);
            else {
                if(mFragmentView!=null)
                    mFragmentView.showDataView();
                if(!isInited)
                showToast(mContext.getString(R.string.no_wifi_msg));
            }
        }else {
            L.v(TAG, "服务器返回mac池为空:");
            if(mFragmentView!=null)
                mFragmentView.showDataView();
            if(!isInited)
            showToast(mContext.getString(R.string.no_wifi_msg));
        }
    }
    //得到可用wifi
    private void getWifiMsg(List<String> list){
        MacStr macStr=new MacStr(list);
        ApiManager.mApiService.getUserInfoByMac(macStr).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<ArrayList<WifiProvider>>() {
                    @Override
                    protected void _onNext(ArrayList<WifiProvider> wifiProviders) {
                        ArrayList<WifiProvider> wifiProviders2= new ArrayList<>();
                        for (WifiProvider w:wifiProviders){
                            for (ScanResult s:listCanUse){
                                if(s.BSSID.equals(w.getMac())){
                                    L.v(TAG, "可用wifi的服务器信息:"+s.BSSID+"\r"+s.SSID);
                                    w.setSSID(s.SSID);
                                    w.setBSSID(s.BSSID);
                                    w.setLevel(WifiManager.calculateSignalLevel(s.level, 5));
                                    wifiProviders2.add(w);
                                    break;
                                }
                            }
                        }
                        mWifiProviders=sortByLevel(wifiProviders2);
                        if(mServiceIView!=null)
                            mServiceIView.showWifiList((mWifiProviders));
                        if(mFragmentView!=null)
                            mFragmentView.showWifiList(mWifiProviders);
                        if(null!=mServiceIView&&null!=mFragmentView)
                            isInited =true;
                        checkCurrentContect();
                        L.v(TAG,"搜索完成，打开搜索开关");
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, "获取用户信息接口异常:"+message);
//                        showToast(message);
                        if(mFragmentView!=null)
                            mFragmentView.showDataView();
                        if(!isInited){
                            if(listCanUse.size()>0){
                                connect(listCanUse.get(0).SSID);
                            }
                        }
                    }
                });
    }


    private void registBordCast(){
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
//                    new Thread(() -> {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }).start();
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
                            if(mServiceIView!=null)
                                mServiceIView.wifiConnectCancle();
                            if(mFragmentView!=null)
                                mFragmentView.wifiConnectCancle();
                            break;
                    }
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION://网络切换
                    int state= NetworkTools.getNetWorkType(context);
                    switch (state){
                        case NetworkTools.NETWORKTYPE_INVALID://断网
                            L.v(TAG,"当前网络状态："+ "断网");
                            if(mServiceIView!=null)
                                mServiceIView.wifiConnectCancle();
                            if(mFragmentView!=null)
                                mFragmentView.wifiConnectCancle();
                            break;
                        case NetworkTools.NETWORKTYPE_2G://2g
                            L.v(TAG,"当前网络状态："+ "2G");
                            if(mServiceIView!=null)
                                mServiceIView.wifiConnectCancle();
                            if(mFragmentView!=null)
                                mFragmentView.wifiConnectCancle();
                            break;
                        case NetworkTools.NETWORKTYPE_3G://3g以上
                            L.v(TAG,"当前网络状态："+ "3G及以上");
                            if(mServiceIView!=null)
                                mServiceIView.wifiConnectCancle();
                            if(mFragmentView!=null)
                                mFragmentView.wifiConnectCancle();
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

    private void checkCurrentContect(){
            WifiInfo wi=wifiAdmin.getConnectWifi();
            if(mWifiProviders!=null&&mWifiProviders.size()>0&&wi!=null){
                L.v(TAG,"当前已连接的网络状态："+wi.getBSSID());
                boolean isCancle=false;//是否有连接到商家wifi
                for (WifiProvider w:mWifiProviders){
                    L.v(TAG,"服务器可用wifi："+w.getBSSID());
                    if(w.getBSSID().equals(wi.getBSSID())){
                        L.v(TAG,"匹配到服务器wifi且已连接");
                        isCancle=true;
                        if(mServiceIView!=null)
                            mServiceIView.wifiConnectSuccess(w);
                        if(mFragmentView!=null)
                            mFragmentView.wifiConnectSuccess(w);
                        if(w.getType()==TYPE_SHOPER_FREE||w.getType()==TYPE_SINGLE_FREE){//当链接是免费wifi
                            MoneyPresenter.openWifi(24*H1);//打开24小时
                        }
                        if(ShopAndPayFragment.isOpenWifi){
                            MoneyPresenter.openWifi(ShopAndPayFragment.allowTime);
                        }
                        break;
                    }
                }
                if(!isCancle){
                    if(mServiceIView!=null)
                        mServiceIView.wifiNoConnect();
                    if(mFragmentView!=null)
                        mFragmentView.wifiNoConnect();
                }
            }
    }

    public void getSlideUrls(){
        ApiManager.mApiService.getHomePageSlide().compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<List<SlideImageUrls>>() {
                    @Override
                    protected void _onNext(List<SlideImageUrls> wifiProviders) {
                        if(mFragmentView!=null)
                            mFragmentView.showSlide(wifiProviders);
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, "获取首页轮播信息接口异常:"+message);
                        showToast(message);
                    }
                });
    }

    public void connect(String ssid){
        wifiAdmin.connect(ssid);
    }

}
