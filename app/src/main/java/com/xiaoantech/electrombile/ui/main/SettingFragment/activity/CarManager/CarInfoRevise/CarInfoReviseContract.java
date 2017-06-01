package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/1/3.
 */

public interface CarInfoReviseContract {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void confirmModification();
        void changeBatteryType();
    }
}
