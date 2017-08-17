package com.traffic.wifiapp.bean.response;

/**
 * Created by ray on 2017/8/16.
 * emial:1452011874@qq.com
 */

public class Message {

    /**
     * unread : 1
     * link_url :
     * intro : 可购在线
     * title : 关于可购
     * details : 你好，这个是一个测试
     */

    private int unread;
    private String link_url;
    private String intro;
    private String title;
    private String details;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
