package com.traffic.wifiapp.bean.entry;

import com.traffic.wifiapp.common.WifiApplication;

/**
 * Created by ray on 2017/8/16.
 * emial:1452011874@qq.com
 */

public class AddShop {
    private String contact;
    private String user_id ;
    private String mobile ;
    private String shop_name ;
    private String addr  ;

    public AddShop(String contact, String mobile, String shop_name, String addr) {
        this.user_id= WifiApplication.getInstance().getUser().getUser_id();
        this.contact = contact;
        this.mobile = mobile;
        this.shop_name = shop_name;
        this.addr = addr;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
