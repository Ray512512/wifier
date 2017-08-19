package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.base.BaseIView;

/**
 * Created by xy on 16/5/16.
 */
public interface UserInfoIView extends BaseIView {
    void refreshAddr(String addr);

    void saveInfoSuccess();
}
