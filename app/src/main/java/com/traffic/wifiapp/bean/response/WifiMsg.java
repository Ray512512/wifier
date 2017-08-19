package com.traffic.wifiapp.bean.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ray on 2017/5/11.
 * email：1452011874@qq.com
 */

public class WifiMsg implements Parcelable{
    private long id;
    private String name;
    private String status;

    public int getBgurl() {
        return bgurl;
    }

    public void setBgurl(int bgurl) {
        this.bgurl = bgurl;
    }

    private int bgurl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.status);
        dest.writeInt(this.bgurl);
    }

    public WifiMsg() {
    }

    protected WifiMsg(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.status = in.readString();
        this.bgurl = in.readInt();
    }

    public static final Creator<WifiMsg> CREATOR = new Creator<WifiMsg>() {
        @Override
        public WifiMsg createFromParcel(Parcel source) {
            return new WifiMsg(source);
        }

        @Override
        public WifiMsg[] newArray(int size) {
            return new WifiMsg[size];
        }
    };
}
