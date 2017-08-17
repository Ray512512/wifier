package com.traffic.wifiapp.retrofit;


import com.lljjcoder.citypickerview.model.ProvinceModel;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.entry.AddShop;
import com.traffic.wifiapp.bean.entry.FeedBack;
import com.traffic.wifiapp.bean.entry.MacStr;
import com.traffic.wifiapp.bean.entry.OderEntry;
import com.traffic.wifiapp.bean.response.Goods;
import com.traffic.wifiapp.bean.response.Message;
import com.traffic.wifiapp.bean.response.SimpleResult;
import com.traffic.wifiapp.bean.response.SlideImageUrls;
import com.traffic.wifiapp.bean.response.WXOrderInfo;
import com.traffic.wifiapp.bean.response.WifiProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by ray on 17-5-17
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

    @POST("wap/mall/orderwifi.html")
    Observable<BaseModel<WXOrderInfo>> getOrderInfo(@Body OderEntry o);

    @POST("wap/mall/getorderwifi.html")
    Observable<BaseModel<Object>> queryPayResult(@Body HashMap<String, String> map);

    @GET
    Observable<String >openWifiByIp(@Url String url);

    @GET("wap/news/article.html")
    Observable<BaseModel<ArrayList<Goods>>> getShowGoods(@Query("shop_id") String shop_id);

    @GET("backstage/adsite/citywifiall.html")
    Observable<BaseModel<ArrayList<ProvinceModel>>> getAddrList();

    @Multipart
    @POST("backstage/adsite/upinfowifise.html")
    Observable<BaseModel<SimpleResult>> saveUserInfo(@PartMap Map<String, RequestBody> params);

    @POST("backstage/adsite/shopapplywifi.html")
    Observable<BaseModel<SimpleResult>> addShop(@Body AddShop addShop);

    @POST("backstage/adsite/reportwifi.html")
    Observable<BaseModel<SimpleResult>> feedBack(@Body FeedBack feedBack);

    @GET("backstage/adsite/infolistwifi.html")
    Observable<BaseModel<ArrayList<Message>>> getMessageList();

}





