package com.traffic.wifiapp.login;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.mvp.presenter.RegistPresenter;
import com.traffic.wifiapp.mvp.view.RegistIView;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.CodeUtils;
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
    @Bind(R.id.exit_psw)
    EditText exitPsw;
    @Bind(R.id.exit_psw2)
    EditText exitPsw2;
    @Bind(R.id.exit_code)
    EditText exitCode;
    @Bind(R.id.regist_img_code)
    ImageView registImgCode;

    private String phone = "",psw="",psw2="",code="";

    private CodeUtils codeUtils;

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBeforeData() {
        tvTitle.setText(getString(R.string.tag_register));
        registAuth.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initEvents() {
        registImgCode.setImageBitmap(codeUtils.createBitmap());
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter = new RegistPresenter(this, this);
        codeUtils=CodeUtils.getInstance();
    }


    @NeedsPermission({Manifest.permission.READ_PHONE_STATE})
    public void getDeviceIdPerssion() {
        mPresenter.regist(phone,psw);
    }

    @OnPermissionDenied({Manifest.permission.READ_PHONE_STATE})
    public void readPhoneRefuse() {
        showShortToast(getString(R.string.perimmsion_refuse_phone_register));
    }

    @OnClick({R.id.btn_login, R.id.img_back,R.id.regist_img_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                phone = exitPhone.getText().toString().trim();
                psw = exitPsw.getText().toString().trim();
                psw2 = exitPsw2.getText().toString().trim();
                code = exitCode.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    showShortToast(getString(R.string.login_phone_empty));
                    return;
                }
                if (!CommonUtils.isMobile(phone)) {
                    showShortToast(getString(R.string.login_phone_error));
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    showShortToast(getString(R.string.login_psw_empty));
                    return;
                }
                if (TextUtils.isEmpty(psw2)) {
                    showShortToast(getString(R.string.login_sure_psw_empty));
                    return;
                }
                if (!psw.equals(psw2)) {
                    showShortToast(getString(R.string.login_psw_not_equals));
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    showShortToast(getString(R.string.login_code_empty));
                    return;
                }
                if (!codeUtils.getCode().toLowerCase().equals(code.toLowerCase())) {
                    showShortToast(getString(R.string.login_code_error));
                    return;
                }
                    RegisterActivityPermissionsDispatcher.getDeviceIdPerssionWithCheck(this);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.regist_img_code:
                registImgCode.setImageBitmap(codeUtils.createBitmap());
                break;
        }
    }


    @Override
    public void onRegistSuccess() {
        showShortToast(getString(R.string.register_success));
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
