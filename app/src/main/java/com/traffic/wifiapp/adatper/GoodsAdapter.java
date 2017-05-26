package com.traffic.wifiapp.adatper;

import com.bumptech.glide.Glide;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.bean.response.Goods;
import com.traffic.wifiapp.ui.view.baseadapter.BaseQuickAdapter;
import com.traffic.wifiapp.ui.view.baseadapter.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Ray on 2017/5/12.
 * email：1452011874@qq.com
 */

public class GoodsAdapter extends BaseQuickAdapter<Goods> {
    public GoodsAdapter() {
        super(R.layout.goods_item,new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item, int position) {
        Glide.with(mContext).load(item.getPhoto()).into(helper.getImageView(R.id.good_item_img));
        helper.setText(R.id.good_item_tv_content,item.getCnt());
        boolean isShowPrice=item.getPrice()!=null&&!item.getPrice().equals("0");
        helper.setVisibleVGone(R.id.good_item_tv_price, isShowPrice);
        if(isShowPrice){
            helper.setText(R.id.good_item_tv_price,"价格："+item.getPrice()+"元");
        }
    }
}
