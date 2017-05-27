package com.traffic.wifiapp.common;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.taobao.sophix.SophixManager;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.manager.hotfix.MyHotFixManager;
import com.traffic.wifiapp.mvp.presenter.MoneyPresenter;
import com.traffic.wifiapp.service.WindowsService;
import com.traffic.wifiapp.utils.CrashHandler;
import com.traffic.wifiapp.utils.DeviceUtils;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.SPUtils;

import static com.traffic.wifiapp.common.ConstantField.H1;
import static com.traffic.wifiapp.common.ConstantField.M1;
import static com.traffic.wifiapp.manager.hotfix.MyHotFixManager.isVisblerToUser;
import static com.traffic.wifiapp.utils.NetworkTools.getWifiIp;


/**
 * Created by ray on 2017/4/13.
 */

public class WifiApplication extends Application {
    private static final String TAG ="WifiApplication" ;
    private static WifiApplication instance;

    private User user;

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
        L.isDebug = false;//日志调试开关
        MyHotFixManager.init(this);
        CrashHandler.getInstance().init(this);
        SDKInitializer.initialize(this);//初始化百度地图
        if(isOpenWifi())
        MoneyPresenter.openWifi(5*M1);//打开app 尝试打开免费wifi5min

    }

    private boolean isOpenWifi(){
        long time=0;
        try {
            time= System.currentTimeMillis() - Long.parseLong(String.valueOf(SPUtils.get(getWifiIp(this), 0)));
        }catch (Exception e){
            L.e(e.toString());
            return false;
        }
        if(time>H1*1000){
            return true;
        }
        return false;
    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(DeviceUtils.isAppAtBackground(this)){
            Intent intent=new Intent(this, WindowsService.class);
            startService(intent);
        }
        isVisblerToUser=false;
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}
