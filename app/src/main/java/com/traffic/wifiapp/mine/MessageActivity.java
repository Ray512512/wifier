package com.traffic.wifiapp.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.bean.response.Message;
import com.traffic.wifiapp.mvp.presenter.MessagePresenter;
import com.traffic.wifiapp.mvp.view.MessageIView;
import com.traffic.wifiapp.ui.view.baseadapter.BaseQuickAdapter;
import com.traffic.wifiapp.ui.view.baseadapter.BaseViewHolder;
import com.traffic.wifiapp.ui.view.swipefreshReccycleview.MyRecyclerView;
import com.traffic.wifiapp.ui.view.swipefreshReccycleview.MySwipeRefreshLayout;
import com.traffic.wifiapp.webclient.WebViewActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity<MessagePresenter> implements MessageIView {


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.msg_recycler)
    MyRecyclerView msgRecycler;
    @Bind(R.id.msg_refresh)
    MySwipeRefreshLayout msgRefresh;

    private BaseQuickAdapter messageAdapter;
    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_message);
    }

    @Override
    protected void initBeforeData() {
        initHelperView(msgRecycler);
    }

    @Override
    protected void initEvents() {
        tvTitle.setText(R.string.msg_center_title);
        messageAdapter=  new BaseQuickAdapter<Message>(R.layout.msg_item,null) {
            @Override
            protected void convert(BaseViewHolder helper, Message item, int position) {
                helper.setText(R.id.msg_title,item.getTitle());
                helper.setText(R.id.msg_content,item.getIntro());
                helper.setOnClickListener(R.id.msg_root, v -> {
                    if(!TextUtils.isEmpty(item.getLink_url())){
                        WebViewActivity.start(mContext,item.getLink_url(),getString(R.string.msg_center_title));
                    }else {
                        showShortToast("暂无消息详情");
                    }
                });
            }
        };
        msgRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        msgRecycler.setAdapter(messageAdapter);
        msgRecycler.initLayout(msgRefresh, messageAdapter);
        msgRecycler.openMoreEnabled(false, false);
        msgRefresh.setColorSchemeColors(getResources().getColor(R.color.mainGreen));
        msgRecycler.setLoadingListener(() -> {
            msgRecycler.loadMoreComplete();
        });

        msgRefresh.setOnRefreshListener(() -> {
            getData();
        });
    }

    @Override
    protected void initAfterData() {
        getData();
    }

    private void getData(){
        showLoadingView();
        mPresenter.getMessage();
    }

    @Override
    protected void initPresenter() {
      mPresenter=new MessagePresenter(this,this);
    }


    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void getMessageList(ArrayList<Message> messages) {
        showDataView();
        msgRefresh.setRefreshing(false);
        messageAdapter.setNewData(messages);
    }

    @Override
    public void getMessageFailed() {
        showDataView();
    }
}
