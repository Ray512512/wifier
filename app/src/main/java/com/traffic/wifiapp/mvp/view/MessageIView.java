package com.traffic.wifiapp.mvp.view;

import com.traffic.wifiapp.base.BaseIView;
import com.traffic.wifiapp.bean.response.Message;

import java.util.ArrayList;

/**
 * Created by xy on 16/5/16.
 */
public interface MessageIView extends BaseIView {

    void getMessageList(ArrayList<Message> messages);

    void getMessageFailed();
}
