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

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

/*

    public void getSlideUrls(String shopId){
        ApiManager.mApiService.getMoneyPageSlide(shopId).compose(RxHelper.handleResult())
                .subscribe(new RxSubscribe<List<SlideImageUrls>>() {
                    @Override
                    protected void _onNext(List<SlideImageUrls> wifiProviders) {
                        mView.showSlide(wifiProviders);
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
            });
    }
*/

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
                        showToast("暂无商品数据");
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
                        showToast("打赏成功,感谢您的支持！");
                        mView.paySuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                        mView.payFailed();
                        showToast("支付失败");
                    }
                });
    }

    public static void openWifi(long duration){
        String ip= NetworkTools.getWifiIp(WifiApplication.getInstance()),redirect=DEDIRECT;
//        ip="192.168.10.1";
        String url="http://"+ip+":2060/wifidog/shumo?allow="+true+"&duration="+duration+"&redirect="+redirect;
        L.v("openWifi",url);
//        Toast.makeText(WifiApplication.getInstance(),"连接wifi中，请稍后...",Toast.LENGTH_SHORT).show();
        String finalIp = ip;
        ApiManager.mApiService.openWifiByIp(url)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(WifiApplication.getInstance(),"开启wifi失败",Toast.LENGTH_SHORT).show();
                        L.v(TAG,e.toString());
                    }

                    @Override
                    public void onNext(Object o) {
                        SPUtils.put(finalIp,System.currentTimeMillis());
                        Toast.makeText(WifiApplication.getInstance(),"连接成功，可以免费上网了",Toast.LENGTH_SHORT).show();
//                        WifiAdmin.getIntance(mContext).connect(entry.getProvider().getIpAddr());
                    }
                });
    }

}
