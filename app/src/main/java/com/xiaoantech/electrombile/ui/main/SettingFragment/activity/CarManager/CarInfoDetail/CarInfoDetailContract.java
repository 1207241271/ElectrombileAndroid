package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/12/21.
 */

public interface CarInfoDetailContract {

    interface View extends BaseView<Presenter>{
        void gotoRevise();
    }

    interface Presenter extends BasePresenter{
        void gotoRevise();
    }

}
