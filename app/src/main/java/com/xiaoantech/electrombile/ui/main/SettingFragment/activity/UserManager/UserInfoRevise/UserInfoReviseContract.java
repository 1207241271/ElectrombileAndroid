package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/1/3.
 */

public interface UserInfoReviseContract {
    interface View extends BaseView<Presenter> {
        void changeIcon();
        void chooseSex();
        void chooseBirthDate();

        void confirmModify();
    }

    interface Presenter extends BasePresenter {
        void changeIcon();
        void chooseSex();
        void chooseBirthDate();

        void confirmModify();
    }
}
