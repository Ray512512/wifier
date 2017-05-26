package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.base.BaseIView;
import com.traffic.wifiapp.bean.response.SlideImageUrls;

import java.util.List;

/**
 * Created by xy on 16/5/16.
 */
public interface BaiduMapIView extends BaseIView {
    void showSlide(List<SlideImageUrls> urls);
}
