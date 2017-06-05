package com.traffic.wifiapp;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.traffic.wifiapp.base.BaseActivity;
import com.traffic.wifiapp.bean.response.WifiProvider;
import com.traffic.wifiapp.common.ConstantField;
import com.traffic.wifiapp.common.WifiApplication;
import com.traffic.wifiapp.manager.window.WindowUtils;
import com.traffic.wifiapp.mvp.presenter.MainPresenter;
import com.traffic.wifiapp.mvp.presenter.WifiAppPresenter;
import com.traffic.wifiapp.mvp.view.MainIView;
import com.traffic.wifiapp.service.WindowsService;
import com.traffic.wifiapp.utils.AlertDialogUtil;
import com.traffic.wifiapp.utils.SPUtils;
import com.traffic.wifiapp.utils.SystemUtil;

import java.util.Timer;
import java.util.TimerTask;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

import static com.traffic.wifiapp.common.ConstantField.JUMP_ACTION;

@RuntimePermissions
public class MainActivity extends BaseActivity<MainPresenter> implements MainIView {


    @Override
    protected void setMainLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initBeforeData() {
    }

    @Override
    protected void initEvents() {
      mPresenter.initUI();
       /* registerReceiver(mHomeKeyEventReceiver, new IntentFilter(
                Intent.ACTION_CLOSE_SYSTEM_DIALOGS));*/
    }

    @Override
    protected void initAfterData() {
        MainActivityPermissionsDispatcher.windowPermissionPassWithCheck(this);
        checkWindowPerission();
        checkJump();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkJump();
    }

    private void checkJump(){
        WifiProvider w=getIntent().getParcelableExtra(JUMP_ACTION);
        if(w!=null){
            WindowUtils.checkJumpAction(this,w);
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter=new MainPresenter(this,this);
        WifiApplication.getInstance().setmWifiPresenter(new WifiAppPresenter(this));
    }

    public MainPresenter getPresenter(){
        return mPresenter;
    }

    public static void start(Context context){
        if(WindowUtils.canJumpMain(context)){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        }
    }

    public static void jumpPay(Context context, WifiProvider w){
        if(WindowUtils.canJumpMain(context)) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(JUMP_ACTION, w);
            context.startActivity(intent);
        }
    }
    private static boolean isExit = false;
    @Override
    public void onBackPressed() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            showShortToast("再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            startService(new Intent(this, WindowsService.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @NeedsPermission({Manifest.permission.SYSTEM_ALERT_WINDOW})
    public void windowPermissionPass(){

    }

    @OnPermissionDenied({Manifest.permission.SYSTEM_ALERT_WINDOW})
    public void windowDenied(){
        AlertDialogUtil.AlertDialog(mContext, "设置", "为给您提供更好的服务，请授予应用悬浮窗权限", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
    }

    /**
     * 检测悬浮窗权限
     * */
    private void checkWindowPerission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if(!SPUtils.getBooleanValue(ConstantField.IS_HINT_SYSTEM_WINDOW)){
                SPUtils.put(ConstantField.IS_HINT_SYSTEM_WINDOW,true);
                AlertDialogUtil.AlertDialog(mContext, "设置", "为给您提供更好的服务，请授予应用悬浮窗权限", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
            }
          /*  AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int checkResult = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Binder.getCallingUid(), context.getPackageName());
            if(checkResult == AppOpsManager.MODE_ALLOWED){
                Log.e("jijiaxin","有权限");
            }else if(checkResult == AppOpsManager.MODE_IGNORED){
                // TODO: 只需要依此方法判断退出就可以了，这时是没有权限的。
                AlertDialogUtil.AlertDialog(mContext, "设置", "为给您提供更好的服务，请授予应用悬浮窗权限", (dialog, which) -> SystemUtil.goToAppSetting(mContext));
                Log.e("jijiaxin","被禁止了");
            }else if(checkResult == AppOpsManager.MODE_ERRORED){
                Log.e("jijiaxin","出错了");
            }else if(checkResult == 4){
                Log.e("jijiaxin","权限需要询问");
            }*/
        }
        //4个状态，0默认 1可用 2禁止 3user disable
    }
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_REASON = "reason";
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
//                    AppManager.getInstance(mContext).killAllActivity();
//                    finish();
                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
                    //表示长按home键,显示最近使用的程序列表
                }
            }
        }
    };

}
