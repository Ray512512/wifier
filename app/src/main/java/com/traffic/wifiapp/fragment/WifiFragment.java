package com.traffic.wifiapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.adatper.WifiAdapter;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.mvp.presenter.WifiPresenter;
import com.traffic.wifiapp.mvp.view.WifiIView;
import com.traffic.wifiapp.ui.view.BannerLayout;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.webclient.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_PAY;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_PAY;
import static com.traffic.wifiapp.manager.window.WifiWindowManager.RECIVER_ACTION;

/**
 * Created by caism on 2017/4/14.
 */

public class WifiFragment extends BaseFragment<WifiPresenter> implements WifiIView {
    private static final String TAG = "wificonnect";


    @Bind(R.id.wifi_recyclerview)
    RecyclerView recyclerView;
    @Bind(R.id.banner)
    BannerLayout banner;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.wifi_bg)
    ImageView img_bg;

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        return fragment;
    }

    private WifiAdapter adapter;
    private WifiProvider currentContectWifi;



    @Override
    protected int inflateContentView() {
        return R.layout.layout_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new WifiPresenter((Activity) mContext, this);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        initHelperView(recyclerView);
        showLoadingView();
        mPresenter.registBordCast();
        mPresenter.getSlideUrls();
        tvTitle.setText("WIFI");

        //初始化wifi列表控件
        adapter = new WifiAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            WifiProvider wifiProvider=adapter.getItem(position);
            if(currentContectWifi!=null){
                if(currentContectWifi.getBSSID().equals(wifiProvider.getBSSID())){
                    AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,wifiProvider);
                    return;//已经成功链接的wifi 和点击的wifi是同一个则什么都不做
                }
            }
            switch (wifiProvider.getType()){
                case TYPE_SHOPER_FREE:
//                    showLoadingView();
                    mPresenter.connect(wifiProvider.getSSID());
                    AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,wifiProvider);
                    break;
                case TYPE_SINGLE_FREE:
//                    showLoadingView();
                    mPresenter.connect(wifiProvider.getSSID());
                    AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,wifiProvider);
                    break;
                case TYPE_SHOPER_PAY:
                    AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,wifiProvider);
                    break;
                case TYPE_SINGLE_PAY:
                    AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,wifiProvider);
                    break;
            }
        });

        banner.setOnBannerItemClickListener(position -> {
            if(slideImageUrlses!=null){
               String clickUrl= slideImageUrlses.get(position).getLink_url();
                Intent intent=new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url",clickUrl);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.isSearching=false;
        mPresenter.getScanWifiList();
    }

    public ArrayList<WifiProvider> getWifiList(){
        if(adapter==null)return null;
        return adapter.getData();
    }
    public WifiProvider getConnectWifi(){
        return currentContectWifi;
    }
    @Override
    public void showWifiList(ArrayList<WifiProvider> wifiMsgs) {
            adapter.setAll(wifiMsgs);
            showDataView();
            AppManager.getInstance(mContext).getMainActivity().getPresenter().getMapFragment().refresh();
    }

    @Override
    public void showDataView() {
        super.showDataView();
        mPresenter.isSearching=false;
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
    }

    @Override
    public void wifiConnectSuccess(WifiProvider s) {
        currentContectWifi=s;
        showDataView();
        showShortToast("连接成功");
        adapter.setConnectWifiState(s);
        mContext.sendBroadcast(new Intent(RECIVER_ACTION));
    }

    @Override
    public void wifiConnectCancle() {
        adapter.setConnectWifiState(null);
        currentContectWifi=null;
        mContext.sendBroadcast(new Intent(RECIVER_ACTION));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unregistBordCast();
        ButterKnife.unbind(this);
    }

    public void refreshWifiList(){
        adapter.notifyDataSetChanged();
    }

}
