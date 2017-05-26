package com.traffic.wifiapp.bean.response;

/**
 * Created by Ray on 2017/5/12.
 * email：1452011874@qq.com
 */

public class Goods {
    private String  shop_id;
    private String  title;
    private String  price;//是空或者等于0 是空或者等于0
    private String  cnt;// 商品简介
    private String photo;//商品图片
    private String photolb;//轮播图

    public String getPhotolb() {
        return photolb;
    }

    public void setPhotolb(String photolb) {
        this.photolb = photolb;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
