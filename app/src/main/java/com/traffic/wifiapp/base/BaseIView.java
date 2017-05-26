package com.traffic.wifiapp.base;

/**
 * Created by xy on 16/5/11.
 */
public interface BaseIView {
    void onRefreshSuccess();
    void onRefreshFaile();
    void showLoading(String msg);
    void stopLoading();
    void onRefresh();
    void showEmpty();
    void showLoadingView();
    void showNetWorkErrorView();
    void showDataView();
    void showErrorView();
}
