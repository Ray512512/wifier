package com.traffic.wifiapp.retrofit;


import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.entry.MacStr;
import com.traffic.wifiapp.bean.entry.OderEntry;
import com.traffic.wifiapp.bean.response.Goods;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WXOrderInfo;
import com.traffic.wifiapp.bean.response.WifiProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Jam on 16-5-17
 * Description:
 */
public interface ApiService {

    @POST("backstage/adsite/registerWifi.html")
    Observable<BaseModel<User>> registerAndLogin(@Body com.traffic.wifiapp.bean.entry.RegistEntry registEntry);

    @GET("backstage/shop/getallmacs.html")
    Observable<BaseModel<List<String>>> getAllMacList();

    @POST("backstage/shop/getalluserinfo.html")
    Observable<BaseModel<ArrayList<WifiProvider>>> getUserInfoByMac(@Body MacStr list);


    @GET("backstage/adsite/getwifilb.html")
    Observable<BaseModel<List<SlideImageUrls>>> getHomePageSlide();


    @GET("backstage/adsite/getnearlb.html")
    Observable<BaseModel<List<SlideImageUrls>>> getMapPageSlide();


   /* @GET("backstage/adsite/getrewardlb.html?{shop_id}")
    Observable<BaseModel<List<SlideImageUrls>>> getMoneyPageSlide(@Path("shop_id")String shop_id);//// TODO: 2017/5/24  换地址
*/
    @POST("wap/mall/orderwifi.html")
    Observable<BaseModel<WXOrderInfo>> getOrderInfo(@Body OderEntry o);

    @POST("wap/mall/getorderwifi.html")
    Observable<BaseModel<Object>> queryPayResult(@Body HashMap<String, String> map);

    @GET
    Observable<Object> openWifiByIp(@Url String url);

    @GET("wap/news/article.html")
    Observable<BaseModel<ArrayList<Goods>>> getShowGoods(@Query("shop_id") String shop_id);
}





