package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCar;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/1/6.
 */

public interface FindCarContract {

    interface View extends BaseView<Presenter>{
        void changeIntensity(int intensity);
    }

    interface Presenter extends BasePresenter{

    }
}
