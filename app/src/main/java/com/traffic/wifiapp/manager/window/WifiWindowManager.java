package com.traffic.wifiapp.manager.window;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.manager.WifiAdmin;
import com.traffic.wifiapp.utils.AnimaUtil;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.LocationUtils;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.utils.SystemUtil;
import com.traffic.wifiapp.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_PAY;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_FREE;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_PAY;
import static com.traffic.wifiapp.common.ConstantField.USER;
import static com.traffic.wifiapp.manager.window.WindowUtils.getXinHaoStr;
import static com.traffic.wifiapp.manager.window.WindowUtils.gotoApp;

/**
 * Created by ray on 2017/5/24.
 */

public class WifiWindowManager implements View.OnClickListener {
    private final String TAG = "WifiWindowManager";
    public static final String RECIVER_ACTION = "wifiListChange";//注册的wifi监听广播
    @Bind(R.id.img_center)
    ImageView imgCenter;
    @Bind(R.id.window_recyclerview)
    View windowRecyclerview;
    @Bind(R.id.img_setting)
    ImageView imgSetting;
    @Bind(R.id.window_item1_imgphoto)
    ImageView windowItem1Imgphoto;
    @Bind(R.id.window_item1_tvname)
    TextView windowItem1Tvname;
    @Bind(R.id.window_item1_photo)
    LinearLayout windowItem1Photo;
    @Bind(R.id.window_tv_loc1)
    TextView windowTvLoc1;
    @Bind(R.id.window_item_xinhao1)
    TextView windowItemXinhao1;
    @Bind(R.id.window_item1_contect_btn1)
    Button windowItem1ContectBtn1;
    @Bind(R.id.window_item1_show)
    LinearLayout windowItem1Show;
    @Bind(R.id.window_item1_show_child)
    LinearLayout windowItem1ShowChild;
    @Bind(R.id.window_item1)
    RelativeLayout windowItem1;
    @Bind(R.id.window_item2_imgphoto)
    ImageView windowItem2Imgphoto;
    @Bind(R.id.window_item2_tvname)
    TextView windowItem2Tvname;
    @Bind(R.id.window_item2_photo)
    LinearLayout windowItem2Photo;
    @Bind(R.id.window_item1_contect_l2)
    LinearLayout windowItem1ContectL2;
    @Bind(R.id.window_tv_loc2)
    TextView windowTvLoc2;
    @Bind(R.id.window_item_xinhao2)
    TextView windowItemXinhao2;
    @Bind(R.id.window_item1_contect_btn2)
    Button windowItem1ContectBtn2;
    @Bind(R.id.window_item2_show)
    LinearLayout windowItem2Show;
    @Bind(R.id.window_item2_show_child)
    LinearLayout windowItem2ShowChild;
    @Bind(R.id.window_item2)
    RelativeLayout windowItem2;
    @Bind(R.id.window_item3_imgphoto)
    ImageView windowItem3Imgphoto;
    @Bind(R.id.window_item3_tvname)
    TextView windowItem3Tvname;
    @Bind(R.id.window_item3_photo)
    LinearLayout windowItem3Photo;
    @Bind(R.id.window_item1_contect_l3)
    LinearLayout windowItem1ContectL3;
    @Bind(R.id.window_tv_loc3)
    TextView windowTvLoc3;
    @Bind(R.id.window_item_xinhao3)
    TextView windowItemXinhao3;
    @Bind(R.id.window_item1_contect_btn3)
    Button windowItem1ContectBtn3;
    @Bind(R.id.window_item3_show)
    LinearLayout windowItem3Show;
    @Bind(R.id.window_item3_show_child)
    LinearLayout windowItem3ShowChild;
    @Bind(R.id.window_item3)
    RelativeLayout windowItem3;
    @Bind(R.id.window_main)
    RelativeLayout windowMain;
    @Bind(R.id.window_main_child)
    RelativeLayout windowMainChild;
    @Bind(R.id.percentTv)
    ImageView percentTv;
    @Bind(R.id.window_root)
    RelativeLayout windowRoot;

    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private View mWindowView;

    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;

    private Context mContext;
    private static WifiWindowManager windowManager;

    public static WifiWindowManager getIntance(Context context) {
        if (windowManager == null) {
            windowManager = new WifiWindowManager(context);
        }
        return windowManager;
    }

    private WifiWindowManager(Context context) {
        this.mContext = context;
    }

    public void init() {
        initWindowParams();
        initView();
        addWindowView2Window();
        initClick();
        registBordCast();
    }

    /**
     * 注册wifi列表变化的广播
     * */
    public void registBordCast(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(RECIVER_ACTION);
        mContext.registerReceiver(mRecaiver, filter);
    }

    /**
     * 初始化悬浮窗参数
     * */
    private void initWindowParams() {
        mWindowManager = (WindowManager) mContext.getSystemService(mContext.getApplicationContext().WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.TOP | Gravity.LEFT;
        wmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.x = SystemUtil.dip2px(mContext, 100);
        wmParams.y = SystemUtil.dip2px(mContext, 150);
    }

    /**
     * 初始化悬浮窗数据
     * */
    private void initView() {
        mWindowView = LayoutInflater.from(mContext).inflate(R.layout.layout_window, null);
        //初始化悬浮球
        windowItem1Tvname=ButterKnife.findById(mWindowView,R.id.window_item1_tvname);
        windowItem1Imgphoto=ButterKnife.findById(mWindowView,R.id.window_item1_imgphoto);
        imgSetting=ButterKnife.findById(mWindowView,R.id.img_setting);
        windowRecyclerview=ButterKnife.findById(mWindowView,R.id.window_recyclerview);
        imgCenter=ButterKnife.findById(mWindowView,R.id.img_center);
        percentTv=ButterKnife.findById(mWindowView,R.id.percentTv);
        windowRoot=ButterKnife.findById(mWindowView,R.id.window_root);
        windowMain=ButterKnife.findById(mWindowView,R.id.window_main);
        windowItem3=ButterKnife.findById(mWindowView,R.id.window_item3);
        windowItem3Show=ButterKnife.findById(mWindowView,R.id.window_item3_show);
        windowItem3ShowChild=ButterKnife.findById(mWindowView,R.id.window_item3_show_child);
        windowItem1ContectBtn3=ButterKnife.findById(mWindowView,R.id.window_item1_contect_btn3);
        windowItemXinhao3=ButterKnife.findById(mWindowView,R.id.window_item_xinhao3);
        windowTvLoc3=ButterKnife.findById(mWindowView,R.id.window_tv_loc3);
        windowItem1ContectL3=ButterKnife.findById(mWindowView,R.id.window_item1_contect_l3);
        windowItem3Photo=ButterKnife.findById(mWindowView,R.id.window_item3_photo);
        windowItem3Tvname=ButterKnife.findById(mWindowView,R.id.window_item3_tvname);
        windowItem3Imgphoto=ButterKnife.findById(mWindowView,R.id.window_item3_imgphoto);
        windowItem2=ButterKnife.findById(mWindowView,R.id.window_item2);
        windowItem2Show=ButterKnife.findById(mWindowView,R.id.window_item2_show);
        windowItem2ShowChild=ButterKnife.findById(mWindowView,R.id.window_item2_show_child);
        windowItem1ContectBtn2=ButterKnife.findById(mWindowView,R.id.window_item1_contect_btn2);
        windowItemXinhao2=ButterKnife.findById(mWindowView,R.id.window_item_xinhao2);
        windowTvLoc2=ButterKnife.findById(mWindowView,R.id.window_tv_loc2);
        windowItem1ContectL2=ButterKnife.findById(mWindowView,R.id.window_item1_contect_l2);
        windowItem2Photo=ButterKnife.findById(mWindowView,R.id.window_item2_photo);
        windowItem2Tvname=ButterKnife.findById(mWindowView,R.id.window_item2_tvname);
        windowItem2Imgphoto=ButterKnife.findById(mWindowView,R.id.window_item2_imgphoto);
        windowItem1=ButterKnife.findById(mWindowView,R.id.window_item1);
        windowItem1Show=ButterKnife.findById(mWindowView,R.id.window_item1_show);
        windowItem1ShowChild=ButterKnife.findById(mWindowView,R.id.window_item1_show_child);
        windowItem1ContectBtn1=ButterKnife.findById(mWindowView,R.id.window_item1_contect_btn1);
        windowItemXinhao1=ButterKnife.findById(mWindowView,R.id.window_item_xinhao1);
        windowTvLoc1=ButterKnife.findById(mWindowView,R.id.window_tv_loc1);
        windowItem1Photo=ButterKnife.findById(mWindowView,R.id.window_item1_photo);
        windowMainChild=ButterKnife.findById(mWindowView,R.id.window_main_child);
    }


    public void addWindowView2Window() {
        mWindowManager.addView(mWindowView, wmParams);
        mWindowManager.updateViewLayout(mWindowView, wmParams);
    }

    private void initClick() {
        windowItem1Photo.setOnClickListener(this);
        windowItem2Photo.setOnClickListener(this);
        windowItem3Photo.setOnClickListener(this);
        windowItem1ContectBtn1.setOnClickListener(this);
        windowItem1ContectBtn2.setOnClickListener(this);
        windowItem1ContectBtn3.setOnClickListener(this);
        imgCenter.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {     //双击事件
                gotoApp(mContext);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if(windowMain.getVisibility()==View.GONE)
                 AnimaUtil.showRainBow(windowMainChild,windowMain,AnimaUtil.VERTICAL);
                else
                AnimaUtil.goneRainBow(windowMainChild,windowMain,AnimaUtil.VERTICAL);
                return true;
            }
        });
        percentTv.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = (int) event.getRawX();
                    mStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mEndX = (int) event.getRawX();
                    mEndY = (int) event.getRawY();
                    if (needIntercept()) {
                        //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                        int mX=percentTv.getMeasuredWidth(),mY=percentTv.getMeasuredHeight();
                        L.v(TAG,"窗口大小：X->"+mX+"\tY->"+mY);
                        wmParams.x = (int) event.getRawX() - mX/2;
                        wmParams.y = (int) event.getRawY() - mY/2;
                        mWindowManager.updateViewLayout(mWindowView, wmParams);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mEndX = (int) event.getRawX();
                    mEndY = (int) event.getRawY();
                    if (needIntercept()) {
                        return true;
                    }
                    break;
            }
            return false;
        });
    }

    /**
     * 是否拦截
     *
     * @return true:拦截;false:不拦截.
     */
    private boolean needIntercept() {
        if (Math.abs(mStartX - mEndX) > 30 || Math.abs(mStartY - mEndY) > 30) {
            return true;
        }
        return false;
    }


    /**
     * 更新悬浮窗View
     * 数据源来自应用内
     * */
    private WifiProvider currentW;//当前连接的wifi
    private ArrayList<WifiProvider> ww;//当前搜索到的wifi列表

    public void getDataFromWifiFragment() {
        try {
            MainActivity activity = AppManager.getInstance(mContext).getMainActivity();
            windowItem1.setVisibility(View.GONE);
            windowItem2.setVisibility(View.GONE);
            windowItem3.setVisibility(View.GONE);
            setMainCOntentViewH(0);
            if (activity != null) {
                currentW = activity.getPresenter().getWifiFragment().getConnectWifi();//获取当前已连接wifi
                 ww = activity.getPresenter().getWifiFragment().getWifiList();//获取可用wifi列表
                if (ww != null) {
                    int size = ww.size();
                    for (int i = 0; i < size; i++) {
                        setItemData(i, ww.get(i));
                        if (i == 2) break;
                    }
                    switch (size) {
                        case 0:
                            break;
                        case 1:
                            windowItem1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            windowItem1.setVisibility(View.VISIBLE);
                            windowItem2.setVisibility(View.VISIBLE);
                            break;
                        default:
                            windowItem1.setVisibility(View.VISIBLE);
                            windowItem2.setVisibility(View.VISIBLE);
                            windowItem3.setVisibility(View.VISIBLE);
                            break;
                    }
                    setMainCOntentViewH(ViewUtils.getViewHeight(windowItem1)*size);
                }
            }
        } catch (Exception e) {
            L.e(TAG,e.toString());
        }
        wmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    /**
     * 重置wifi内容填充高度
     * */
    private void setMainCOntentViewH(int h){
        L.v(TAG,"重置内容高度："+h);
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(SystemUtil.dip2px(mContext,60),h);
        windowRecyclerview.setLayoutParams(params);
    }

    /**
     * 设置每个wifi数据
     * */
    private void setItemData(int pos, WifiProvider w) {
        if (w == null) return;
        LatLng latLng = null;
        User u = SPUtils.getObject(USER);
        if (u != null) latLng = new LatLng(u.getLat(), u.getLng());
        switch (pos) {
            case 0:
                Glide.with(mContext).load(w.getLogo()).placeholder(R.mipmap.ic_photo).
                        bitmapTransform(new CropCircleTransformation(mContext)).into(windowItem1Imgphoto);
                windowItem1Tvname.setText(w.getShopname());
                windowTvLoc1.setText(LocationUtils.getDistance(new LatLng(w.getLat(), w.getLng()), latLng));
                windowItemXinhao1.setText(getXinHaoStr(w.getLevel()));
                if (currentW != null && currentW.getBSSID().equals(w.getBSSID())) {
                    windowItem1ContectBtn1.setText("已连");
                } else {
                    windowItem1ContectBtn1.setText("连接");
                }
                break;
            case 1:
                Glide.with(mContext).load(w.getLogo()).placeholder(R.mipmap.ic_photo).
                        bitmapTransform(new CropCircleTransformation(mContext)).into(windowItem2Imgphoto);
                windowItem2Tvname.setText(w.getShopname());
                windowTvLoc2.setText(LocationUtils.getDistance(new LatLng(w.getLat(), w.getLng()), latLng));
                windowItemXinhao2.setText(getXinHaoStr(w.getLevel()));
                if (currentW != null && currentW.getBSSID().equals(w.getBSSID())) {
                    windowItem1ContectBtn2.setText("已连");
                } else {
                    windowItem1ContectBtn2.setText("连接");
                }
                break;
            case 2:
                Glide.with(mContext).load(w.getLogo()).placeholder(R.mipmap.ic_photo).
                        bitmapTransform(new CropCircleTransformation(mContext)).into(windowItem3Imgphoto);
                windowItem3Tvname.setText(w.getShopname());
                windowTvLoc3.setText(LocationUtils.getDistance(new LatLng(w.getLat(), w.getLng()), latLng));
                windowItemXinhao3.setText(getXinHaoStr(w.getLevel()));
                if (currentW != null && currentW.getBSSID().equals(w.getBSSID())) {
                    windowItem1ContectBtn3.setText("已连");
                } else {
                    windowItem1ContectBtn3.setText("连接");
                }
                break;
        }
    }


    public void onDestory() {
        if (mWindowView != null) {
            //移除悬浮窗口
            Log.i(TAG, "removeView");
            mWindowManager.removeView(mWindowView);
        }
        if(mRecaiver!=null)
            mContext.unregisterReceiver(mRecaiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.window_item1_photo:
                if(windowItem1Show.getVisibility()==View.GONE)
                AnimaUtil.showRainBow(windowItem1ShowChild,windowItem1Show,AnimaUtil.HORIZONTAL);
                else
                AnimaUtil.goneRainBow(windowItem1ShowChild,windowItem1Show,AnimaUtil.HORIZONTAL);
                break;
            case R.id.window_item2_photo:
                if(windowItem2Show.getVisibility()==View.GONE)
                    AnimaUtil.showRainBow(windowItem2ShowChild,windowItem2Show,AnimaUtil.HORIZONTAL);
                else
                    AnimaUtil.goneRainBow(windowItem2ShowChild,windowItem2Show,AnimaUtil.HORIZONTAL);
                break;
            case R.id.window_item3_photo:
                if(windowItem3Show.getVisibility()==View.GONE)
                    AnimaUtil.showRainBow(windowItem3ShowChild,windowItem3Show,AnimaUtil.HORIZONTAL);
                else
                    AnimaUtil.goneRainBow(windowItem3ShowChild,windowItem3Show,AnimaUtil.HORIZONTAL);
                break;
            case R.id.window_item1_contect_btn1:
                connectBtnClick(0,windowItem1ContectBtn1);
                break;
            case R.id.window_item1_contect_btn2:
                connectBtnClick(1,windowItem1ContectBtn2);
                break;
            case R.id.window_item1_contect_btn3:
                connectBtnClick(2,windowItem1ContectBtn3);
                break;
            case R.id.img_center:
                MainActivity.start(mContext);
                break;
            case R.id.img_setting:
                MainActivity.start(mContext);
                break;
        }
    }

    private void connectBtnClick(int index,Button button){
        try {
        WifiProvider wifiProvider=ww.get(index);
        if(wifiProvider==null){
            Toast.makeText(mContext,"wifi信息已丢失",Toast.LENGTH_SHORT).show();
            return;
        }
        if(button.getText().equals("已连"))return;
        switch (wifiProvider.getType()){
            case TYPE_SHOPER_FREE:
                WifiAdmin.getIntance(mContext).connect(wifiProvider.getSSID());
                break;
            case TYPE_SINGLE_FREE:
                WifiAdmin.getIntance(mContext).connect(wifiProvider.getSSID());
                break;
            case TYPE_SHOPER_PAY:
                MainActivity.start(mContext);
                AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,wifiProvider);
                break;
            case TYPE_SINGLE_PAY:
                MainActivity.start(mContext);
                AppManager.getInstance(mContext).getMainActivity().getPresenter().setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,wifiProvider);
                break;
        }
    }catch (Exception e){
            L.e(TAG,e.toString());
        }
    }



    private BroadcastReceiver mRecaiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(RECIVER_ACTION)){
                L.v(TAG,"wifi列表数据变化，刷新悬浮窗");
                getDataFromWifiFragment();//刷新wifi列表
            }

        }
    };
}
