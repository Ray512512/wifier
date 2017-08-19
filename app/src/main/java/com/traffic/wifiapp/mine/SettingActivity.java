package com.traffic.wifiapp.mine;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.utils.SystemUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.setting_cb_msg)
    CheckBox settingCbMsg;

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

    @OnClick({R.id.img_back, R.id.menu_item_window})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.menu_item_window:
                SystemUtil.goToWindow(mContext);
                break;
        }
    }
}
