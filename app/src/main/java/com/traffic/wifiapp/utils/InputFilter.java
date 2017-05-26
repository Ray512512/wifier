package com.traffic.wifiapp.utils;

import android.widget.EditText;

/**
 * Created by ray on 2017/5/25.
 * emial:1452011874@qq.com
 */

public class InputFilter {
    public static void setMoneyFilter(EditText editText){
        editText.setFilters(new android.text.InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if(source.equals(".") && dest.toString().length() == 0){
                return "0.";
            }
            if(source.equals("0") && dest.toString().length() == 1 && dest.charAt(0)=='0'){
                return "";
            }
            if(dest.toString().contains(".")){
                int index = dest.toString().indexOf(".");
                int mlength = dest.toString().substring(index).length();
                if(mlength == 3){
                    return "";
                }
            }
            return null;
        }});
    }
}
