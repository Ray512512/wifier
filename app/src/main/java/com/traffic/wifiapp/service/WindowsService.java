package com.traffic.wifiapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.traffic.wifiapp.manager.window.WifiWindowManager;

/**
 * Created by ray on 2017/4/13.
 */
public class WindowsService extends Service {
    private final String TAG = "WindowsService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        WifiWindowManager.getIntance(this).init();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        WifiWindowManager.getIntance(this).getDataFromWifiFragment();
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        WifiWindowManager.getIntance(this).onDestory();
        Log.i(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
