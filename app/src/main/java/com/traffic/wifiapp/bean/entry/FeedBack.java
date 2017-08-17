package com.traffic.wifiapp.bean.entry;

import com.traffic.wifiapp.common.WifiApplication;

/**
 * Created by ray on 2017/8/16.
 * emial:1452011874@qq.com
 */

public class FeedBack {
    private String title;
    private String content ;
    private String user_id  ;

    public FeedBack(String title, String content) {
        this.title = title;
        this.content = content;
        this.user_id= WifiApplication.getInstance().getUser().getUser_id();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
