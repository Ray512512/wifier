package com.traffic.wifiapp.utils;

import java.io.Serializable;

/**
 * Created by caism on 2017/4/16.
 */

public class ResultBean implements Serializable{
    private String name;
    private String macl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacl() {
        return macl;
    }

    public void setMacl(String macl) {
        this.macl = macl;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "name='" + name + '\'' +
                ", macl='" + macl + '\'' +
                '}';
    }
}
