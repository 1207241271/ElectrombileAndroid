package com.xiaoantech.electrombile.ui.main.SettingFragment;

import com.xiaoantech.electrombile.ui.main.MainFragment.MainFragmentContract;

/**
 * Created by yangxu on 2016/12/13.
 */

public class SettingFragmentPresenter implements SettingFragmentContract.Presenter{
    private final static String TAG = "SettingFragmentPresenter";
    private SettingFragmentContract.View    mSettingFragmentView;

    protected  SettingFragmentPresenter(SettingFragmentContract.View settingFragmentView){
        this.mSettingFragmentView = settingFragmentView;
        mSettingFragmentView.setPresenter(this);
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void gotoCarManager() {
        mSettingFragmentView.gotoCarManager();
    }

    @Override
    public void gotoUserManager() {
        mSettingFragmentView.gotoUserManager();
    }

    @Override
    public void gotoMapDownload() {
        mSettingFragmentView.gotoMapDownload();
    }

    @Override
    public void gotoSettingManager() {
        mSettingFragmentView.gotoSettingManager();
    }

    @Override
    public void gotoAboutUs() {
        mSettingFragmentView.gotoAboutUs();
    }
}
