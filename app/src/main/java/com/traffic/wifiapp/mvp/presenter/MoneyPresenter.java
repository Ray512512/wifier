package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

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

import static com.traffic.wifiapp.common.ConstantField.DEDIRECT;


/**
 * Created by xy on 16/5/16.
 */
public class MoneyPresenter extends BasePresenter<MoneyIView> {
    public static final String TAG="MoneyPresenter";
    public MoneyPresenter(Activity mContext, MoneyIView mView) {
        super(mContext, mView);
    }
    private WXManager wxManager;

    public void getgetShowGoods(String shopId){
        L.v(TAG,shopId);
        ApiManager.mApiService.getShowGoods(shopId).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<ArrayList<Goods>>() {
                    @Override
                    protected void _onNext(ArrayList<Goods> goodses) {
                        mView.showGoods(goodses);
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG,"获取商品数据失败");
                        mView.showGoods(null);
                    }
                });
    }

    public void getWXOrderInfo(OderEntry o){
        if(o==null)return;
        ApiManager.mApiService.getOrderInfo(o).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<WXOrderInfo>(mContext,"下单中，请稍后...") {
                    @Override
                    protected void _onNext(WXOrderInfo wxOrderInfo) {
                        wxOrderInfo.setOderEntry(o);
                        if (wxManager == null) wxManager = new WXManager(mContext);
                        wxManager.startPay(wxOrderInfo);
                        L.v("getWXOrderInfo",wxOrderInfo.getReturn_msg());
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
                });
    }


    public void queryPayResult(String transactionId){
        String logid=transactionId.split("&")[0];
        if(TextUtils.isEmpty(logid)){
            showToast("支付成功");
            return;
        }
        HashMap map=new HashMap();
        map.put("user_id", WifiApplication.getInstance().getUser().getUser_id());
        map.put("log_id",logid);
        ApiManager.mApiService.queryPayResult(map).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<Object>(mContext,"查询支付结果中，请稍后...") {
                    @Override
                    protected void _onNext(Object wxOrderInfo) {
                        mView.paySuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        L.v(TAG,message);
                        mView.paySuccess();
                    }
                });
    }

    public static void openWifi(long duration){
        String ip= NetworkTools.getWifiIp(WifiApplication.getInstance());
//        ip="192.168.10.1";
        String url="http://"+ip+":2060/wifidog/auth?internet=allow"+"&duration="+duration+"&redirect="+DEDIRECT;
//        FileUtils.writeTxtToFile("尝试打开当前链接wifi开关---"+"url:+"+url,FileUtils.path,"wifi开关调试日志");
        L.v("openWifi",url);
        String finalIp = ip;
        OkHttpClient mOkHttpClient = new OkHttpClient();
        new Thread() {
            public void run() {
                try {
                    Request request = new Request.Builder().url(url).build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        L.f(TAG,"开启免费wifi成功："+url);
                        SPUtils.put(finalIp,System.currentTimeMillis());
                        Toast.makeText(WifiApplication.getInstance(),"连接成功，可以免费上网了",Toast.LENGTH_SHORT).show();
//                        FileUtils.writeTxtToFile("接口访问成功:"+response.toString(),FileUtils.path,"wifi开关调试日志");
                    }else if(response.isRedirect()){
//                        FileUtils.writeTxtToFile("接口重定向:"+response.toString(),FileUtils.path,"wifi开关调试日志");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    L.f(TAG,"尝试打开失败"+"url:+"+url+"失败原因："+e.toString());
                }
            }
        }.start();
    }


}
