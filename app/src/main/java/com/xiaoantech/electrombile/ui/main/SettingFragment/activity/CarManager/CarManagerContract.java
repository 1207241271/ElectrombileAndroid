package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/12/14.
 */

public interface CarManagerContract {
    interface View extends BaseView<Presenter> {
        void addDevice();
        void gotoBindedCarInfo(int index);
    }

    interface Presenter extends BasePresenter {
        void addDevice();
        void gotoBindedCarInfo();
    }
}
