package com.traffic.wifiapp.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.bean.User;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.mvp.presenter.UserInfoPresenter;
import com.traffic.wifiapp.mvp.view.UserInfoIView;
import com.traffic.wifiapp.utils.FileUtils;
import com.zhihu.matisse.Matisse;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoIView {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.info_liner_avatar)
    LinearLayout infoLinerAvatar;
    @Bind(R.id.info_liner_name)
    LinearLayout infoLinerName;
    @Bind(R.id.info_liner_address)
    LinearLayout infoLinerAddress;
    @Bind(R.id.info_btn_out)
    Button infoBtnOut;
    @Bind(R.id.tv_action)
    TextView tvAction;
    @Bind(R.id.info_img_avatar)
    ImageView infoImgAvatar;
    @Bind(R.id.info_tv_name)
    EditText infoTvName;
    @Bind(R.id.info_tv_address)
    TextView infoTvAddress;

    private User user;
    private Uri avatarUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initBeforeData() {
        user = WifiApplication.getInstance().getUser();
    }

    @Override
    protected void initEvents() {
        tvTitle.setText(getString(R.string.mine_info_title));
        tvAction.setText(getString(R.string.mine_info_save));
        if (user == null) return;
        Glide.with(mContext).load(user.getFace()).placeholder(R.mipmap.logo).
                bitmapTransform(new CropCircleTransformation(mContext)).into(infoImgAvatar);
        infoTvName.setText(TextUtils.isEmpty(user.getNickname())?user.getMobile():user.getNickname());
    }

    @Override
    protected void initAfterData() {
        mPresenter = new UserInfoPresenter(this, this);
    }

    @Override
    protected void initPresenter() {
    }



    @OnClick({R.id.img_back, R.id.tv_action, R.id.info_liner_avatar, R.id.info_liner_name, R.id.info_liner_address, R.id.info_btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_action:
                if(TextUtils.isEmpty(infoTvName.getText().toString())){
                    showShortToast(R.string.mine_info_hint_name);
                    return;
                }
                String  p=user.getFace();
               if(avatarUri!=null){
                    p= FileUtils.getRealPathFromURI(mContext,avatarUri);
                }
                if(TextUtils.isEmpty(p)){
                    showShortToast(R.string.mine_info_hint_avatar);
                    return;
                }
                if(!infoTvAddress.getText().toString().contains("-")){
                    showShortToast(R.string.mine_info_hint_address);
                    return;
                }
                mPresenter.saveInfo(p, infoTvName.getText().toString());
                break;
            case R.id.info_liner_avatar:
                mPresenter.chooseImage();
                break;
            case R.id.info_liner_address:
                mPresenter.showAddrChoose();
                break;
            case R.id.info_btn_out:
                mPresenter.loginOut();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserInfoPresenter.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            if (mSelected != null && mSelected.size() > 0) {
                avatarUri = mSelected.get(0);
                Glide.with(mContext).load(avatarUri).bitmapTransform(new CropCircleTransformation(mContext)).into(infoImgAvatar);
            }
        }
    }

    @Override
    public void refreshAddr(String addr) {
        infoTvAddress.setText(addr);
    }


    @Override
    public void saveInfoSuccess() {
        setResult(RESULT_OK);
        finish();
    }

}
