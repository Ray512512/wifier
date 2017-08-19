package com.traffic.wifiapp.bean.entry;

/**
 * Created by ray on 2017/8/17.
 * emial:1452011874@qq.com
 */

public class UpdateInfo {


    /**
     * nickname : ray
     * uid : 617
     * provinceid : 612
     * cityid : 612
     */

    private String nickname;
    private String uid;
    private String provinceid;
    private String cityid;

    public UpdateInfo(String nickname, String uid, String provinceid, String cityid) {
        this.nickname = nickname;
        this.uid = uid;
        this.provinceid = provinceid;
        this.cityid = cityid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }
}
