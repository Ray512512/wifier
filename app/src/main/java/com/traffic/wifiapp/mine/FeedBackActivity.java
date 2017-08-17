package com.traffic.wifiapp.mine;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.mvp.presenter.FeedBackPresenter;
import com.traffic.wifiapp.mvp.view.FeedBackIView;
import com.traffic.wifiapp.utils.ViewUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackIView {
    public static final int LIMIT_SIZE=200;


    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.feedback_et)
    EditText feedbackEt;
    @Bind(R.id.feedback_et_title)
    EditText feedbackEtTitle;
    @Bind(R.id.feed_back_tv_hint)
    TextView tv_hint;

    private String content,title;
    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    protected void initBeforeData() {

    }

    @Override
    protected void initEvents() {
        tvTitle.setText(R.string.system_feedback_title);
        feedbackEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                content=feedbackEt.getText().toString();
                refeshInputHint(content);
            }
        });
    }

    private void refeshInputHint(String content){
        if(TextUtils.isEmpty(content.trim())){
            tv_hint.setText(getString(R.string.system_feedback_limit_hint));
        }else {
            String str=getString(R.string.system_feedback_limit_hint_input);
            str=String.format(str,LIMIT_SIZE-content.length());
            tv_hint.setText(str);
            if(LIMIT_SIZE==content.length()){
                ViewUtils.EditloseInputAndFocus(mContext,feedbackEt);
            }
        }
    }
    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {
        mPresenter=new FeedBackPresenter(this,this);
    }

    @Override
    public void submitSuccess() {
       finish();
    }

    @OnClick({R.id.img_back, R.id.info_btn_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.info_btn_out:
                title=feedbackEtTitle.getText().toString();
                if(TextUtils.isEmpty(title)){
                    showShortToast(R.string.system_feedback_hint_title);
                    return;
                }
                if(TextUtils.isEmpty(content)){
                    showShortToast(R.string.system_feedback_hint_content);
                    return;
                }
                mPresenter.submit(title,content);
                break;
        }
    }
}
