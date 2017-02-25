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
    public void gotoChangePass() {
        mSettingManagerView.gotoChangePass();
    }
}
