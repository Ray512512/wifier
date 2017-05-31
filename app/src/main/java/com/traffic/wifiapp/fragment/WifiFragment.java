package com.traffic.wifiapp.fragment;

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
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.data.WifiUtils;
import com.traffic.wifiapp.mvp.presenter.WifiAppPresenter;
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

/**
 * Created by caism on 2017/4/14.
 */

public class WifiFragment extends BaseFragment implements WifiIView {
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

    private WifiAppPresenter mPresenter;
    private  WifiAdapter adapter;
    private  WifiProvider currentContectWifi;
    private  boolean isJump=true;



    @Override
    protected int inflateContentView() {
        return R.layout.layout_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = WifiApplication.getInstance().getWifiAppPresenter();
        mPresenter.setmFragmentView(this);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        initHelperView(recyclerView);
        showLoadingView();
        mPresenter.getSlideUrls();
        tvTitle.setText("WIFI");

        //初始化wifi列表控件
        adapter = new WifiAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            WifiProvider wifiProvider=adapter.getItem(position);
            int type=wifiProvider.getType();
            switch (type){
                case TYPE_SHOPER_FREE:
                case TYPE_SINGLE_FREE:
                    if(currentContectWifi!=null){
                        if(currentContectWifi.getBSSID().equals(wifiProvider.getBSSID())){
                            AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,wifiProvider);
                            return;//已经成功链接的wifi 和点击的wifi是同一个则什么都不做
                        }
                    }
                    isJump=true;
                    mPresenter.connect(wifiProvider.getSSID());
                    break;
                case TYPE_SHOPER_PAY:
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

    @Override
    public void showWifiList(ArrayList<WifiProvider> wifiMsgs) {
        showDataView();
        if(AppManager.getInstance(mContext).getMainActivity()==null)return;
            adapter.setAll(wifiMsgs);
            WifiApplication.getInstance().setWifiProviders(wifiMsgs);
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
        boolean isFree=WifiUtils.isFreeWifiAndOpenIt(s,false);
        if(isJump) {
            isJump=false;
            if (!isFree) {
                AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS, s);
            } else
                AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS, s);
        }
        currentContectWifi=s;
        WifiApplication.getInstance().setCurrentWifi(s);
        showDataView();
//        showShortToast("连接成功");
        adapter.setConnectWifiState(s);
//        mContext.sendBroadcast(new Intent(RECIVER_ACTION));
    }

    @Override
    public void wifiConnectCancle() {
        WifiApplication.getInstance().setWifiProviders(null);
        adapter.setAll(null);
        WifiApplication.getInstance().setCurrentWifi(null);
        adapter.setConnectWifiState(null);
        currentContectWifi=null;
//        mContext.sendBroadcast(new Intent(RECIVER_ACTION));
    }

    @Override
    public void wifiNoConnect() {
        WifiApplication.getInstance().setCurrentWifi(null);
        adapter.setConnectWifiState(null);
        currentContectWifi=null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mPresenter.setmFragmentView(null);
        ButterKnife.unbind(this);
    }

    public void refreshWifiList(){
        if(adapter!=null)
        adapter.notifyDataSetChanged();
    }

}
