package com.traffic.wifiapp.common;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.taobao.sophix.SophixManager;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.manager.hotfix.MyHotFixManager;
import com.traffic.wifiapp.mvp.presenter.MoneyPresenter;
import com.traffic.wifiapp.mvp.presenter.WifiAppPresenter;
import com.traffic.wifiapp.service.WindowsService;
import com.traffic.wifiapp.utils.CrashHandler;
import com.traffic.wifiapp.utils.DeviceUtils;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.SPUtils;

import java.util.ArrayList;
import java.util.Calendar;

import static com.traffic.wifiapp.common.ConstantField.H1;
import static com.traffic.wifiapp.common.ConstantField.M1;
import static com.traffic.wifiapp.manager.hotfix.MyHotFixManager.isVisblerToUser;
import static com.traffic.wifiapp.utils.NetworkTools.getWifiIp;


/**
 * Created by ray on 2017/4/13.
 */

public class WifiApplication extends Application{
    private static final String TAG ="WifiApplication" ;
    private static WifiApplication instance;

    private WifiAppPresenter mWifiPresenter;
    private ArrayList<WifiProvider> wifiProviders;
    private WifiProvider currentWifi;
    private User user;

    public WifiAppPresenter getWifiAppPresenter() {
        return mWifiPresenter;
    }

    public void setmWifiPresenter(WifiAppPresenter mWifiPresenter) {
        this.mWifiPresenter = mWifiPresenter;
    }

    public ArrayList<WifiProvider> getWifiProviders() {
        return wifiProviders;
    }

    public void setWifiProviders(ArrayList<WifiProvider> wifiProviders) {
        this.wifiProviders = wifiProviders;
    }

    public WifiProvider getCurrentWifi() {
        return currentWifi;
    }

    public void setCurrentWifi(WifiProvider currentWifi) {
        this.currentWifi = currentWifi;
    }

    public User getUser() {
        user= SPUtils.getObject(ConstantField.USER);
        if(user==null)user=new User();
        return user;
    }

    public static WifiApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        L.isDebug = true;//日志调试开关
        MyHotFixManager.init(this);
        CrashHandler.getInstance().init(this);
        SDKInitializer.initialize(this);//初始化百度地图
//        if(isOpenWifi())
        MoneyPresenter.openWifi(10*M1);//打开app 尝试打开免费wifi10min
    }

    private boolean isOpenWifi(){
        long time=0;
        try {
            time= System.currentTimeMillis() - Long.parseLong(String.valueOf(SPUtils.get(getWifiIp(this), 0)));
        }catch (Exception e){
            L.e(e.toString());
            return false;
        }
        if(time>H1*1000){//毫秒级别 x1000
            return true;
        }
        return false;
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(DeviceUtils.isAppAtBackground(this)&&mWifiPresenter!=null){
//            mWifiPresenter.setIView(this);
            Intent intent=new Intent(this, WindowsService.class);
            startService(intent);
        }
        isVisblerToUser=false;
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
