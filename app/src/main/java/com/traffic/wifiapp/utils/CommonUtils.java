package com.traffic.wifiapp.utils;

import java.util.regex.Pattern;

/**
 * Created by Ray on 2017/5/5.
 * email：1452011874@qq.com
 */

public class CommonUtils {

    private static final String REGEX_MOBILE = "(^[1]([3|5|8|7|4][0-9]{1})[ ]{0,8}[0-9]{4}[ ]{0,8}[0-9]{4})|(^[1]([3|5|8|7|4][0-9]{2})[ ]{0,8}[0-9]{4}[ ]{0,8}[0-9]{3})";
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

}
