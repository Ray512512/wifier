package com.traffic.wifiapp.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.utils.AppManager;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{

    private static final String TAG = "WXManager";
    private IWXAPI api;
    private boolean isPaySuccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ConstantField.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void setMainLayout() {
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initAfterData() {
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PayResp payResp = (PayResp) resp;
            String transactionId = payResp.extData;
            if (resp.errCode == 0) {
                isPaySuccess = true;
            } else {
                isPaySuccess = false;
            }
            Log.v(TAG, "errCode="+payResp.errCode+"\terrStr="+payResp.errStr+"\textData"+payResp.extData
            +"\treturnKey="+payResp.returnKey+"\tprepayId="+payResp.prepayId+"\tcheckArgs="+payResp.checkArgs()+
            "\topenId="+payResp.openId+"\tgetType="+payResp.getType()+"\ttransaction="+payResp.transaction);
            if(isPaySuccess){
                MainActivity activity= AppManager.getInstance(mContext).getMainActivity();
                if(activity!=null){
                    activity.getPresenter().getPayFragment().dealPayWifiR(transactionId);
                }
            }else {
                showShortToast("支付失败");
            }
        }
        finish();
    }
}