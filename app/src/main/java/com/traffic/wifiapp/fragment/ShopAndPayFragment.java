package com.traffic.wifiapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.adatper.GoodsAdapter;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.bean.entry.OderEntry;
import com.traffic.wifiapp.bean.response.Goods;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.manager.WifiAdmin;
import com.traffic.wifiapp.mvp.presenter.MoneyPresenter;
import com.traffic.wifiapp.mvp.view.MoneyIView;
import com.traffic.wifiapp.ui.view.BannerLayout;
import com.traffic.wifiapp.ui.view.swipefreshReccycleview.MyRecyclerView;
import com.traffic.wifiapp.ui.view.swipefreshReccycleview.MySwipeRefreshLayout;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.NetworkTools;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.traffic.wifiapp.common.ConstantField.H1;
import static com.traffic.wifiapp.common.ConstantField.H24;
import static com.traffic.wifiapp.common.ConstantField.H4;

/**
 * Created by ray on 2017/4/15.
 */

public class ShopAndPayFragment extends BaseFragment<MoneyPresenter> implements MoneyIView {
    public static final int MONEY_CHECK_1H = 1;
    public static final int MONEY_CHECK_4H = 2;
    public static final int MONEY_CHECK_24H = 3;

    public static final int TYPE_SHOW_NORMAL = 1;//打赏给平台
    public static final int TYPE_SHOW_PAYS = 2;//打赏给商家 私人
    public static final int TYPE_SHOW_GOODS = 3;//显示商品
    private static final java.lang.String TAG = "ShopAndPayFragment";


    @Bind(R.id.img_back) //收费wifi
    ImageView imgBack;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fragment_money_root)
    LinearLayout fragmentMoneyRoot;
    @Bind(R.id.money_name)
    TextView moneyName;
    @Bind(R.id.money1_liner)
    LinearLayout money1Liner;
    @Bind(R.id.money2_liner)
    LinearLayout money2Liner;
    @Bind(R.id.money3_liner)
    LinearLayout money3Liner;


    @Bind(R.id.et_pya)//打赏平台
    EditText payEt;
    @Bind(R.id.platform_root)
    LinearLayout platformRoot;

    @Bind(R.id.show_goods)
    LinearLayout showGoods;
    /* @Bind(R.id.wifi_recyclerview)
     RecyclerView wifiRecyclerview;
     @Bind(R.id.show_goods_pullto)
     PullToRefreshLayout showGoodsPullto;*/
    @Bind(R.id.banner)
    BannerLayout banner;
    @Bind(R.id.wifi_recycler)
    MyRecyclerView wifiRecycler;
    @Bind(R.id.wifi_refresh)
    MySwipeRefreshLayout wifiRefresh;
    @Bind(R.id.money1_price)
    TextView money1Price;
    @Bind(R.id.money2_price)
    TextView money2Price;
    @Bind(R.id.money3_price)
    TextView money3Price;
    @Bind(R.id.money1_time)
    TextView money1Time;
    @Bind(R.id.money2_time)
    TextView money2Time;
    @Bind(R.id.money3_time)
    TextView money3Time;


    private WifiProvider mWifiProvider;
    private int checkMoney = 1;

    private GoodsAdapter goodsAdapter;
    private int viewType = TYPE_SHOW_NORMAL;
    private OderEntry currentPayWifi;//当前等待付费的wifi对象  包含订单，商家wifi信息 等


    public void setmWifiProvider(WifiProvider mWifiProvider) {
        if(viewType==TYPE_SHOW_NORMAL)return;
        this.mWifiProvider = mWifiProvider;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public static ShopAndPayFragment newInstance() {
        ShopAndPayFragment fragment = new ShopAndPayFragment();
        return fragment;
    }


    @Override
    protected int inflateContentView() {
        return R.layout.three_fragment;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MoneyPresenter(getActivity(), this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()&&currentPayWifi==null&&platformRoot!=null) {
            platformRoot.setVisibility(View.GONE);
            fragmentMoneyRoot.setVisibility(View.GONE);
            showGoods.setVisibility(View.GONE);
            if (getActivity() == null) return;
            switch (viewType) {
                case TYPE_SHOW_PAYS://收费wifi  商家和私人
                    fragmentMoneyRoot.setVisibility(View.VISIBLE);
                    if (mWifiProvider != null){
                        moneyName.setText(mWifiProvider.getShopname());
                    }
                    break;
                case TYPE_SHOW_NORMAL://打赏给平台
                    platformRoot.setVisibility(View.VISIBLE);
                    break;
                case TYPE_SHOW_GOODS://免费商家wifi 进行商品显示
                    if (mWifiProvider != null){
                        mPresenter.getgetShowGoods(mWifiProvider.getShop_id());
                    }
                    showGoods.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        imgBack.setVisibility(View.INVISIBLE);
        tvTitle.setText("商品");
        payEt.setText("0.1");
        //设置只允许输入小数点后两位
//        InputFilter.setMoneyFilter(payEt);
        setMoneyCheck();

        goodsAdapter = new GoodsAdapter();
        wifiRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        wifiRecycler.setAdapter(goodsAdapter);
        wifiRecycler.initLayout(wifiRefresh, goodsAdapter);
        wifiRecycler.openMoreEnabled(false, false);
        wifiRecycler.setLoadingListener(() -> {
            wifiRecycler.loadMoreComplete();
        });
        wifiRefresh.setOnRefreshListener(() -> {
            if (mWifiProvider != null){
                mPresenter.getgetShowGoods(mWifiProvider.getShop_id());
            }
        });

        banner.setOnBannerItemClickListener(position -> {
           /* if (slideImageUrlses != null) {
                String clickUrl = slideImageUrlses.get(position).getLink_url();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", clickUrl);
                startActivity(intent);
            }*/
        });

    }


    private void setMoneyCheck() {
        setViewAllSelect(money1Liner, false);
        setViewAllSelect(money2Liner, false);
        setViewAllSelect(money3Liner, false);
        switch (checkMoney) {
            case MONEY_CHECK_1H:
                setViewAllSelect(money1Liner, true);
                break;
            case MONEY_CHECK_4H:
                setViewAllSelect(money2Liner, true);
                break;
            case MONEY_CHECK_24H:
                setViewAllSelect(money3Liner, true);
                break;
        }
    }

    private void setViewAllSelect(ViewGroup view, boolean isSelected) {
        view.setSelected(isSelected);
        for (int i = 0; i < view.getChildCount(); i++) {
            view.getChildAt(i).setSelected(isSelected);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.money1_liner, R.id.money2_liner, R.id.money3_liner, R.id.monet_btn_pay, R.id.monet_btn_pay_platform})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.money1_liner:
                checkMoney = MONEY_CHECK_1H;
                setMoneyCheck();
                break;
            case R.id.money2_liner:
                checkMoney = MONEY_CHECK_4H;
                setMoneyCheck();
                break;
            case R.id.money3_liner:
                checkMoney = MONEY_CHECK_24H;
                setMoneyCheck();
                break;
            case R.id.monet_btn_pay://收费wifi
                mPresenter.getWXOrderInfo(getOrder());
                break;
            case R.id.monet_btn_pay_platform://打赏给平台
                String money = payEt.getText().toString();
                mPresenter.getWXOrderInfo(new OderEntry("0",money,"打赏给平台"));
                break;
        }
    }

    private OderEntry getOrder() {
        if(mWifiProvider==null){
            showShortToast("未获取到wifi商户信息");
            return null;
        }
        String price="", msg="", text;
        long time=0;
        switch (checkMoney) {
            case MONEY_CHECK_1H:
                text = money1Price.getText().toString();
                price = text.substring(0, text.length() - 1);
                msg = "购买了"+money1Time.getText().toString()+"wifi使用";
                time= H1;
                break;
            case MONEY_CHECK_4H:
                text = money2Price.getText().toString();
                price = text.substring(0, text.length() - 1);
                msg = "购买了"+money2Time.getText().toString()+"wifi使用";
                time= H4;
                break;
            case MONEY_CHECK_24H:
                text = money3Price.getText().toString();
                price = text.substring(0, text.length() - 1);
                msg = "购买了"+money2Time.getText().toString()+"wifi使用";
                time= H24;
                break;
        }
        mWifiProvider.setIpAddr(NetworkTools.getWifiIp(mContext));
        currentPayWifi=new OderEntry(mWifiProvider.getShop_id(),price,msg);
        currentPayWifi.setProvider(mWifiProvider);
        currentPayWifi.setAllowTime(time);
        return currentPayWifi;
    }


    @Override
    public void showGoods(ArrayList<Goods> goodses) {
        wifiRefresh.setRefreshing(false);
        if(goodses==null){
            return;
        }
        goodsAdapter.setNewData(goodses);
        ArrayList<String> slides=new ArrayList<>();
        for (Goods goods:goodses){
            if(!TextUtils.isEmpty(goods.getPhotolb())){
                slides.add(goods.getPhotolb());
            }
        }
        if(slides.size()==0){
            banner.setVisibility(View.GONE);
        }else {
            banner.setVisibility(View.VISIBLE);
            banner.setViewUrls(slides);
        }
    }

    @Override
    public void payFailed() {
//        currentPayWifi=null;
    }

    @Override
    public void paySuccess() {
//        Log.v("paySuccess", "当前Activity"+ AppManager.getInstance(mContext).getTopActivity().getClass().getSimpleName());
//        mPresenter.openWifi(currentPayWifi.getAllowTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public static boolean isOpenWifi=false;
    public static long allowTime=0;
    public void dealPayWifiR(String t,boolean isSuccess){
        if(TextUtils.isEmpty(t)||!isSuccess){
                return;
        }
        isOpenWifi=true;
        showShortToast("打赏成功,感谢您的支持！");
        new Handler().postDelayed(() -> {
            isOpenWifi = false;
            allowTime=0;
        },10*1000);
        try {
        allowTime=Long.parseLong(t);
        WifiAdmin.getIntance(mContext).connect(currentPayWifi.getProvider().getSSID());
//        MoneyPresenter.openWifi();
        }catch (Exception e){
            L.e(TAG,e.toString());
        }
        currentPayWifi=null;
    }
}
