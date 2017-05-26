package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.entry.RegistEntry;
import com.traffic.wifiapp.mvp.view.RegistIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.SystemUtil;

import static com.traffic.wifiapp.bean.entry.RegistEntry.REG_REGIST;


/**
 * Created by xy on 16/5/16.
 */
public class RegistPresenter extends BasePresenter<RegistIView> {
    public RegistPresenter(Activity mContext, RegistIView mView) {
        super(mContext, mView);
    }

    //注册
    public void regist(String phone) {
        RegistEntry registEntry=new RegistEntry(REG_REGIST);
        registEntry.setMobile(phone);
        registEntry.setExt0(SystemUtil.getDeviceId());

        ApiManager.mApiService.registerAndLogin(registEntry).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<User>(mContext, R.string.regist_load) {
                    @Override
                    protected void _onNext(User u) {
                        mView.onRegistSuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
                });

    }
}
