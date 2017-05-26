package com.traffic.wifiapp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by wxy on 2017/2/15.
 */

public abstract class BaseLazyFragment<P extends BasePresenter> extends BaseFragment {
    public P mPresenter;
    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    //标志当前页面是否可见
    private boolean isVisible;
    protected abstract void getData();

    private boolean canRequest = true;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    @Override
    public void onPause() {
        super.onPause();
        canRequest = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        canRequest = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }


    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }
        if (canRequest) {
            getData();//数据请求
        }
    }

    protected void onInvisible() {
    }
}
