package com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory;

import android.util.Log;

import com.xiaoantech.electrombile.event.http.HttpGetEvent;
import com.xiaoantech.electrombile.manager.HistoryRouteManager;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.model.GPSPointModel;
import com.xiaoantech.electrombile.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by yangxu on 2016/11/25.
 */

public class MapListPresenter implements MapListContract.Presenter{
    private final static String TAG = "MapListPresenter";
    private MapListContract.View    mMapListView;
    private List<List<Map<String,String>>> mRouteList;

    private final String KET_LONG = "lon";
    private final String SPEED = "speed";
    private final String KET_LAT = "lat";
    private final String TIMESTAMP = "timestamp";

    protected MapListPresenter(MapListContract.View mapListView){
        this.mMapListView = mapListView;
        mMapListView.setPresenter(this);

        mRouteList = new ArrayList<>();

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
    public void getSevenDayRoute(int startIndex) {
        if (startIndex <0) startIndex = 0;
        if (startIndex >29) startIndex = 29;

        prepareRouteList(startIndex);
        Calendar calendar = Calendar.getInstance();
        GregorianCalendar gcStart = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        gcStart.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) - startIndex - 6, 0, 0, 0);

        Date startTime = gcStart.getTime();


        GregorianCalendar gcEnd = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        gcEnd.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) - startIndex + 1, 0, 0, 0);

        Date endTime = gcEnd.getTime();
        HistoryRouteManager.getInstance().getRouteInfoFormHttp(startTime.getTime()/1000,endTime.getTime()/1000);
    }

    @Override
    public void getGPSPoints(long startTimeStamp,long endTimeStamp){
        HistoryRouteManager.getInstance().getGPSPointsFromHttp(startTimeStamp,endTimeStamp);
    }

    private void prepareRouteList(int startIndex){
        if (mRouteList.size() < startIndex + 7){
            List<Map<String, String>> listmap;
            for(int i = 0;i<7;i++)
            {
                listmap = new ArrayList<>();
                mRouteList.add(listmap);
            }
        }else {
            List<Map<String, String>> listmap;
            for(int i = startIndex;i< startIndex + 7;i++)
            {
                listmap = new ArrayList<>();
                mRouteList.set(i,listmap);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpGetEvent event){
        try {
            if (event.getRequestType() == HttpManager.getType.GET_TYPE_ROUTES){
                dealWithRouteInfo(event.getResult());
            }else if (event.getRequestType() == HttpManager.getType.GET_TYPE_GPS_POINTS){
                dealWithGPSInfo(event.getResult());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mMapListView.refreshList(mRouteList);
        Log.d(TAG,mRouteList.toString());
    }

    private void dealWithRouteInfo(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (!jsonObject.has("itinerary")){
                //TODO:无数据
            }else {
                JSONArray routeArray = jsonObject.getJSONArray("itinerary");
                for (int i = 0;i<routeArray.length();i++){
                    JSONObject route = routeArray.getJSONObject(i);
                    JSONObject start = route.getJSONObject("start");
                    JSONObject end = route.getJSONObject("end");
                    long startTimeStamp = start.getLong("timestamp");
                    long endTimeStamp = end.getLong("timestamp");
                    if (startTimeStamp == endTimeStamp) {
                        continue;
                    }

                    int index = (int)(TimeUtil.getZeroTimeStamp() - startTimeStamp)/86400;
                    Map<String,String> map = new HashMap<>();
                    map.put("startTimestamp",start.getString("timestamp"));
                    map.put("startlat",start.getString("lat"));
                    map.put("startlon",start.getString("lon"));
                    map.put("endTimestamp",end.getString("timestamp"));
                    map.put("endlat",end.getString("lat"));
                    map.put("endlon",end.getString("lon"));
                    map.put("miles",route.getString("miles"));
                    mRouteList.get(index).add(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        mMapListView.refreshList(mRouteList);
        Log.d(TAG,mRouteList.toString());
    }

    private void dealWithGPSInfo(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("gps")){
                JSONArray jsonArray = jsonObject.getJSONArray("gps");
                ArrayList<GPSPointModel> points = new ArrayList<>();
                for (int i = 0; i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    double lat = object.getDouble(KET_LAT);
                    double lng = object.getDouble(KET_LONG);
                    int speed = object.getInt(SPEED);
                    long timestamp = object.getLong(TIMESTAMP);
                    GPSPointModel gpsPoint = new GPSPointModel(timestamp,lat,lng,speed);
                    points.add(gpsPoint);
                }
                mMapListView.gotoPlayHistory(points);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
