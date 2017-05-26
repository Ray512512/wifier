package com.traffic.wifiapp.bean.response;

import com.traffic.wifiapp.bean.entry.OderEntry;

/**
 * Created by ray on 2017/5/24.
 */

public class WXOrderInfo {

    /**
     * appid : wx49b05ad7241e53bf
     * mch_id : 1475324602
     * nonce_str : KtcPB5I8mugcZ0QV
     * prepay_id : wx201705250850186af0de93000173863621
     * result_code : SUCCESS
     * return_code : SUCCESS
     * return_msg : OK
     * sign : D4EA961EF0D4F6E5FBB148DF27516625
     * trade_type : APP
     */

    private String appid;
    private String mch_id;
    private String nonce_str;
    private String prepay_id;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String trade_type;

    /**
     * log_id : 697
     */

    private int log_id;
    /**
     * create_time : 1495677518
     */

    private String create_time;

    private OderEntry oderEntry;

    public OderEntry getOderEntry() {
        return oderEntry;
    }

    public void setOderEntry(OderEntry oderEntry) {
        this.oderEntry = oderEntry;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
