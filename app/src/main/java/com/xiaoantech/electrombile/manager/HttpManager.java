package com.xiaoantech.electrombile.manager;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xiaoantech.electrombile.event.http.HttpEvent;
import com.xiaoantech.electrombile.utils.StreamToStringUtil;
import com.xiaoantech.electrombile.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by yangxu on 2016/11/7.
 */

public class HttpManager {
    public enum getType{
        GET_TYPE_WEATHER,
        GET_TYPE_TODAYITINERARY,
        GET_TYPE_ROUTES,
        GET_TYPE_GPS_POINTS
    }
    public static void getHttpResult(String url, final getType type){
        final String Url = url;
        new Thread(new Runnable(){
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    URL getURL = new URL(Url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new HttpEvent(type,StringUtil.decodeUnicode(result),true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
