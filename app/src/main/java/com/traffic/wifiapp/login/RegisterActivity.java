package com.traffic.wifiapp.login;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.mvp.presenter.RegistPresenter;
import com.traffic.wifiapp.mvp.view.RegistIView;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.CommonUtils;
import com.traffic.wifiapp.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class RegisterActivity extends BaseActivity<RegistPresenter> implements RegistIView {

    @Bind(R.id.exit_phone)
    EditText exitPhone;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.regist_auth)
    TextView registAuth;

    private String phone = "";

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBeforeData() {
        tvTitle.setText("注册");
        registAuth.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegistPresenter(this, this);
    }


    @NeedsPermission({Manifest.permission.READ_PHONE_STATE})
    public void getDeviceIdPerssion() {
        mPresenter.regist(phone);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE})
    public void readPhoneRefuse() {
        showShortToast("拒绝了读取设备信息权限，您将无法进行注册");
    }

    @OnClick({R.id.btn_login, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                phone = exitPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showShortToast("请输入手机号");
                }
                if (!CommonUtils.isMobile(phone)) {
                    showShortToast("请输入正确的手机号");
                } else {
                    RegisterActivityPermissionsDispatcher.getDeviceIdPerssionWithCheck(this);
                }
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }


    @Override
    public void onRegistSuccess() {
        showShortToast("注册成功");
        SPUtils.put(ConstantField.USER_NAME, exitPhone.getText().toString().trim());
        AppManager.getInstance(this).killAllActivity();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        RegisterActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegisterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
