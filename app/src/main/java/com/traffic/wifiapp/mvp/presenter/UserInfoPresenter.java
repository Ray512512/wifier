package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.text.TextUtils;

import com.lljjcoder.citypickerview.model.ProvinceModel;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.response.SimpleResult;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.login.LoginActivity;
import com.traffic.wifiapp.manager.bdmap.BaiduMapManager;
import com.traffic.wifiapp.mvp.view.UserInfoIView;
import com.traffic.wifiapp.retrofit.ApiManager;
import com.traffic.wifiapp.retrofit.CacheManager;
import com.traffic.wifiapp.rxjava.RxHelper;
import com.traffic.wifiapp.rxjava.RxRetrofitCache;
import com.traffic.wifiapp.rxjava.RxSubscribe;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.FileUtils;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.SPUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * Created by xy on 16/5/16.
 */
public class UserInfoPresenter extends BasePresenter<UserInfoIView> {
    public static final int REQUEST_CODE_CHOOSE = 1;
    private static final java.lang.String TAG = "UserInfoPresenter";

    public UserInfoPresenter(Activity mContext, UserInfoIView mView) {
        super(mContext, mView);
        initAddrSelect();
    }

    private CityPicker mCityPicker;
    private String curentProvince, currentCity;

    //获取省市数据
    private void getAddr() {
        RxRetrofitCache.load(mContext, ConstantField.ADDRESS_KEY, CacheManager.ADDRESS_CACHE_TIME, ApiManager.mApiService.getAddrList(), false)
                .compose(RxHelper.handleResult()).subscribe(new RxSubscribe<ArrayList<ProvinceModel>>(mContext, R.string.mine_info_get_addressing) {
            @Override
            protected void _onNext(ArrayList<ProvinceModel> addresses) {
                initAddrSelect();
            }

            @Override
            protected void _onError(String message) {

            }
        });
    }


    public void saveInfo(String avatarPath, String userName) {
//        String ids[]=getProvinceId(curentProvince,currentCity);
        Map<String, RequestBody> map = new HashMap<>();
        try {
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), WifiApplication.getInstance().getUser().getUser_id());
            RequestBody nickName = RequestBody.create(MediaType.parse("text/plain"), userName);
            RequestBody provinceId = RequestBody.create(MediaType.parse("text/plain"), curentProvince);
            RequestBody cityId = RequestBody.create(MediaType.parse("text/plain"), currentCity);

            File file = FileUtils.getCompressFile(avatarPath);
            L.v("saveInfo", file.length() + "");
            RequestBody fileAvatar = RequestBody.create(MediaType.parse("image/*"), file);

            map.put("uid", userId);
            map.put("nickname", nickName);
            map.put("provinceid", provinceId);
            map.put("cityid", cityId);
            map.put("uploadfile[]\"; filename=\"" + file.getName(), fileAvatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiManager.mApiService.saveUserInfo(map).compose(RxHelper.handleResult()).subscribe(
                new RxSubscribe<SimpleResult>(mContext, R.string.mine_info_saving) {
                    @Override
                    protected void _onNext(SimpleResult o) {
                        if (o.isIsSuccess()) {
                            showToast(mContext.getString(R.string.msg_save_info_success));
                            mView.saveInfoSuccess();
                            saveUserInfo(userName, o.getMsg());
                            //// TODO: 2017/8/17  保存信息
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        showToast(message);
                    }
                });
    }


    private void saveUserInfo(String nickname, String avatarPath) {
        User u = WifiApplication.getInstance().getUser();
        u.setNickname(nickname);
        u.setFace(avatarPath);
        u.setProvinceid(curentProvince);
        u.setCityid(currentCity);
        u.setProvinceName(curentProvince);
        u.setCityName(currentCity);
        SPUtils.setObject(ConstantField.USER, u);
    }


    private void initAddrSelect() {
        User user = WifiApplication.getInstance().getUser();
        if (!TextUtils.isEmpty(user.getProvinceid()) && !user.getProvinceid().equals("0") &&
                !TextUtils.isEmpty(user.getCityid()) && !user.getCityid().equals("0")) {
            curentProvince = user.getProvinceid();
            currentCity = user.getCityid();
        } else {
            curentProvince = BaiduMapManager.myLocation == null ? "" : BaiduMapManager.myLocation.getProvince();
            currentCity = BaiduMapManager.myLocation == null ? "" : BaiduMapManager.myLocation.getCity();
        }
        mView.refreshAddr(curentProvince + "-" + currentCity);

        mCityPicker = new CityPicker.Builder(mContext)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .backgroundPop(0x50000000)
                .province(curentProvince)
                .city(currentCity)
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(true)
                .build();

        mCityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                curentProvince = citySelected[0];
                //城市
                currentCity = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
//                String district = citySelected[2];
                //邮编
//                String code = citySelected[3];
                mView.refreshAddr(curentProvince + "-" + currentCity);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void loginOut() {
        SPUtils.put(ConstantField.USER, null);
        SPUtils.put(ConstantField.USER_NAME, null);
        AppManager.getInstance(mContext).killAllActivity();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

    public void chooseImage() {
        Matisse.from(mContext)
                .choose(MimeType.of(MimeType.PNG, MimeType.JPEG)) // 选择 mime 的类型
                .countable(false)
                .maxSelectable(1) // 图片选择的最多数量
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f) // 缩略图的比例
                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
    }

    public void showAddrChoose() {
        if (mCityPicker == null) {
            showToast(mContext.getString(R.string.mine_info_no_addr));
        } else {
            mCityPicker.show();
        }
    }

/*
    private  String [] getProvinceId(String provinceName,String cityName){
        String [] r=new String[2];
         if(mData!=null&& !TextUtils.isEmpty(provinceName))
             for (int i = 0; i <mData.size() ; i++) {
                 ProvinceModel p=mData.get(i);
                 if(p.getName().equals(provinceName)){
                     r[0]=p.getId();
                     for (int j = 0; j <p.getList().size() ; j++) {
                         CityModel c=p.getList().get(i);
                         if(c.getName().equals(cityName)){
                             r[1]=c.getId();
                             break;
                         }
                     }
                     break;
                 }
             }
        L.v(TAG,"省id:"+r[0]+"\t市id:"+r[1]);
        return r;
    }

    private  String [] getAddrName(String provinceId,String cityId){
        String [] r=new String[2];
        if(mData!=null&& !TextUtils.isEmpty(provinceId))
            for (int i = 0; i <mData.size() ; i++) {
                ProvinceModel p=mData.get(i);
                if(p.getId().equals(provinceId)){
                    r[0]=p.getName();
                    for (int j = 0; j <p.getList().size() ; j++) {
                        CityModel c=p.getList().get(i);
                        if(c.getId().equals(cityId)){
                            r[1]=c.getName();
                            break;
                        }
                    }
                    break;
                }
            }
        L.v(TAG,"当前省:"+r[0]+"\t当前市:"+r[1]);
        return r;
    }*/
}
