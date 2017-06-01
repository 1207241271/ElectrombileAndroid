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

import cn.jpush.android.api.JPushInterface;

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
        //LeanCloud
        SDKInitializer.initialize(this);
        AVOSCloud.initialize(this,"5wk8ccseci7lnss55xfxdgj9xn77hxg3rppsu16o83fydjjn","yovqy5zy16og43zwew8i6qmtkp2y6r9b18zerha0fqi5dqsw");
        AVAnalytics.enableCrashReport(this,true);


        //JPush
        try {
            JPushInterface.setDebugMode(true);
            JPushInterface.init(this);
            JPushInterface.setLatestNotificationNumber(this,1);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
