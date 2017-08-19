package com.traffic.wifiapp.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.login.LoginActivity;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.utils.SystemUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.setting_cb_msg)
    CheckBox settingCbMsg;
    @Bind(R.id.info_btn_out)
    Button infoBtnOut;
    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        tvTitle.setText(R.string.tag_setting);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {

    }

    @OnClick({R.id.img_back, R.id.menu_item_window,R.id.info_btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.menu_item_window:
                SystemUtil.goToWindow(mContext);
                break;
            case R.id.info_btn_out:
                loginOut();
                break;
        }
    }
    public void loginOut() {
        SPUtils.put(ConstantField.USER, null);
        SPUtils.put(ConstantField.USER_NAME, "");
        AppManager.getInstance(mContext).killAllActivity();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }

}
