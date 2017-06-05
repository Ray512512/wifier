package com.traffic.wifiapp.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.mvp.presenter.BaiduMapPresenter;
import com.traffic.wifiapp.mvp.view.BaiduMapIView;
import com.traffic.wifiapp.ui.view.BannerLayout;
import com.traffic.wifiapp.utils.AnimaUtil;
import com.traffic.wifiapp.webclient.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ray on 2017/4/15.
 */

public class MapFragment extends BaseFragment<BaiduMapPresenter> implements BaiduMapIView {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.mapView)
    MapView mMapView;
    @Bind(R.id.img_map_up_down)
    ImageView imgMapUpDown;
    @Bind(R.id.tv_map_up_down)
    TextView tvMapUpDown;
    @Bind(R.id.img_action)
    ImageView imgAction;
    @Bind(R.id.banner)
    BannerLayout banner;
    @Bind(R.id.map_up_down)
    LinearLayout mapUpDown;
    @Bind(R.id.map_top_view)
    LinearLayout top_view;


    private BaiduMap mBaiduMap;
    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }


    @Override
    protected int inflateContentView() {
        return R.layout.layout_second;
    }

    @Override
    protected void initPresenter() {
        mBaiduMap = mMapView.getMap();
        mPresenter=new BaiduMapPresenter(getActivity(),this,mBaiduMap);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        imgBack.setVisibility(View.INVISIBLE);
        tvTitle.setText("附近");

        mPresenter.initLocation();
        mPresenter.getSlideUrls();
        banner.setOnBannerItemClickListener(position -> {
            if(slideImageUrlses!=null){
                WebViewActivity.start(mContext,slideImageUrlses.get(position).getLink_url());
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            refresh();
        }
    }

    public void refresh(){
        if(mPresenter!=null)
        mPresenter.showWifiPoint(WifiApplication.getInstance().getWifiProviders());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.map_up_down,R.id.map_setmyLocation})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_up_down:
                if(tvMapUpDown.getText().equals(getString(R.string.up))){
                    tvMapUpDown.setText(getString(R.string.down));
                    AnimaUtil.RotateView(imgMapUpDown,true);
                    AnimaUtil.TranslateGoneViewSelfYdown(top_view,banner);
                }else {
                    tvMapUpDown.setText(getString(R.string.up));
                    AnimaUtil.RotateView(imgMapUpDown,false);
                    AnimaUtil.TranslateShowViewSelfYup(top_view,banner);
                }
                break;
            case R.id.map_setmyLocation:
                mPresenter.setMyLocation();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mMapView!=null)
        mMapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if(mMapView!=null)
        mMapView.onResume();
        mPresenter.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mMapView!=null)
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mMapView!=null)
        mPresenter.onStop();
    }


    private ArrayList<SlideImageUrls> slideImageUrlses;
    @Override
    public void showSlide(List<SlideImageUrls> urls) {
        slideImageUrlses= (ArrayList<SlideImageUrls>) urls;
        List<String> showUrls=new ArrayList<>();
        List<String> clickUrls=new ArrayList<>();
        for (SlideImageUrls s:urls){
            showUrls.add(s.getPic_url());
            clickUrls.add(s.getLink_url());
        }
        banner.setViewUrls(showUrls);
        if(showUrls.size()!=0){
            mapUpDown.setVisibility(View.VISIBLE);
        }
    }
}
