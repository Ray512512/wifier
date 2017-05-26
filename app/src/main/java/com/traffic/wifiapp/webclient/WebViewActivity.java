package com.traffic.wifiapp.webclient;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.traffic.wifiapp.R;
import com.traffic.wifiapp.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    private static final java.lang.String TAG ="WebViewActivity" ;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.webview)
    WebView webview;

    private String  url;


    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initBeforeData() {
        tvTitle.setText("商品");
    }

    @Override
    protected void initEvents() {
        url=getIntent().getStringExtra("url");
        if(TextUtils.isEmpty(url)){
            finish();
            return;
        }

        webview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                return false;
            }
        });

        webview.loadUrl(url);
    }

    @Override
    protected void initAfterData() {

    }

    @Override
    protected void initPresenter() {

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}
