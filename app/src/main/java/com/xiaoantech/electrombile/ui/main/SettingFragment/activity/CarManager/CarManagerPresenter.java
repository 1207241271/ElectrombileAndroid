package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager;

import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.SettingManagerContract;

/**
 * Created by yangxu on 2016/12/14.
 */

public class CarManagerPresenter implements CarManagerContract.Presenter{
    private final static String TAG = "SettingManagerPresenter";
    private CarManagerContract.View mCarManagerView;

    protected  CarManagerPresenter(CarManagerContract.View carManagerView){
        this.mCarManagerView = carManagerView;
        carManagerView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void addDevice() {
        mCarManagerView.addDevice();
    }

    @Override
    public void gotoBindedCarInfo() {
        mCarManagerView.gotoBindedCarInfo(0);
    }
}
