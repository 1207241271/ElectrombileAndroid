package com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;
import com.xiaoantech.electrombile.model.GPSPointModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxu on 2016/11/25.
 */

public class MapListContract {
    interface View extends BaseView<Presenter>{
        void refreshList(List<List<Map<String,String>>> routeList);
        void gotoPlayHistory(ArrayList<GPSPointModel> gpsPointModels);
    }

    interface Presenter extends BasePresenter{
        void getSevenDayRoute(int startIndex);
        void getGPSPoints(long startTimeStamp,long endTimeStamp);
    }
}
