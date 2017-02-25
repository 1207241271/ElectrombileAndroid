package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail;

/**
 * Created by yangxu on 2016/12/21.
 */

public class CarInfoDetailPresenter implements CarInfoDetailContract.Presenter{
    private final static String TAG = "CarInfoDetailPresenter";
    private CarInfoDetailContract.View mCarInfoDetailView;

    protected CarInfoDetailPresenter(CarInfoDetailContract.View carInfoDetailView){
        this.mCarInfoDetailView = carInfoDetailView;
        carInfoDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void gotoRevise() {
        mCarInfoDetailView.gotoRevise();
    }
}
