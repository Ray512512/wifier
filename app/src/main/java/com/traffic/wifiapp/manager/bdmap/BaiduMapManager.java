package com.traffic.wifiapp.manager.bdmap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.ui.view.MapItemView;
import com.traffic.wifiapp.utils.AlertDialogUtil;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.SPUtils;

import java.util.ArrayList;

/**
 * Created by Ray on 2017/5/8.
 * email：1452011874@qq.com
 */

public class BaiduMapManager {
    public static final String ADDR_TAG="addR";
    public static final String BAIDU_CODE="bd09ll";

    private BaiduMap mBaiduMap;
    private Context mContext;
    private BDLocation myLocation;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isShowMyLoc=false;

    //方向传感器
    private MyOrientationListener myOrientationListener;


    public BaiduMapManager(Context mContext,BaiduMap mBaiduMap ) {
        this.mBaiduMap = mBaiduMap;
        this.mContext = mContext;
    }

    /**
     * 初始化定位相关参数
     * */
    public void initLocation(){
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setCoorType(BAIDU_CODE);
        //可选，默认gcj02，设置返回的定位结果坐标系
//        int span=10*1000;
//        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        mLocationClient.setLocOption(option);
        //声明LocationClient类
        myLocationListener=new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //注册监听函数
        mLocationClient.start();

        if(SPUtils.getObject(ConstantField.CURRENT_LOCATION)!=null){
            myLocation=SPUtils.getObject(ConstantField.CURRENT_LOCATION);
            showMyLocation();
        }

        mBaiduMap.setOnMarkerClickListener((Marker marker) -> {
            Bundle bundle=marker.getExtraInfo();
            if(bundle==null)return true;
            String add=bundle.getString(ADDR_TAG);
            if(!TextUtils.isEmpty(add))
                AlertDialogUtil.AlertDialog(mContext,String .format(mContext.getString(R.string.addr_detail),add),mContext.getString(R.string.tag_sure));
            return false;
        });
//        initOrientation();
    }

    /**
     * 初始化传感器
     * */
    private void initOrientation(){
        myOrientationListener = new MyOrientationListener(mContext);
        myOrientationListener.setmOnOrientationListener(x -> showNewLocation(myLocation,x));
        //开启定位的允许
        myOrientationListener.star();
    }

    public void onStop(){
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //停止方向传感器
//        myOrientationListener.stop();
    }

    public void onResume(){
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }


    /**
     * 显示wifi信息
     * */
    public void showWifiPoint(ArrayList<WifiProvider> wifiProviders){
        try {
        for (WifiProvider w:wifiProviders){
            BitmapDescriptor bitmap=BitmapDescriptorFactory.fromView(new MapItemView(mContext,w));
            Bundle bundle = new Bundle();
            bundle.putString(ADDR_TAG,w.getAddr());
            addOverView(new LatLng(w.getLat(),w.getLng()),bundle,bitmap);
        }
        }catch (Exception e){
            L.e(e.toString());
        }
    }

    /**
     * 展示我的位置，并且将地图中心定位到当前位置
     * */
    public void showMyLocation(){
        if(myLocation==null||isShowMyLoc)return;
        LatLng cenpt=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        showCenterPoint(cenpt);
        BitmapDescriptor bitmap ;
        bitmap= BitmapDescriptorFactory.fromResource(R.mipmap.ic_myloc);
        addOverView(cenpt,null,bitmap);
        isShowMyLoc=true;
        AppManager.getInstance(mContext).getMainActivity().getPresenter().getWifiFragment().refreshWifiList();
    }

    /**
     * 展示我的位置
     * */
    public void showMyLoc(){
        if(myLocation==null)return;
        LatLng cenpt=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        showCenterPoint(cenpt);
    }

    /**
     * 添加地图覆盖物
     * */
    private void addOverView(LatLng l,Bundle data,BitmapDescriptor bitmap){
        OverlayOptions option = new MarkerOptions()
                .position(l)
                .icon(bitmap)
                .extraInfo(data);
        mBaiduMap.addOverlay(option);

    }

    /**
     * 将指定坐标显示在中心
     * */
    private void showCenterPoint(LatLng latLng){
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(18)
                .build();
//定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//改变地图状态
//        mBaiduMap.setMapStatus(mMapStatusUpdate);
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
    }
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            myLocation=location;
            showMyLocation();
//            showNewLocation(location);
            WifiApplication.getInstance().getUser().setLocationInfo(myLocation);
        }
    }

    /**
     * 位置发生改变
     * */
    private void showNewLocation(BDLocation location,float x){
        if(location==null)return;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
// 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(x)
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
// 设置定位数据
// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.wifi);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
        mBaiduMap.setMyLocationData(locData);
// 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }


}
