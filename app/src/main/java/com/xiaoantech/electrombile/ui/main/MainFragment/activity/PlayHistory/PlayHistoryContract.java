package com.xiaoantech.electrombile.ui.main.MainFragment.activity.PlayHistory;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/11/27.
 */

public interface PlayHistoryContract {

    interface View extends BaseView<Presenter>{
        void switchPlayStatus();
        void switchPlaySpeed();
    }
    interface Presenter extends BasePresenter{
        void switchPlayStatus();
        void switchPlaySpeed();
    }
}
