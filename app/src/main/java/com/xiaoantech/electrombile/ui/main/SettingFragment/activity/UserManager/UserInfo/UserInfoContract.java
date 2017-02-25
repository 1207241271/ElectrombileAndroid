package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfo;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/12/14.
 */

public interface UserInfoContract {
    interface View extends BaseView<Presenter> {
        void gotoUserInfoRevise();
    }

    interface Presenter extends BasePresenter {
        void gotoUserInfoRevise();
    }
}
