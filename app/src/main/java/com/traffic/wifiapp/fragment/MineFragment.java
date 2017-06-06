package com.traffic.wifiapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.mvp.presenter.MinePresenter;
import com.traffic.wifiapp.mvp.view.MineIView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ray on 2017/6/6.
 * emial:1452011874@qq.com
 */

public class MineFragment extends BaseFragment<MinePresenter> implements MineIView {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter((Activity) mContext, this);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        imgBack.setVisibility(View.INVISIBLE);
        tvTitle.setText(getString(R.string.tab_four));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
