package com.traffic.wifiapp.bean.entry;

/**
 * Created by ray on 2017/5/26.
 * emial:1452011874@qq.com
 */

public class QueryPayResultEnter {

    public QueryPayResultEnter(String user_id, String log_id) {
        this.user_id = user_id;
        this.log_id = log_id;
    }

    /**
     * user_id : 617
     * log_id : 781
     */

    private String user_id;
    private String log_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
}
