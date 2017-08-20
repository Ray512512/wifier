package com.traffic.wifiapp.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ray on 2017/5/7.
 * email：1452011874@qq.com
 */
public class ApiManager {
    public static final String  ROOT_URL="http://wififan.zhikenet.com/";
    public static ApiService mApiService;

    //后台请求的方法名枚举，后续根据需要调用的方法来增加
    public enum Method {
        Login
    }
    static  {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mApiService=new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

}
