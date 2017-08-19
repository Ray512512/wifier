package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;

import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.mvp.view.MineIView;


/**
 * Created by pl on 17/6/6
 */
public class MinePresenter extends BasePresenter<MineIView> {
    public MinePresenter(Activity mContext, MineIView mView) {
        super(mContext, mView);
    }


}
