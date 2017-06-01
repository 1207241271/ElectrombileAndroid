package com.xiaoantech.electrombile.manager;

import com.xiaoantech.electrombile.http.HttpManager;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by yangxu on 2016/11/13.
 */

public class HistoryRouteManager {
    public static HistoryRouteManager  mInstance;

    public static HistoryRouteManager getInstance() {
        if (null == mInstance){
            mInstance = new HistoryRouteManager();
        }
        return mInstance;
    }

    public  void getTodayItineray(){
        Calendar can = Calendar.getInstance();
        GregorianCalendar gcStart = new GregorianCalendar(TimeZone.getTimeZone("GMT+08:00"));
        gcStart.set(can.get(Calendar.YEAR), can.get(Calendar.MONTH),
                can.get(Calendar.DAY_OF_MONTH) , 0, 0, 0);
        Date zero = gcStart.getTime();
        long startTime = zero.getTime()/1000;
        long endTime = startTime+86400;
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/itinerary/"+BasicDataManager.getInstance().getBindIMEI()+"?start="+startTime+"&end="+endTime;
        HttpManager.getHttpResult(url, HttpManager.getType.GET_TYPE_TODAYITINERARY);
    }

    public void getRouteInfoFormHttp(long startTimestamp,long endTimestamp){
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/itinerary/"+BasicDataManager.getInstance().getBindIMEI()+"?start="+startTimestamp+"&end="+endTimestamp;
        HttpManager.getHttpResult(url, HttpManager.getType.GET_TYPE_ROUTES);
    }

    public void getGPSPointsFromHttp(long startTimestamp,long endTimestamp){
        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/history/"+BasicDataManager.getInstance().getBindIMEI()+"?start="+startTimestamp+"&end="+endTimestamp;
        HttpManager.getHttpResult(url,HttpManager.getType.GET_TYPE_GPS_POINTS);
    }
}
