package com.traffic.wifiapp.manager.hotfix;

import android.app.Application;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.traffic.wifiapp.utils.AppManager;
import com.traffic.wifiapp.utils.L;
import com.traffic.wifiapp.utils.VersionInfoUtil;

/**
 * Created by ray on 2017/5/26.
 * emial:1452011874@qq.com
 */

public class MyHotFixManager {
private static final String ASE_KEY="1452011874@qqcom";
private static final String TAG="HotFixManager";

    public static  boolean isVisblerToUser=true;
    public static void init(Application context){
        isVisblerToUser=true;
        SophixManager.getInstance().setContext(context)
                .setAppVersion(VersionInfoUtil.getVersionName(context))
                .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub((mode, code, info, handlePatchVersion) -> {
                    // 补丁加载回调通知
                    if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                        L.v(TAG,"补丁加载成功");
                        // 表明补丁加载成功
                    } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                        L.v(TAG,"补丁生效需要重启");
                        if(!isVisblerToUser){
                            L.v(TAG,"App在后台，直接杀死app下次进入补丁立即生效");
                            AppManager.getInstance(context).AppExit(context);
                        }
                        // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                        // 建议: 用户可以监听进入后台事件, 然后应用自杀
                    } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                        L.v(TAG,"内部引擎异常");
                        // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                         SophixManager.getInstance().cleanPatches();
                    } else {
                        // 其它错误信息, 查看PatchStatus类说明
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
   }
}
