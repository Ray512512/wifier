package com.traffic.wifiapp.bean.entry;

import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.utils.NetworkTools;

/**
 * Created by ray on 2017/5/24.
 */

public class OderEntry {
    private String total_price;
    private String user_id;
    private String ip;
    private String message;
    private String shop_id;
    private WifiProvider provider;
    private long allowTime;

    @Override
    public String toString() {
        return "total_price"+total_price+"\t"+
                "user_id"+user_id+"\t"+
                "ip"+ip+"\t"+
                "message"+message+"\t"+
                "shop_id"+shop_id+"\t"+
                "allowTime"+allowTime+"\t"
                ;
    }

    public OderEntry(String shopId, String total_price, String message) {
        this.total_price = total_price;
        this.shop_id=shopId;
        this.message = message;
        this.ip= NetworkTools.getLocalIpAddr(WifiApplication.getInstance());
        this.user_id=WifiApplication.getInstance().getUser().getUser_id();
    }

    public long getAllowTime() {
        return allowTime;
    }

    public void setAllowTime(long allowTime) {
        this.allowTime = allowTime;
    }

    public WifiProvider getProvider() {
        return provider;
    }

    public void setProvider(WifiProvider provider) {
        this.provider = provider;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
