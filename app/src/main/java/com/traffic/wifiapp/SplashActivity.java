package com.traffic.wifiapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.login.LoginActivity;
import com.traffic.wifiapp.utils.AlertDialogUtil;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.utils.SystemUtil;

import java.util.concurrent.TimeUnit;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@RuntimePermissions
public class SplashActivity extends BaseActivity {

    public static final int CHECK_PEERISSION_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SplashActivityPermissionsDispatcher.locationPermissionPassWithCheck(this);
    }

    @Override
    protected void setMainLayout() {

    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initAfterData() {
    }

    @Override
    protected void initPresenter() {

    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void locationPermissionPass(){
        Observable.just(SPUtils.getStrValue(ConstantField.USER_NAME)).subscribeOn(Schedulers.io()).flatMap(user -> {
            if (TextUtils.isEmpty(user)) {
                return Observable.just(Boolean.FALSE).delay(2, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread());
            } else {
                return Observable.just(Boolean.TRUE).delay(2, TimeUnit.SECONDS).subscribeOn(Schedulers.newThread());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(r -> {
            if (r) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        });
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void locationDenied(){
        AlertDialogUtil.AlertDialog(mContext, "设置", "请同意应用必要权限，否则应用无法正常使用", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onNeverAsk(){
        AlertDialogUtil.AlertDialog(mContext, "设置", "请同意应用必要权限，否则应用无法正常使用", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHECK_PEERISSION_CODE){
            SplashActivityPermissionsDispatcher.locationPermissionPassWithCheck(this);
        }
    }
}
