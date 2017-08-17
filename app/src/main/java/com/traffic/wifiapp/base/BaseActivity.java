package com.traffic.wifiapp.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.service.WindowsService;
import com.traffic.wifiapp.ui.viewhelper.VaryViewHelper;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.SystemUtil;

import butterknife.ButterKnife;

/**
 * Created by caism on 2017/4/13.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements
        VaryViewHelper.NetWorkErrorListener,BaseIView {
    public Context mContext;
    protected VaryViewHelper mVaryViewHelper;
    protected int viewType = ConstantField.TYPE_VIEW_DATA;
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getInstance(mContext).addActivity(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        initPresenter();
        setMainLayout();
        ButterKnife.bind(this);
        initBeforeData();
        initEvents();
        initAfterData();
    }


    /*
        * 初始化布局
        */
    protected abstract void setMainLayout();

    /**
     * 初始化先前数据
     */
    protected abstract void initBeforeData();

    /**
     * 初始化事件
     */
    protected abstract void initEvents();

    /**
     * 初始化之后数据
     */
    protected abstract void initAfterData();

    /**
     * 初始化p
     **/
    protected abstract void initPresenter() ;

    //设置覆盖加载页面 网络错误页面
    protected void initHelperView(View bindView) {
        mVaryViewHelper = new VaryViewHelper.Builder()
                .setDataView(bindView)//放数据的父布局，逻辑处理在该Activity中处理
                .setLoadingView(LayoutInflater.from(this).inflate(R.layout.view_loading, null))//加载页，无实际逻辑处理
                .setEmptyView(LayoutInflater.from(this).inflate(R.layout.view_empty, null))//空页面，无实际逻辑处理
                .setErrorView(LayoutInflater.from(this).inflate(R.layout.view_error, null))//错误页面
                .setNetWorkErrorView(LayoutInflater.from(this).inflate(R.layout.view_neterror, null))//网络错误页
                .setRefreshListener(this)//错误页点击刷新实现
                .build();
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
    public void showErrorView() {
        if (mVaryViewHelper != null) {
            viewType = ConstantField.TYPE_VIEW_ERROR;
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

    /**
     * 含有Bundle通过Action跳转界面 *
     */
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Class跳转界面 *
     */
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面 *
     */
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 短暂显示Toast提示(来自String) *
     */
    public void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自res) *
     */
    protected void showShortToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onSettingNetWork() {
      SystemUtil.openWifi(this);
    }

    @Override
    public void onBackPressed() {
        AppManager.getInstance(mContext).killActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetachView();
        }
        AppManager.getInstance(mContext).killActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, WindowsService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        WifiApplication.getInstance().startService();
    }
}
