package com.xiaoantech.electrombile.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.constant.HandlerConstant;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.ui.ViewPager.ViewPagerActivity;
import com.xiaoantech.electrombile.ui.login.LoginMain.LoginMainActivity;
import com.xiaoantech.electrombile.ui.main.FragmentMainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangxu on 2017/3/30.
 */

public class LaunchPageActivity extends Activity {
    private Timer   timer;
    private int     counter;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == HandlerConstant.TimerWhat0){
                counter ++;
                if (counter > 2){
                    timer.cancel();
                    if (LocalDataManager.getInstance().getIsFirstLaunch()){
                        gotoPagerActivity();
                        LocalDataManager.getInstance().setIsFirstLaunch(false);
                    }else {
                        checkUserStatus();
                    }
                }
            }
            if (msg.what == HandlerConstant.StartTimer){
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(HandlerConstant.TimerWhat0);
                    }
                },1000,1000);
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

        this.setContentView(R.layout.activity_launch);
        DisplayMetrics dm = new DisplayMetrics();

    }

    @Override
    protected void onResume() {
        super.onResume();
        counter = 0;
        handler.sendEmptyMessage(HandlerConstant.StartTimer);
    }

    private void checkUserStatus(){
        AVUser user = AVUser.getCurrentUser();
        if (null != user){
            //已经有登陆状态
            if(null != LocalDataManager.getInstance().getIMEI() && !LocalDataManager.getInstance().getIMEI().isEmpty()) {
                MqttManager.getInstance().subscribe(LocalDataManager.getInstance().getIMEI());
                gotoFragmentActivity();

                //TODO: 暂时直接从服务器获取，之后本地化
                BasicDataManager.getInstance().initFromLocal();
            }else {
                gotoLoginActivity();
            }
        }else {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity(){
        Intent intent = new Intent(this, LoginMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void gotoFragmentActivity(){
        Intent intent = new Intent(this, FragmentMainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void gotoPagerActivity(){
        Intent intent = new Intent(this, ViewPagerActivity.class);
        startActivity(intent);
        this.finish();
    }

}
