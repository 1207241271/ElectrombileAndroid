package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by 73843 on 2017/3/18.
 */

public interface PhoneAlarmAgreementContract {

    interface View extends BaseView<Presenter> {
        void toPhoneAlarmActivity(boolean isTo);
    }

    interface Presenter extends BasePresenter {
        void setPhoneAlarmPhone();
    }
}
