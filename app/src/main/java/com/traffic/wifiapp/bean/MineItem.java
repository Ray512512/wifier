package com.traffic.wifiapp.bean;

/**
 * Created by ray on 2017/6/7.
 * emial:1452011874@qq.com
 */

public class MineItem {
    private int imgRes;
    private int title;

    public MineItem(int title,int imgRes) {
        this.imgRes = imgRes;
        this.title = title;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
