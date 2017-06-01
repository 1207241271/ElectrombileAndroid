package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangxu on 2017/1/3.
 */

public class CarInfoRevisePresenter implements CarInfoReviseContract.Presenter {
    private final static String TAG = "CarInfoRevisePresenter";
    private CarInfoReviseContract.View mCarInfoReviseView;

    protected CarInfoRevisePresenter(CarInfoReviseContract.View carInfoReviseView){
        this.mCarInfoReviseView = carInfoReviseView;
        carInfoReviseView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void changeBatteryType() {

    }

    @Override
    public void confirmModification() {

    }
}
