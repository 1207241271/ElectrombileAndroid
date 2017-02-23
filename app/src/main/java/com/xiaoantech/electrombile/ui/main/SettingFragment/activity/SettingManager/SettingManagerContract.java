package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/12/14.
 */

public interface SettingManagerContract {

    interface View extends BaseView<Presenter> {
        void gotoChangePass();
    }

    interface Presenter extends BasePresenter{
        void gotoChangePass();
    }
}
