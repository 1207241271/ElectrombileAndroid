package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;


import com.xiaoantech.electrombile.event.http.httpPost.HttpPostElectricSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostLockSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostSeatOpenEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostWheelSetEvent;
import com.xiaoantech.electrombile.http.HttpPublishManager;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.event.http.HttpGetEvent;
import com.xiaoantech.electrombile.utils.ErrorCodeConvertUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangxu on 2016/12/14.
 */

public class SettingManagerPresenter implements SettingManagerContract.Presenter{
    private  SettingManagerContract.View mSettingManagerView;
    private boolean isLinkOn;
    private boolean isLockOn;
    private boolean isWheelOn;

    protected SettingManagerPresenter(SettingManagerContract.View settingManagerView){
        this.mSettingManagerView = settingManagerView;
        mSettingManagerView.setPresenter(this);
    }

    public void subscribe(){
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void gotoAutoLock() {
        mSettingManagerView.gotoAutoLock();
    }

    @Override
    public void gotoChangePass() {
        mSettingManagerView.gotoChangePass();
    }

    @Override
    public void gotoPhoneAlarm() {
        mSettingManagerView.gotoPhoneAlarm();
    }

    @Override
    public void gotoRecord() {
        mSettingManagerView.gotoRecord();
    }

    @Override
    public void logout() {
        mSettingManagerView.logout();
    }

    public void isPhoneAlarmOpen(){
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/telephone/"+ BasicDataManager.getInstance().getBindIMEI();
        HttpManager.getHttpResult(url, HttpManager.getType.GET_TYPE_PHONENUM);
    }

    @Override
    public void relevenceSwitchChange(boolean isOn) {
        mSettingManagerView.showWaitingDialog("正在设置");
        isLinkOn = isOn;
        if (isOn)
            HttpPublishManager.getInstance().setLinkElectricLockOff();
        else
            HttpPublishManager.getInstance().setLinkElectricLockOn();
    }

    @Override
    public void rearWheelSwitchChange(boolean isOn) {
        mSettingManagerView.showWaitingDialog("正在设置");
        isWheelOn = isOn;
        if (isOn)
            HttpPublishManager.getInstance().setWheelLockOff();
        else
            HttpPublishManager.getInstance().setWheelLockOn();
    }

    @Override
    public void lockSwitchChange(boolean isOn) {
        mSettingManagerView.showWaitingDialog("正在设置");
        isLockOn = isOn;
        if (isOn)
            HttpPublishManager.getInstance().setLockOn();
        else
            HttpPublishManager.getInstance().setLockOFF();
    }

    @Override
    public void seatLockOpen() {
        mSettingManagerView.showWaitingDialog("正在设置");
        HttpPublishManager.getInstance().openSeatLock();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpGetEvent(HttpGetEvent event){
        try{
            JSONObject jsonObject = new JSONObject(event.getResult());
            if (jsonObject.has("telephone")){
                mSettingManagerView.PhoneAlarmOpen(true);
            }else {
                mSettingManagerView.PhoneAlarmOpen(false);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostElectricSetEvent(HttpPostElectricSetEvent event){

        if (event.getCode() == 0){
            mSettingManagerView.setSwitch(isLinkOn);
            mSettingManagerView.showToast("设置成功");
            LocalDataManager.getInstance().setIsRelevanceOn(isLinkOn);
            return;
        }else if (event.getCode() == 111){
            mSettingManagerView.showToast("该设备不支持此操作");
        }else {
            mSettingManagerView.showToast("设置失败");
        }
        mSettingManagerView.setSwitch(!isLinkOn);
        LocalDataManager.getInstance().setIsRelevanceOn(!isLinkOn);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostLockSetEvent(HttpPostLockSetEvent event){
        if (event.getCode() == ErrorCodeConvertUtil.HTTPCodeSuccess){
            mSettingManagerView.setLock(isLockOn);
            mSettingManagerView.showToast("设置成功");
            LocalDataManager.getInstance().setLockStatus(isLockOn);
            return;
        }else {
            mSettingManagerView.showToast(ErrorCodeConvertUtil.getHttpErrorStrWithCode(event.getCode()));
        }
        mSettingManagerView.setLock(!isLockOn);
        LocalDataManager.getInstance().setLockStatus(!isLockOn);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostWheelSetEvent(HttpPostWheelSetEvent event){
        if (event.getCode() == ErrorCodeConvertUtil.HTTPCodeSuccess){
            mSettingManagerView.setWheelLock(isWheelOn);
            mSettingManagerView.showToast("设置成功");
            LocalDataManager.getInstance().setWheelLock(isWheelOn);
            return;
        }else
            mSettingManagerView.showToast(ErrorCodeConvertUtil.getHttpErrorStrWithCode(event.getCode()));
        mSettingManagerView.setWheelLock(!isWheelOn);
        LocalDataManager.getInstance().setWheelLock(!isWheelOn);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostSeatOpenEvent(HttpPostSeatOpenEvent event){
        if (event.getCode() == ErrorCodeConvertUtil.HTTPCodeSuccess) {
            mSettingManagerView.showToast("设置成功");
        }else {
            mSettingManagerView.showToast(ErrorCodeConvertUtil.getHttpErrorStrWithCode(event.getCode()));
        }
    }
}
