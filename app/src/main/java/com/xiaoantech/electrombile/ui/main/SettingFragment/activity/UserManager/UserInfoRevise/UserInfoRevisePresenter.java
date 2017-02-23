package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise;

import android.net.Uri;

import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise.CarInfoReviseContract;

/**
 * Created by yangxu on 2017/1/3.
 */

public class UserInfoRevisePresenter implements UserInfoReviseContract.Presenter {
    private final static String TAG = "UserInfoRevisePresenter";
    private UserInfoReviseContract.View mUserInfoReviseView;

    protected UserInfoRevisePresenter(UserInfoReviseContract.View userInfoReviseView){
        this.mUserInfoReviseView = userInfoReviseView;
        mUserInfoReviseView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void changeIcon() {
        mUserInfoReviseView.changeIcon();
    }

    @Override
    public void chooseSex() {
        mUserInfoReviseView.chooseSex();
    }

    @Override
    public void chooseBirthDate() {
        mUserInfoReviseView.chooseBirthDate();
    }

    @Override
    public void confirmModify() {
        mUserInfoReviseView.confirmModify();
    }

}
