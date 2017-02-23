package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfo;


import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail.CarInfoDetailContract;

/**
 * Created by yangxu on 2016/12/14.
 */

public class UserInfoPresenter implements UserInfoContract.Presenter {
    private final static String TAG = "UserInfoPresenter";
    private UserInfoContract.View mUserInfoView;

    protected UserInfoPresenter(UserInfoContract.View userInfoView){
        this.mUserInfoView = userInfoView;
        mUserInfoView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void gotoUserInfoRevise() {
        mUserInfoView.gotoUserInfoRevise();
    }
}
