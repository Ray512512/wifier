package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.response.Message;
import com.traffic.wifiapp.mvp.view.MessageIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;

import java.util.ArrayList;


/**
 * Created by xy on 16/5/16.
 */
public class MessagePresenter extends BasePresenter<MessageIView> {
    public MessagePresenter(Activity mContext, MessageIView mView) {
        super(mContext, mView);
    }

    public void getMessage() {
        ApiManager.mApiService.getMessageList().compose(RxHelper.handleResult())
                    .subscribe(new RxSubscribe<ArrayList<Message>>() {
                        @Override
                        protected void _onNext(ArrayList<Message> messages) {
                            mView.getMessageList(messages);
                        }

                        @Override
                        protected void _onError(String message) {
                         showToast(message);
                         mView.getMessageFailed();
                        }
                    });

    }
}
