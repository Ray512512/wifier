package com.traffic.wifiapp.adatper;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.adapter.RecyleAdapter;
import com.traffic.wifiapp.common.adapter.base.BaseAdapterHelper;
import com.traffic.wifiapp.utils.LocationUtils;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.webclient.WebViewActivity;

import static com.traffic.wifiapp.bean.response.WifiProvider.AUTH_NO;
import static com.traffic.wifiapp.common.ConstantField.USER;


/**
 * Created by ray on 2017/5/8.
 */

public class WifiAdapter extends RecyleAdapter<WifiProvider> {

    public WifiAdapter(Context context) {
        super(context, R.layout.wifi_item);
    }

    private WifiProvider contectWifi;
    public void setConnectWifiState(WifiProvider w){
        contectWifi=w;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, WifiProvider item, int position) {
        helper.setImageByUrl(R.id.wifi_item_img_bg,item.getPhoto(),R.color.bgGreen);
        helper.setText(R.id.item_wifi_tv_title,item.getShopname());
        User u= SPUtils.getObject(USER);
        if(contectWifi!=null&&contectWifi.getBSSID().equals(item.getBSSID())){
        helper.setText(R.id.item_wifi_tv_state, "已连接");
        }else {
        LatLng latLng=null;
        if(u!=null)
        latLng=new LatLng(u.getLat(),u.getLng());
        helper.setText(R.id.item_wifi_tv_state,
                LocationUtils.getDistance(new LatLng(item.getLat(),item.getLng()),latLng));
        }
        switch (item.getLevel()){
            case 0:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_0);
                break;
            case 1:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_1);
                break;
            case 2:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_2);
                break;
            case 3:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_3);
                break;
            case 4:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_4);
                break;
            default:
                helper.setImageResource(R.id.item_wifi_img_wifi,R.mipmap.wifi_4);
        }

        if(item.getRz()==AUTH_NO){
            helper.setVisible(R.id.wifi_item_auth, View.INVISIBLE);
        }else {
            helper.setVisible(R.id.wifi_item_auth, View.VISIBLE);
        }

        helper.setOnClickListener(R.id.wifi_item_tv_connect, v -> {
            Intent intent=new Intent(context, WebViewActivity.class);
            intent.putExtra("url",item.getWl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

}
