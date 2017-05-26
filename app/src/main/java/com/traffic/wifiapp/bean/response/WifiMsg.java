package com.traffic.wifiapp.bean.response;

/**
 * Created by Ray on 2017/5/11.
 * email：1452011874@qq.com
 */

public class WifiMsg {
    private long id;
    private String name;
    private String status;

    public int getBgurl() {
        return bgurl;
    }

    public void setBgurl(int bgurl) {
        this.bgurl = bgurl;
    }

    private int bgurl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
