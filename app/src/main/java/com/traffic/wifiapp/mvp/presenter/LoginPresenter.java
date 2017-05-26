package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.entry.RegistEntry;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.mvp.view.LoginIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.utils.SystemUtil;

import static com.traffic.wifiapp.bean.entry.RegistEntry.REG_LOGIN;


/**
 * Created by xy on 16/5/16.
 */
public class LoginPresenter extends BasePresenter<LoginIView> {
    public LoginPresenter(Activity mContext, LoginIView mView) {
        super(mContext, mView);
    }

    //登录
    public void login(String phone) {
        RegistEntry registEntry=new RegistEntry(REG_LOGIN);
        registEntry.setMobile(phone);
        registEntry.setExt0(SystemUtil.getDeviceId());

        ApiManager.mApiService.registerAndLogin(registEntry).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<User>(mContext, R.string.login_load) {
                    @Override
                    protected void _onNext(User user) {
                        SPUtils.setObject(ConstantField.USER,user);
                        mView.onLoginSuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
                });

    }
}
