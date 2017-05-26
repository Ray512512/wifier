package com.traffic.wifiapp.bean.entry;

import com.traffic.wifiapp.utils.StringUtil;

import java.util.List;

/**
 * Created by Ray on 2017/5/12.
 * email：1452011874@qq.com
 */

public class MacStr {

    public MacStr(List<String> list) {
        this.macstr = StringUtil.listToString(list,",");
    }

    private String macstr;

    public String getMacstr() {
        return macstr;
    }

    public void setMacstr(String macstr) {
        this.macstr = macstr;
    }
}
