package com.xiaoantech.electrombile.ui.main.MainFragment;

import com.baidu.mapapi.model.LatLng;
import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by yangxu on 2016/11/5.
 */

public interface MainFragmentContract {

    interface View extends BaseView<Presenter>{
        void showWeather(int temperature,String weather);
        void changeFenceStatus(Boolean isOn,boolean isGet);
        void changeBattery(int battery,boolean showTip);
        void changeItinerary(int itinerary);
        void changeGPSPoint(LatLng point);
        void changePlaceInfo(String placeInfo);
        void changeCar();
        void gotoMap();
        void showWeatherDialog(JSONObject weatherInfo,String placeInfo);
        void gotoMessage();
    }

    interface Presenter extends BasePresenter{
        void showWeatherInfo();

        List<Map<String,Object>> getCarListInfo();

        void changeFenceStatus();

        void getBattery();

        void getItinerary();

        void getGPSInfo();

        void gotoMap();

        void changeCar();

        void gotoMessage();

        void getWeatherInfo();
    }
}
