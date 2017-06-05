package com.traffic.wifiapp.manager.window;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by ray on 2017/6/2.
 * emial:1452011874@qq.com
 */

public class WindowHideManager {
    public static final int TYPE_LEFT_IN=0;
    public static final int TYPE_LEFT_OUT=1;
    public static final int TYPE_RIGHT_IN=2;
    public static final int TYPE_RIGHT_OUT=3;
    public static final int TYPE_TOP_IN=4;
    public static final int TYPE_TOP_OUT=5;
    public static final int TYPE_BOTTOM_IN=6;
    public static final int TYPE_BOTTOM_OUT=7;


    public static final int TYPE_MOVEING=8;

    public WindowHideManager(View view) {
        this.view = view;
        new Handler().postDelayed(() -> action(mType),500);
    }

    private View view;
    private int mType =TYPE_LEFT_IN;
    private float fromX=0.0f,toX=0.0f,fromY=0.0f,toY=0.0f;

    public int getmType() {
        return mType;
    }

    public boolean isMoving(){
        if(mType==TYPE_MOVEING)return true;
        return false;
    }
    public void post(int type){
        action(type);
    }
    /**
     * 将悬浮球隐藏一半
     * */
    public  void action(int type){
        if(mType==TYPE_MOVEING)return;
        switch (type){
            case TYPE_LEFT_IN:
                fromX=0.0f;toX=-0.5f;fromY=0.0f;toY=0.0f;
                break;
            case TYPE_LEFT_OUT:
                fromX=-0.5f;toX=0.0f;fromY=0.0f;toY=0.0f;
                break;
            case TYPE_RIGHT_IN:
                fromX=0.0f;toX=0.5f;fromY=0.0f;toY=0.0f;
                break;
            case TYPE_RIGHT_OUT:
                fromX=0.5f;toX=0.0f;fromY=0.0f;toY=0.0f;
                break;
            case TYPE_TOP_IN:
                fromX=0.0f;toX=0.0f;fromY=0.0f;toY=-0.5f;
                break;
            case TYPE_TOP_OUT:
                fromX=0.0f;toX=0.0f;fromY=-0.5f;toY=0.0f;
                break;
            case TYPE_BOTTOM_IN:
                fromX=0.0f;toX=0.0f;fromY=0.0f;toY=0.5f;
                break;
            case TYPE_BOTTOM_OUT:
                fromX=0.0f;toX=0.0f;fromY=0.5f;toY=0.0f;
                break;
        }
        TranslateAnimation animation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,fromX,
                TranslateAnimation.RELATIVE_TO_SELF,toX,
                TranslateAnimation.RELATIVE_TO_SELF,fromY,
                TranslateAnimation.RELATIVE_TO_SELF,toY);
        animation.setDuration(500);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mType=TYPE_MOVEING;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mType=type;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    /**
     * 移动悬浮球前 检查悬浮球当前状态
     * 若处于隐藏或者正在从隐藏到显示状态都阻止touch事件
     * */
    public boolean onTouchEvent() {
        switch (mType){
            case TYPE_MOVEING:
                return true;
            case TYPE_LEFT_IN:
                action(TYPE_LEFT_OUT);
                return true;
          /*  case TYPE_LEFT_OUT:
                action(TYPE_LEFT_IN);
                break;*/
            case TYPE_RIGHT_IN:
                action(TYPE_RIGHT_OUT);
                return true;
          /*  case TYPE_RIGHT_OUT:
                action(TYPE_RIGHT_IN);
                break;*/
            case TYPE_TOP_IN:
                action(TYPE_TOP_OUT);
                return true;
           /* case TYPE_TOP_OUT:
                action(TYPE_TOP_IN);
                break;*/
            case TYPE_BOTTOM_IN:
                action(TYPE_BOTTOM_OUT);
                return true;
           /* case TYPE_BOTTOM_OUT:
                action(TYPE_BOTTOM_IN);
                break;*/
        }
        return false;
    }
    
}
