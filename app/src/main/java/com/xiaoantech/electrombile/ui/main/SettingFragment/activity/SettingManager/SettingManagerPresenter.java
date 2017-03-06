package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

/**
 * Created by yangxu on 2016/12/14.
 */

public class SettingManagerPresenter implements SettingManagerContract.Presenter{
    private  SettingManagerContract.View mSettingManagerView;

    protected SettingManagerPresenter(SettingManagerContract.View settingManagerView){
        this.mSettingManagerView = settingManagerView;
        mSettingManagerView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

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
    public void logout() {
        mSettingManagerView.logout();
    }
}
