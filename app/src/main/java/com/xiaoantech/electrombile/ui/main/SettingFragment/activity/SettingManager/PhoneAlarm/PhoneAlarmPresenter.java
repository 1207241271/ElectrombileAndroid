package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm;

/**
 * Created by yangxu on 2017/2/25.
 */

public class PhoneAlarmPresenter implements PhoneAlarmContract.Presenter{
    private final static String TAG = "PhoneAlarmPresenter";
    private PhoneAlarmContract.View mPhoneAlarm;

    protected PhoneAlarmPresenter(PhoneAlarmContract.View phoneAlarm){
        this.mPhoneAlarm = phoneAlarm;
        mPhoneAlarm.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
