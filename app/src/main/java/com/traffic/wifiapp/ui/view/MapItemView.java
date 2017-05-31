package com.traffic.wifiapp.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.utils.AlertDialogUtil;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.LocationUtils;
import com.traffic.wifiapp.utils.SPUtils;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_PAY;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_PAY;
import static com.traffic.wifiapp.common.ConstantField.USER;


public class MapItemView extends FrameLayout {
    ImageView viewRect;
    TextView tv_name,tv_dis;
    private LinearLayout layout;

    private WifiProvider mWifiProvider;

    Context mContext;
    OnClickListener l = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_rect:
                    if(mWifiProvider!=null&& !TextUtils.isEmpty(mWifiProvider.getAddr()))
                    AlertDialogUtil.AlertDialog(mContext,"详细地址："+mWifiProvider.getAddr(),"确定");
                   else if(mWifiProvider!=null)
                    switch (mWifiProvider.getType()){
                        case TYPE_SHOPER_FREE:
                        case TYPE_SINGLE_FREE:
                            AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,mWifiProvider);
                            break;
                        case TYPE_SHOPER_PAY:
                        case TYPE_SINGLE_PAY:
                            AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,mWifiProvider);
                            break;
                        default:
                            AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_NORMAL,mWifiProvider);
                    }else {
                       AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_NORMAL,mWifiProvider);
                   }
            }
        }
    };


    public MapItemView(Context context, WifiProvider wifiProvider) {
        super(context);
        mWifiProvider=wifiProvider;
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.map_ietm, this);
        layout = (LinearLayout) findViewById(R.id.view_rect);
        viewRect = (ImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_dis = (TextView) findViewById(R.id.tv_dis);
        layout.setOnClickListener(l);
        Glide.with(mContext).load(wifiProvider.getLogo()).placeholder(R.mipmap.ic_photo)
                .bitmapTransform(new CropCircleTransformation(mContext)).into(viewRect);
        tv_name.setText(wifiProvider.getShopname());
        User u=SPUtils.getObject(USER);
        if(u!=null){
        LatLng latLng= new LatLng(u.getLat(),u.getLng());
        tv_dis.setText(LocationUtils.getDistance(new LatLng(wifiProvider.getLat(),wifiProvider.getLng()),latLng));
        }
    }

}
