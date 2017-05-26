package com.traffic.wifiapp.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ray on 2017/5/11.
 * email：1452011874@qq.com
 */

public class WifiProvider implements Parcelable {
    public static final int AUTH_NO=0;//未认证
    public static final int AUTH_YES=1;//已认证


    public static final int TYPE_SHOPER_FREE=94;//免费商家
    public static final int TYPE_SHOPER_PAY=95;//收费商家
    public static final int TYPE_SINGLE_FREE=96;//免费私人
    public static final int TYPE_SINGLE_PAY=97;//收费私人
    /**
     * user_id : 58
     * mac : 113:224:44
     * logo : http://wififan.zhikenet.com/attachs/2017/05/09/thumb_59111e6838c94.png
     * photo : http://wififan.zhikenet.com/attachs/2017/05/09/thumb_59111e6f2b2c4.jpg
     * mobile : 13782644211
     * lng : 113.285284
     * lat : 35.182255
     * type : 94
     */

    private String user_id;
    private String mac;
    private String BSSID;
    private String SSID;
    private String logo;
    private String photo;
    private String mobile;
    private double lng;
    private double lat;
    private int level;//wifi 信号强度 0-4
    private String addr;
    private String ipAddr;
    private String shop_id;

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int type;//商户类型 94 免费商家 95收费商家 96免费私人用户 97收费私人用户


    /**
     * shopname : 国美电器
     */
    private String shopname;
    /**
     * rz : 1
     * wl : http://meipin365.cn/wap/
     */



    private int rz;// 代表认证 0-否 1-是
    private String wl;//wl 是外链地址

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

//    public String getUser_id() {
//        return user_id;
//    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public WifiProvider() {
    }

    public int getRz() {
        return rz;
    }

    public void setRz(int rz) {
        this.rz = rz;
    }

    public String getWl() {
        return wl;
    }

    public void setWl(String wl) {
        this.wl = wl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.mac);
        dest.writeString(this.BSSID);
        dest.writeString(this.SSID);
        dest.writeString(this.logo);
        dest.writeString(this.photo);
        dest.writeString(this.mobile);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeInt(this.level);
        dest.writeString(this.addr);
        dest.writeString(this.ipAddr);
        dest.writeString(this.shop_id);
        dest.writeInt(this.type);
        dest.writeString(this.shopname);
        dest.writeInt(this.rz);
        dest.writeString(this.wl);
    }

    protected WifiProvider(Parcel in) {
        this.user_id = in.readString();
        this.mac = in.readString();
        this.BSSID = in.readString();
        this.SSID = in.readString();
        this.logo = in.readString();
        this.photo = in.readString();
        this.mobile = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.level = in.readInt();
        this.addr = in.readString();
        this.ipAddr = in.readString();
        this.shop_id = in.readString();
        this.type = in.readInt();
        this.shopname = in.readString();
        this.rz = in.readInt();
        this.wl = in.readString();
    }

    public static final Creator<WifiProvider> CREATOR = new Creator<WifiProvider>() {
        @Override
        public WifiProvider createFromParcel(Parcel source) {
            return new WifiProvider(source);
        }

        @Override
        public WifiProvider[] newArray(int size) {
            return new WifiProvider[size];
        }
    };
}
