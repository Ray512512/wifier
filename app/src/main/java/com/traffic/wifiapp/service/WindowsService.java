package com.traffic.wifiapp.service;

import android.app.Notification;
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
    private static final int GRAY_SERVICE_ID = 1;
    public static final String RELAODING_SERVICE="android.intent.relodingservice";
    public static final String RESTART_SERVICE="android.intent.restartservice";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        /**升级服务为隐形前台进程
         * 灰色保护
         * */
        Intent innerIntent = new Intent(this, GrayInnerService.class);
        startService(innerIntent);
        startForeground(GRAY_SERVICE_ID, new Notification());
        WifiWindowManager.getIntance(this).init();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        WifiWindowManager.getIntance(this).getDataFromWifiFragment();
        return Service.START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        WifiWindowManager.getIntance(this).onDestory();
        Log.i(TAG, "onDestroy");
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class GrayInnerService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
