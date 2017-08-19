package com.traffic.wifiapp.mine;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.mvp.presenter.JoinPresenter;
import com.traffic.wifiapp.mvp.view.JoinIView;

import butterknife.Bind;
import butterknife.OnClick;

public class JoinActivity extends BaseActivity<JoinPresenter> implements JoinIView {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.join_tv_name)
    EditText joinTvName;
    @Bind(R.id.join_tv_phone)
    EditText joinTvPhone;
    @Bind(R.id.join_tv_shop_name)
    EditText joinTvShopName;
    @Bind(R.id.join_tv_addr)
    EditText joinTvAddr;
    @Bind(R.id.join_btn_submit)
    Button joinBtnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_join);
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        tvTitle.setText(R.string.mine_join_title);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter=new JoinPresenter(this,this);
    }

    @OnClick({R.id.img_back, R.id.join_btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.join_btn_submit:
                if(checkInput()){
                    mPresenter.submit(joinTvName.getText().toString(),joinTvPhone.getText().toString(),
                            joinTvShopName.getText().toString(),joinTvAddr.getText().toString());
                }
                break;
        }
    }

    private boolean checkInput(){
        if(TextUtils.isEmpty(joinTvName.getText().toString())){
            showShortToast(R.string.mine_join_empty_name);
            return false;
        }
        if(!PhoneNumberUtils.isGlobalPhoneNumber(joinTvPhone.getText().toString())){
            showShortToast(R.string.mine_join_empty_phone);
            return false;
        }
        if(TextUtils.isEmpty(joinTvShopName.getText().toString())){
            showShortToast(R.string.mine_join_empty_shop_name);
            return false;
        }
        if(TextUtils.isEmpty(joinTvAddr.getText().toString())){
            showShortToast(R.string.mine_join_empty_addr);
            return false;
        }
        return true;
    }

    @Override
    public void submitSuccess() {
        finish();
    }
}
