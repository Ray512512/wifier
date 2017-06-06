package com.traffic.wifiapp.manager.wxpay;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.response.WXOrderInfo;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.MD5Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Ray on 2017/5/14.
 * email：1452011874@qq.com
 */

public class WXManager {
public static final String TAG="WXManager";
    private IWXAPI iwxapi;
    private Context mContext;

    public WXManager(Context mContext) {
        this.mContext = mContext;
        retToWX();
    }

    private void retToWX(){
        iwxapi= WXAPIFactory.createWXAPI(mContext.getApplicationContext(),null);
        iwxapi.registerApp(ConstantField.WX_APPID);
    }

    public void startPay(WXOrderInfo wxOrderInfo){
        if(wxOrderInfo==null){
            Toast.makeText(mContext,mContext.getString(R.string.e_no_order_msg),Toast.LENGTH_SHORT).show();
            return;
        }
        PayReq request = new PayReq();

        request.appId = wxOrderInfo.getAppid();

        request.partnerId = wxOrderInfo.getMch_id();//商户号

        request.prepayId= wxOrderInfo.getPrepay_id();//预支付交易会话ID

        request.packageValue ="Sign=WXPay";

        request.nonceStr=wxOrderInfo.getNonce_str();//随机字符串
        request.timeStamp= wxOrderInfo.getCreate_time();
        request.extData=wxOrderInfo.getOderEntry().getAllowTime()+"";
//        request.sign= wxOrderInfo.getSign();
        request.sign=genPayReq(request);
        String A="appid="+request.appId+"&partnerId="+request.partnerId+
                "&prepayId="+request.prepayId+"&packageValue="+request.packageValue+"&nonce_str="+request.nonceStr
                +"&timeStamp="+request.timeStamp+"&服务端sign="+wxOrderInfo.getSign()+"&app端sign="+genPayReq(request);
        L.v(TAG,A);
        iwxapi.sendReq(request);
    }

    /**
     * 获取sign签名
     *
     * @return
     */
    private String genPayReq(PayReq request) {
        // 把参数的值传进去SortedMap集合里面
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", request.appId);
        parameters.put("noncestr", request.nonceStr);
        parameters.put("package", request.packageValue);
        parameters.put("partnerid", request.partnerId);
        parameters.put("prepayid", request.prepayId);
        parameters.put("timestamp", request.timeStamp);
        String characterEncoding = "UTF-8";
        String mySign = createSign(characterEncoding, parameters);
        System.out.println("我的签名是：" + mySign);
        return mySign;
    }

    /**
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding,
                                    SortedMap<Object, Object> parameters) {

        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + ConstantField.WX_SECRECT); //KEY是商户秘钥
        String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding)
                .toUpperCase();
        return sign; // D3A5D13E7838E1D453F4F2EA526C4766
        // D3A5D13E7838E1D453F4F2EA526C4766
    }
}
