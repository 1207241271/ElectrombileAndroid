package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/2/25.
 */

public interface PhoneAlarmContract {

    interface View extends BaseView<Presenter>{
        void gotoResendActivity();
        void changeCutDownStatus(int num);
        void finishActivity();
    }

    interface Presenter extends BasePresenter{
        void phoneAlarmTest();
        void phoneAlarmUnreceived();
        void phoneAlarmDelete();
        void AddCallerIndex();
    }
}
