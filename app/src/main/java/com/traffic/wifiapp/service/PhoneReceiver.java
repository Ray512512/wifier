package com.traffic.wifiapp.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class PhoneReceiver extends BroadcastReceiver {
	public static final String BOOT_COMPLETED="android.intent.action.BOOT_COMPLETED";

	private static String TAG="PhoneReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service=null;
		switch (intent.getAction()){
			case BOOT_COMPLETED://监听开机启动
				service=new Intent(context,WindowsService.class);
				context.startService(service);
				Log.i(TAG, "开机监听启动服务！!");
				break;
			case WindowsService.RELAODING_SERVICE://服务被销毁后再次启动服务
				 service=new Intent(context,WindowsService.class);
				context.startService(service);
				Log.i(TAG, "ondestroy重新启动服务！!");
				break;
			case WindowsService.RESTART_SERVICE://服务被销毁后再次启动服务
				service=new Intent(context,WindowsService.class);
				context.startService(service);
				Log.i(TAG, "stopservice重新启动服务");
				break;
		}
	}



}
