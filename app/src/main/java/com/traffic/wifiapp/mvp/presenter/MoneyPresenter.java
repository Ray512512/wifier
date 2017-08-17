package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.entry.OderEntry;
import com.traffic.wifiapp.bean.response.Goods;
import com.traffic.wifiapp.bean.response.WXOrderInfo;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.manager.wxpay.WXManager;
import com.traffic.wifiapp.mvp.view.MoneyIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.NetworkTools;
import com.traffic.wifiapp.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by xy on 16/5/16.
 */
public class MoneyPresenter extends BasePresenter<MoneyIView> {
    public static final String TAG = "MoneyPresenter";

    public MoneyPresenter(Activity mContext, MoneyIView mView) {
        super(mContext, mView);
    }

    private WXManager wxManager;

    public void getgetShowGoods(String shopId) {
        L.v(TAG, shopId);
        ApiManager.mApiService.getShowGoods(shopId).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<ArrayList<Goods>>() {
                    @Override
                    protected void _onNext(ArrayList<Goods> goodses) {
                        mView.showGoods(goodses);
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, "获取商品数据失败");
                        mView.showGoods(null);
                    }
                });
    }

    public void getWXOrderInfo(OderEntry o) {
        if (o == null) return;
        L.v(TAG, o.toString());
        ApiManager.mApiService.getOrderInfo(o).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<WXOrderInfo>(mContext, mContext.getString(R.string.pay_get_order)) {
                    @Override
                    protected void _onNext(WXOrderInfo wxOrderInfo) {
                        wxOrderInfo.setOderEntry(o);
                        if (wxManager == null) wxManager = new WXManager(mContext);
                        wxManager.startPay(wxOrderInfo);
                        L.v("getWXOrderInfo", wxOrderInfo.getReturn_msg());
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, message);
                        showToast(message);
                    }
                });
    }


    public void queryPayResult(String transactionId) {
        String logid = transactionId.split("&")[0];
        if (TextUtils.isEmpty(logid)) {
            showToast(mContext.getString(R.string.pay_success2));
            return;
        }
        HashMap map = new HashMap();
        map.put("user_id", WifiApplication.getInstance().getUser().getUser_id());
        map.put("log_id", logid);
        ApiManager.mApiService.queryPayResult(map).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<Object>(mContext, mContext.getString(R.string.pay_query_result)) {
                    @Override
                    protected void _onNext(Object wxOrderInfo) {
                        mView.paySuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG, message);
                        mView.paySuccess();
                    }
                });
    }

    public static void openWifi(long duration) {
        String gateWay = NetworkTools.getWifiGateWay(WifiApplication.getInstance());
        String ip = NetworkTools.getWifiIp(WifiApplication.getInstance());
        String mac = NetworkTools.getMacAddress();
        String login = "http://" + gateWay + "/cgi-bin/luci/smart/method?login&ip=" + ip + "&mac=" + mac + "&lease=" + duration / 60;
        String logout = "http://" + gateWay + "/cgi-bin/luci/smart/method?logout&ip=" + ip + "&mac=" + mac + "&lease=" + 0;
        L.v("openWifi", login + "\n" + logout);
        String finalIp = ip;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        new Thread() {
            public void run() {
                Request request=null;
                Response response=null;
                try {
                    request= new Request.Builder().url(logout).build();
                    response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {//先执行关闭操作
                        L.f(TAG, "关闭wifi成功：" + logout);
                        request = new Request.Builder().url(login).build();
                        response = mOkHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {//
                            L.f(TAG, "开启wifi成功：" + login);
                            SPUtils.put(finalIp, System.currentTimeMillis());
                            Toast.makeText(WifiApplication.getInstance(), WifiApplication.getInstance().getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
                        }

                      }
                    } catch (Exception e) {
                    e.printStackTrace();
                    L.f(TAG, "尝试打开失败" + "url:+" + (request != null ? request.url() : null) + "失败原因：" + e.toString());
                }
            }
        }.start();
    }


}
