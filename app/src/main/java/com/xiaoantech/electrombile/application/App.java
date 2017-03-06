package com.xiaoantech.electrombile.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.baidu.mapapi.SDKInitializer;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.ui.login.LoginMain.LoginMainActivity;
import com.xiaoantech.electrombile.ui.main.FragmentMainActivity;

/**
 * Created by jk on 16-10-26.
 *
 * initialize AVOS cloud and the baidu map sdk
 *
 */

public class App extends Application {

    private static Context  context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        MqttManager.getInstance().createConnect();
        SDKInitializer.initialize(this);
        AVOSCloud.initialize(this,"5wk8ccseci7lnss55xfxdgj9xn77hxg3rppsu16o83fydjjn","yovqy5zy16og43zwew8i6qmtkp2y6r9b18zerha0fqi5dqsw");
        AVAnalytics.enableCrashReport(this,true);
        checkUserStatus();
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void gotoFragmentActivity(){
        Intent intent = new Intent(this, FragmentMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
