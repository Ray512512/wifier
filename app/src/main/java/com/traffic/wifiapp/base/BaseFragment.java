package com.traffic.wifiapp.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.ui.viewhelper.VaryViewHelper;
import com.traffic.wifiapp.utils.SystemUtil;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by xy on 16/5/11.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements
        VaryViewHelper.NetWorkErrorListener, VaryViewHelper.TransferListener ,BaseIView{
    protected Context mContext;
    public P mPresenter;
    /**
     * 视图
     **/
    protected LayoutInflater inflater;
    private ViewGroup container;

    protected abstract int inflateContentView();

    protected View rootView;// 根视图
    protected Resources mRes;
    protected boolean hasData = false;
    protected VaryViewHelper mVaryViewHelper;
    protected Dialog loginDialog;
    protected int viewType = ConstantField.TYPE_VIEW_DATA;
    /**
     * 需要初始化Presenter重写
     **/
    protected abstract void initPresenter();

    protected abstract void initView(Bundle savedInstanceSate);

    //用来保存当前界面的状态和恢复上一个界面的状态
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflateContentView() > 0) {
            this.inflater = inflater;
            this.container = container;
            rootView = inflater.inflate(inflateContentView(), null);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ButterKnife.bind(this, rootView);
            mContext = getActivity();
            mRes = getResources();

            initPresenter();
            initView(savedInstanceState);
            return rootView;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 短暂显示Toast提示(来自String) *
     */
    public void showShortToast(String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自res) *
     */
    protected void showShortToast(int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
    }

    private void reShowView(int viewType) {
        switch (viewType) {
            case ConstantField.TYPE_VIEW_EMPTY:
                showEmpty();
                break;
            case ConstantField.TYPE_VIEW_ERROR:
                showErrorView();
                break;
            case ConstantField.TYPE_VIEW_DATA:
                showDataView();
                break;
        }
    }
    protected void onCreateView(Bundle savedInstanceState) {

    }

    public void setContentView(View view) {
        rootView = view;
    }

    public void setContentView(int layoutResID) {
        setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
    }

    public View getContentView() {
        return rootView;
    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Action跳转界面
     **/
    protected void startActivity(String action, Uri uri) {
        Intent intent = new Intent(action, uri);
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    //刷新覆盖
    public void onRefresh() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    //设置覆盖加载页面 网络错误页面
    protected void initHelperView(View bindView) {
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(bindView)//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(getActivity()).inflate(R.layout.view_loading, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.view_empty, null))//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(getActivity()).inflate(R.layout.view_error, null))//错误页面
                .setNetWorkErrorView(LayoutInflater.from(getActivity()).inflate(R.layout.view_neterror, null))//网络错误页
//                .setTransferMineView(LayoutInflater.from(getActivity()).inflate(R.layout.view_transfer_mine, null))//我的中转页
                .setRefreshListener(this)//错误页点击刷新实现
                .setTransferListener(this)//点击跳转登录页实现
                .build();
    }

    //展示空页面
    public void showEmpty() {
        if (mVaryViewHelper != null) {
            viewType = ConstantField.TYPE_VIEW_EMPTY;
            mVaryViewHelper.showEmptyView();
        }
    }

    //展示加载页
    public void showLoadingView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showLoadingView();
        }
    }

    //展示网络错误页
    public void showNetWorkErrorView() {
        if (mVaryViewHelper != null) {
            mVaryViewHelper.showNetWorkErrorView();
        }
    }

    //展示数据页
    public void showDataView() {
        if (mVaryViewHelper != null) {
            viewType = ConstantField.TYPE_VIEW_DATA;
            mVaryViewHelper.showDataView();
        }
    }

    //展示错误页
    public void showErrorView() {
        if (mVaryViewHelper != null) {
            viewType = ConstantField.TYPE_VIEW_ERROR;
            mVaryViewHelper.showNetWorkErrorView();
        }
    }


    //打开设置网络
    @Override
    public void onSettingNetWork() {
        SystemUtil.openWifi(mContext);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //跳转去登录
    @Override
    public void onLoginAction() {
    }

    @Override
    public void onRefreshSuccess() {

    }

    @Override
    public void onRefreshFaile() {

    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void stopLoading() {

    }
}
