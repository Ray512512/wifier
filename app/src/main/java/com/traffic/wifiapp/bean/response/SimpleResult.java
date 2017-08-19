package com.traffic.wifiapp.bean.response;

/**
 * Created by ray on 2017/8/16.
 * emial:1452011874@qq.com
 */

public class SimpleResult {

    /**
     * isSuccess : true
     * msg : 申请成功，请等待审核！
     */

    private boolean isSuccess;
    private String msg;

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
