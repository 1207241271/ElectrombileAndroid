package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCarMap;

import android.location.LocationManager;

import com.baidu.mapapi.model.LatLng;
import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/1/5.
 */

public interface FindCarMapContract  {

    interface View extends BaseView<Presenter>{
        boolean isGPSOpen(LocationManager locationManager);
        void setUserPosition(LatLng latLng);
        void gotoFindCar();
    }

    interface Presenter extends BasePresenter{
        void gotoFindCarView();
        void getPhoneLocation();
        void setLocationManager(LocationManager locationManager);
    }
}
