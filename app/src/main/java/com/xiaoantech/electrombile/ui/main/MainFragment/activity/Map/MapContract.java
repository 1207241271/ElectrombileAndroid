package com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map;

import com.baidu.mapapi.model.LatLng;
import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;
import com.xiaoantech.electrombile.constant.LayoutConstant;

/**
 * Created by yangxu on 2016/11/15.
 */

public interface MapContract {

    interface View extends BaseView<Presenter>{


        void changeGPSPoint(LatLng point);

        void changePlaceInfo(String placeInfo);

        void changeDateInfo(String timeDate);

        void changeMapType();

        void gotoMapList();

        void gotoFindCar();
    }


    interface Presenter extends BasePresenter{
        void refreshLocation();

        void changeMapType();

        void gotoMapList();

        void gotoFindCar();
    }


}
