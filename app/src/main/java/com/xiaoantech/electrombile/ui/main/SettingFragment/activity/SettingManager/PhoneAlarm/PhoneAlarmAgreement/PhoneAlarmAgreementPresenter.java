package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.constant.EventBusConstant;
import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.cmd.AutoLockEvent;
import com.xiaoantech.electrombile.event.http.HttpPostEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.HttpManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/18.
 */

public class PhoneAlarmAgreementPresenter implements PhoneAlarmAgreementContract.Presenter{
    private final static String TAG = "PhoneAlarmAgreementPresenter";
    private PhoneAlarmAgreementContract.View mPhoneAlarmAgreement;

    protected PhoneAlarmAgreementPresenter(PhoneAlarmAgreementContract.View mPhoneAlarmAgreement){
        this.mPhoneAlarmAgreement = mPhoneAlarmAgreement;
        mPhoneAlarmAgreement.setPresenter(this);
    }


    public void subscribe(){
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    public void setPhoneAlarmPhone(){
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/telephone/"+ BasicDataManager.getInstance().getBindIMEI();
        String tel = AVUser.getCurrentUser().getUsername();
        String body = "{\"telephone\":\"" + tel + "\"}";
        HttpManager.postHttpResult(url, HttpManager.postType.POST_TYPE_PHONE, HttpConstant.HttpCmd.HTTP_CMD_SETPHONEALARM, body);
        mPhoneAlarmAgreement.showWaitingDialog("正在设置");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostEvent(HttpPostEvent event){
        if (event.getCmdType() == HttpConstant.HttpCmd.HTTP_CMD_SETPHONEALARM || event.getRequestType() == HttpManager.postType.POST_TYPE_PHONE){
            try {
                mPhoneAlarmAgreement.showToast("设置成功");
                JSONObject object = new JSONObject(event.getResult());
                int code = object.getInt("code");
                if (code == 0) {
                    mPhoneAlarmAgreement.toPhoneAlarmActivity(true);
                }else{
                    mPhoneAlarmAgreement.toPhoneAlarmActivity(true);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
