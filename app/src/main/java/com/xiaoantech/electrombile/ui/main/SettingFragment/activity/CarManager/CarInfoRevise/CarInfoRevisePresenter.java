package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise;


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

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void confirmModification() {

    }
}
