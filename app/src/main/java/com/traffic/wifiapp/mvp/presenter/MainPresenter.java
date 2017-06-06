package com.traffic.wifiapp.mvp.presenter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.traffic.wifiapp.MainActivity;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseIView;
import com.traffic.wifiapp.base.BasePresenter;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.fragment.MapFragment;
import com.traffic.wifiapp.fragment.MineFragment;
import com.traffic.wifiapp.fragment.ShopAndPayFragment;
import com.traffic.wifiapp.fragment.WifiFragment;

import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SHOPER_PAY;
import static com.traffic.wifiapp.bean.response.WifiProvider.TYPE_SINGLE_PAY;

/**
 * Created by Ray on 2017/5/6.
 * email：1452011874@qq.com
 */

public class MainPresenter extends BasePresenter {
    private MainActivity mainActivity;
    private ViewPager viewpager;

    public MainPresenter(Activity mContext, BaseIView mView) {
        super(mContext, mView);
        mainActivity = (MainActivity) mContext;
    }

    public static final int FRAGMENT_COUNT = 4;
    public static final int FRAGMENT_INDEX_ONE = 0;
    public static final int FRAGMENT_INDEX_TWO = 1;
    public static final int FRAGMENT_INDEX_THREE = 2;
    public static final int FRAGMENT_INDEX_FOUR = 3;
    private Fragment[] mFragments = new Fragment[FRAGMENT_COUNT];
    private RadioGroup mRadioGroup;
    private RadioButton[] mRadioButtons = new RadioButton[FRAGMENT_COUNT];


    public void initUI() {
        viewpager = (ViewPager) mainActivity.findViewById(R.id.viewpager);
        mRadioGroup = (RadioGroup) mainActivity.findViewById(R.id.radio_group);
        setRadioGroupListener();
        mRadioButtons[FRAGMENT_INDEX_ONE] = (RadioButton) mainActivity.findViewById(R.id.radio_button_one);
        mRadioButtons[FRAGMENT_INDEX_TWO] = (RadioButton) mainActivity.findViewById(R.id.radio_button_two);
        mRadioButtons[FRAGMENT_INDEX_THREE] = (RadioButton) mainActivity.findViewById(R.id.radio_button_third);
        mRadioButtons[FRAGMENT_INDEX_FOUR] = (RadioButton) mainActivity.findViewById(R.id.radio_button_four);

        mFragments[FRAGMENT_INDEX_ONE] = WifiFragment.newInstance();
        mFragments[FRAGMENT_INDEX_TWO] = MapFragment.newInstance();
        mFragments[FRAGMENT_INDEX_THREE] = ShopAndPayFragment.newInstance();
        mFragments[FRAGMENT_INDEX_FOUR] = MineFragment.newInstance();

        viewpager.setOffscreenPageLimit(FRAGMENT_COUNT);
        FragmentStatePagerAdapter paperAdapter = new FragmentStatePagerAdapter(mainActivity.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments[arg0];
            }
        };
        viewpager.setAdapter(paperAdapter);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mRadioButtons[arg0].setChecked(true);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void setRadioGroupListener() {
        mRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_button_one:
                    viewpager.setCurrentItem(FRAGMENT_INDEX_ONE);
                    break;
                case R.id.radio_button_two:
                    viewpager.setCurrentItem(FRAGMENT_INDEX_TWO);
                    break;
                case R.id.radio_button_third:
                    break;
                case R.id.radio_button_four:
                    viewpager.setCurrentItem(FRAGMENT_INDEX_FOUR);
                    break;
                default:
                    break;
            }
        });
        mainActivity.findViewById(R.id.radio_button_third).setOnClickListener(v -> {
            WifiProvider mWifiProvider= WifiApplication.getInstance().getCurrentWifi();
            if(mWifiProvider==null){//
                setMoneyPage(ShopAndPayFragment.TYPE_SHOW_NORMAL,mWifiProvider);
            }else {
                if(mWifiProvider.getType()==TYPE_SHOPER_PAY||mWifiProvider.getType()==TYPE_SINGLE_PAY){
                    setMoneyPage(ShopAndPayFragment.TYPE_SHOW_PAYS,mWifiProvider);
                }else {
                    setMoneyPage(ShopAndPayFragment.TYPE_SHOW_GOODS,mWifiProvider);
                }
            }
        });
    }

    public void setMoneyPage(int type, WifiProvider wifiProvider){
        ((ShopAndPayFragment)mFragments[FRAGMENT_INDEX_THREE]).setViewType(type);
        ((ShopAndPayFragment)mFragments[FRAGMENT_INDEX_THREE]).setmWifiProvider(wifiProvider);
        viewpager.setCurrentItem(FRAGMENT_INDEX_THREE);
    }

    public WifiFragment getWifiFragment(){
        return (WifiFragment) mFragments[FRAGMENT_INDEX_ONE];
    }

    public MapFragment getMapFragment(){
        return (MapFragment) mFragments[FRAGMENT_INDEX_TWO];
    }

    public ShopAndPayFragment getPayFragment(){
        return (ShopAndPayFragment) mFragments[FRAGMENT_INDEX_THREE];
    }



}