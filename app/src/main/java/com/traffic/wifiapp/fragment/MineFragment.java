package com.traffic.wifiapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.bean.MineItem;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.common.adapter.RecyleAdapter;
import com.traffic.wifiapp.common.adapter.base.BaseAdapterHelper;
import com.traffic.wifiapp.mine.FeedBackActivity;
import com.traffic.wifiapp.mine.JoinActivity;
import com.traffic.wifiapp.mine.MessageActivity;
import com.traffic.wifiapp.mine.SettingActivity;
import com.traffic.wifiapp.mine.UserInfoActivity;
import com.traffic.wifiapp.mvp.presenter.MinePresenter;
import com.traffic.wifiapp.mvp.view.MineIView;
import com.traffic.wifiapp.ui.view.RoundImageView;
import com.traffic.wifiapp.utils.SystemUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ray on 2017/6/6.
 * emial:1452011874@qq.com
 */

public class MineFragment extends BaseFragment<MinePresenter> implements MineIView {
    public static final int MENU_ACCOUNT=0;//账户管理
    public static final int MENU_MESSAGE=1;//消息
    public static final int MENU_MY_TASK=2;//我的任务
    public static final int MENU_SHOP=3;//积分商城
    public static final int MENU_HELP=3;//帮助与反馈
    public static final int MENU_JOIN=2;//加入我们
    public static final int MENU_SHARE=6;//分享
    public static final int MENU_MY_FOOT=7;//我的足迹
    public static final int MENU_COLLECT=8;//我的收藏
    public static final int MENU_CHEAP=9; //优惠劵
    public static final int MENU_SETTING=4; //设置
    @Bind(R.id.mine_avatar)
    RoundImageView mineAvatar;
    @Bind(R.id.mine_username)
    TextView mineUsername;
    @Bind(R.id.mine_foot)
    LinearLayout mineFoot;
    @Bind(R.id.mine_collect)
    LinearLayout mineCollect;
    @Bind(R.id.mine_cheap)
    LinearLayout mineCheap;
    @Bind(R.id.mine_recycler)
    RecyclerView mineRecycler;

    private ArrayList<MineItem> menus=new ArrayList<>();
    private RecyleAdapter<MineItem> adapter;

    private User user;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_mine_layout;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MinePresenter((Activity) mContext, this);
    }

    @Override
    protected void initView(Bundle savedInstanceSate) {
        new MenuItemClick(MENU_MY_FOOT,mineFoot);
        new MenuItemClick(MENU_COLLECT,mineCollect);
        new MenuItemClick(MENU_CHEAP,mineCheap);

    adapter= new RecyleAdapter<MineItem>(mContext, R.layout.mine_item_layout) {
            @Override
            protected void convert(BaseAdapterHelper helper, MineItem item, int position) {
                helper.setImageResource(R.id.menu_item_img,item.getImgRes());
                helper.setText(R.id.menu_item_tv,item.getTitle());
                new MenuItemClick(position,helper.getView(R.id.menu_item_root));
                if(position==MENU_JOIN){
                    helper.setVisible(R.id.menu_item_line,true);
                }else {
                    helper.setVisible(R.id.menu_item_line,false);
                }
            }
        };
        mineRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mineRecycler.setAdapter(adapter);
        mineRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, SystemUtil.dip2px(mContext,1));
            }
        });

        initMenu();
        intData();


    }

    private void initMenu(){
        menus.add(new MineItem(R.string.mine_item_account,R.mipmap.account));
        menus.add(new MineItem(R.string.mine_item_message,R.mipmap.message));
//        menus.add(new MineItem(R.string.mine_item_mytask,R.mipmap.mytask));
//        menus.add(new MineItem(R.string.mine_item_shop,R.mipmap.shop));
        menus.add(new MineItem(R.string.mine_item_join,R.mipmap.join));
        menus.add(new MineItem(R.string.mine_item_help,R.mipmap.mytask));
        menus.add(new MineItem(R.string.tag_setting,R.mipmap.help));

        adapter.setAll(menus);
    }

    private void intData(){
        user= WifiApplication.getInstance().getUser();
        mineUsername.setText(TextUtils.isEmpty(user.getNickname())?user.getMobile():user.getNickname());
        Glide.with(mContext).load(user.getFace()).asBitmap().placeholder(R.drawable.ic_app).into(mineAvatar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== ConstantField.REQUEST_USER){
            if(resultCode==Activity.RESULT_OK){//修改过个人信息
                intData();
            }
        }
    }

    private class MenuItemClick {
       private int pos;
       private View view;

       public MenuItemClick(int pos,View view) {
           this.pos = pos;
           this.view=view;
           registOnclick();
       }

       private void registOnclick(){
           RxView.clicks(view).throttleFirst(1, TimeUnit.SECONDS)
                   .subscribe(o -> {
                       switch (pos){
                           case MENU_ACCOUNT://账户管理
                               ((Activity)mContext).startActivityForResult(new Intent(mContext, UserInfoActivity.class),ConstantField.REQUEST_USER);
                               break;
                           case MENU_MESSAGE://信息中心
                               startActivity(new Intent(mContext, MessageActivity.class));
                               break;
                           case MENU_JOIN://我要加入
                               startActivity(new Intent(mContext, JoinActivity.class));
                               break;
                           case MENU_HELP://意见反馈
                               startActivity(new Intent(mContext, FeedBackActivity.class));
                               break;
                           case MENU_SETTING://设置
                               startActivity(new Intent(mContext, SettingActivity.class));
                               break;
                       }
                   });
       }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
