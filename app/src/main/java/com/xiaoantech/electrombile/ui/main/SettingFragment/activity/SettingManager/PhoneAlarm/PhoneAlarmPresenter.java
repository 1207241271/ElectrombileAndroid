package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoantech.electrombile.constant.HandlerConstant;
import com.xiaoantech.electrombile.constant.TimerConstant;
import com.xiaoantech.electrombile.event.http.HttpDeleteEvent;
import com.xiaoantech.electrombile.event.http.HttpPutEvent;
import com.xiaoantech.electrombile.http.HttpPublishManager;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangxu on 2017/2/25.
 */

public class PhoneAlarmPresenter implements PhoneAlarmContract.Presenter{
    private final static String TAG = "PhoneAlarmPresenter";
    private PhoneAlarmContract.View mPhoneAlarm;
    private int secondleft;
    private Timer timer;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == HandlerConstant.TimerWhat0) {
                secondleft--;
                if (secondleft <= 0) {
                    timer.cancel();
                    mPhoneAlarm.changeCutDownStatus(0);
                } else {
                    mPhoneAlarm.changeCutDownStatus(secondleft);
                }
            } else if (msg.what == HandlerConstant.StartTimer) {
                mPhoneAlarm.hideWaitingDialog();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(HandlerConstant.TimerWhat0);
                    }
                }, 1000, 1000);
            }
        }
    };

    public void AddCallerIndex(){
        int caller = LocalDataManager.getInstance().getContractIndex() + 1;
        if (caller >= 10){
            caller = 0;
        }
        LocalDataManager.getInstance().setCallerIndex(caller);
        phoneAlarmTest();
    }


    protected PhoneAlarmPresenter(PhoneAlarmContract.View phoneAlarm){
        this.mPhoneAlarm = phoneAlarm;
        mPhoneAlarm.setPresenter(this);
    }

    public void subscribe(){
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void phoneAlarmTest() {
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/telephone/"+BasicDataManager.getInstance().getBindIMEI();
        try{
            JSONObject caller = new JSONObject();
            caller.put("caller",LocalDataManager.getInstance().getCallerIndex());
            mPhoneAlarm.showWaitingDialog("正在设置");
            HttpManager.putHttpResult(url, HttpManager.putType.PUT_TYPE_ALARMPHONE, caller.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        secondleft = 60;
        EventBus.getDefault().post(new HttpPutEvent(HttpManager.putType.PUT_TYPE_ALARMPHONE,"{\"code\":0}",true));
    }

    @Override
    public void phoneAlarmUnreceived() {
        mPhoneAlarm.gotoResendActivity();
    }

    @Override
    public void phoneAlarmDelete() {
        String url = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort()+"/v1/telephone/" + BasicDataManager.getInstance().getBindIMEI();
        HttpManager.deleteHttpResult(url, HttpManager.deleteType.DELETE_TYPE_ALARMPHONE,null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPutEvent(HttpPutEvent event){
        if (event.getRequestType() == HttpManager.putType.PUT_TYPE_ALARMPHONE){
            try {
                JSONObject jsonObject = new JSONObject(event.getResultStr());
                int code = jsonObject.getInt("code");
                if (code == 0){
                    mPhoneAlarm.showToast("开始测试");
                    handler.sendEmptyMessage(HandlerConstant.StartTimer);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        mPhoneAlarm.showToast("开始测试");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpDeleteEvent(HttpDeleteEvent event){
        if (event.getRequestType() == HttpManager.deleteType.DELETE_TYPE_ALARMPHONE){
            try {
                JSONObject jsonObject = new JSONObject(event.getResultStr());
                int code = jsonObject.getInt("code");
                if (code == 0){
                    LocalDataManager.getInstance().setPhoneAlarmOpen(false);
                    mPhoneAlarm.showToast("电话报警已关闭");
                    mPhoneAlarm.finishActivity();
                }else {
                    mPhoneAlarm.showToast("电话报警关闭失败");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
