package com.traffic.wifiapp.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseFragment;
import com.traffic.wifiapp.bean.MineItem;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.common.adapter.RecyleAdapter;
import com.traffic.wifiapp.common.adapter.base.BaseAdapterHelper;
import com.traffic.wifiapp.mvp.presenter.MinePresenter;
import com.traffic.wifiapp.mvp.view.MineIView;
import com.traffic.wifiapp.utils.SystemUtil;

import java.util.ArrayList;

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
    public static final int MENU_HELP=4;//帮助与反馈
    public static final int MENU_JOIN=5;//加入我们
    public static final int MENU_SHARE=6;//分享
    public static final int MENU_MY_FOOT=7;//我的足迹
    public static final int MENU_COLLECT=8;//我的收藏
    public static final int MENU_CHEAP=9; //优惠劵
    @Bind(R.id.mine_avatar)
    ImageView mineAvatar;
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
        mineFoot.setOnClickListener(new MenuItemClick(MENU_MY_FOOT));
        mineCollect.setOnClickListener(new MenuItemClick(MENU_COLLECT));
        mineCheap.setOnClickListener(new MenuItemClick(MENU_CHEAP));
        adapter= new RecyleAdapter<MineItem>(mContext, R.layout.mine_item_layout) {
            @Override
            protected void convert(BaseAdapterHelper helper, MineItem item, int position) {
                helper.setImageResource(R.id.menu_item_img,item.getImgRes());
                helper.setText(R.id.menu_item_tv,item.getTitle());
                helper.setOnClickListener(R.id.menu_item_root,new MenuItemClick(position));
                if(position==MENU_MY_TASK){
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
        menus.add(new MineItem(R.string.mine_item_mytask,R.mipmap.mytask));
        menus.add(new MineItem(R.string.mine_item_shop,R.mipmap.shop));
        menus.add(new MineItem(R.string.mine_item_help,R.mipmap.help));
        menus.add(new MineItem(R.string.mine_item_join,R.mipmap.join));
        menus.add(new MineItem(R.string.mine_item_share,R.mipmap.share));

        adapter.setAll(menus);
    }

    private void intData(){
        user= WifiApplication.getInstance().getUser();
        mineUsername.setText(user.getMobile());
    }

   private class MenuItemClick implements View.OnClickListener{
       private int pos;

       public MenuItemClick(int pos) {
           this.pos = pos;
       }

       @Override
        public void onClick(View v) {
           switch (pos){
               case MENU_ACCOUNT:
                   break;
               case MENU_MESSAGE:
                   break;
               case MENU_MY_TASK:
                   break;
               case MENU_SHOP:
                   break;
               case MENU_HELP:
                   break;
               case MENU_JOIN:
                   break;
               case MENU_SHARE:
                   break;
               case MENU_MY_FOOT:
                   break;
               case MENU_COLLECT:
                   break;
               case MENU_CHEAP:
                   break;
           }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
