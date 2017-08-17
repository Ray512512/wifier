package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.entry.FeedBack;
import com.traffic.wifiapp.bean.response.SimpleResult;
import com.traffic.wifiapp.mvp.view.FeedBackIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;


/**
 * Created by xy on 16/5/16.
 */
public class FeedBackPresenter extends BasePresenter<FeedBackIView> {
    public FeedBackPresenter(Activity mContext, FeedBackIView mView) {
        super(mContext, mView);
    }

    //登录
    public void submit(String title,String content) {
        FeedBack f=new FeedBack(title,content);
        ApiManager.mApiService.feedBack(f).compose(RxHelper.handleResult())
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
