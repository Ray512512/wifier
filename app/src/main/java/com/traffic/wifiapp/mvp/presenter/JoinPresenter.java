package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.entry.AddShop;
import com.traffic.wifiapp.bean.response.SimpleResult;
import com.traffic.wifiapp.mvp.view.JoinIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;


/**
 * Created by xy on 16/5/16.
 */
public class JoinPresenter extends BasePresenter<JoinIView> {
    public JoinPresenter(Activity mContext, JoinIView mView) {
        super(mContext, mView);
    }

    //登录
    public void submit(String name,String phone,String shopName,String addr) {
        AddShop addShop=new AddShop(name,phone,shopName,addr);
        ApiManager.mApiService.addShop(addShop).compose(RxHelper.handleResult())
                    .subscribe(new RxSubscribe<SimpleResult>(mContext, R.string.mine_join_submit_success) {
                        @Override
                        protected void _onNext(SimpleResult r) {
                            if(r!=null){
                                showToast(r.getMsg());
                                if(r.isIsSuccess())
                                    mView.submitSuccess();
                            }
                        }
                        @Override
                        protected void _onError(String message) {
                            showToast(message);
                        }
                    });

    }
}
