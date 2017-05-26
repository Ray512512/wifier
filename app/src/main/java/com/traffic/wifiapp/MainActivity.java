package com.traffic.wifiapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;

import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.mvp.presenter.MainPresenter;
import com.traffic.wifiapp.mvp.view.MainIView;
import com.traffic.wifiapp.utils.AlertDialogUtil;
import com.traffic.wifiapp.utils.SystemUtil;

import java.util.Timer;
import java.util.TimerTask;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity<MainPresenter> implements MainIView {


    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initBeforeData() {
    }

    @Override
    protected void initEvents() {
      mPresenter.initUI();
    }

    @Override
    protected void initAfterData() {
        MainActivityPermissionsDispatcher.windowPermissionPassWithCheck(this);
    }

    @Override
    protected void initPresenter() {
        mPresenter=new MainPresenter(this,this);
    }

    public MainPresenter getPresenter(){
        return mPresenter;
    }


    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private static boolean isExit = false;
    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            showShortToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
//            System.exit(0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @NeedsPermission({Manifest.permission.SYSTEM_ALERT_WINDOW})
    public void windowPermissionPass(){

    }

    @OnPermissionDenied({Manifest.permission.SYSTEM_ALERT_WINDOW})
    public void windowDenied(){
        AlertDialogUtil.AlertDialog(mContext, "设置", "为给您提供更好的服务，请授予应用悬浮窗权限", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
    }

}
