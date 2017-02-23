package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/1/4.
 */

public interface ChangePassContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void changePassword(String pass1,String pass2);
    }
}
