package com.xiaoantech.electrombile.manager;

import android.os.Bundle;

import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.http.HttpGetEvent;
import com.xiaoantech.electrombile.event.http.HttpPostEvent;
import com.xiaoantech.electrombile.event.http.HttpPutEvent;
import com.xiaoantech.electrombile.event.http.HttpDeleteEvent;
import com.xiaoantech.electrombile.utils.StreamToStringUtil;
import com.xiaoantech.electrombile.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yangxu on 2016/11/7.
 */

public class HttpManager {
    public enum getType{
        GET_TYPE_WEATHER,
        GET_TYPE_TODAYITINERARY,
        GET_TYPE_ROUTES,
        GET_TYPE_GPS_POINTS,
    }
    public enum postType{
        POST_TYPE_DEVICE,
        POST_TYPE_PHONE
    }
    public enum putType{
        PUT_TYPE_ALARMPHONE
    }
    public enum deleteType{
    }
    public static void getHttpResult(final String url, final getType gettype){
        new Thread(new Runnable(){
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                    EventBus.getDefault().post(new HttpGetEvent(gettype,StringUtil.decodeUnicode(result),true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void putHttpResult(final String url, final putType puttype, final String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                byte[] bytes = body.getBytes();
                try {
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("PUT");
                    connection.setConnectTimeout(5000);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream out = connection.getOutputStream();
                    out.write(bytes);
                    out.flush();
                    out.close();
                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK) {
                        String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                        EventBus.getDefault().post(new HttpPutEvent(puttype, StringUtil.decodeUnicode(result.toString()), true));
                    }else {
                        connection.disconnect();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void deleteHttpResult(final String url, final deleteType deletetype, final String body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                byte[] bytes = body.getBytes();
                try {
                    URL getURL = new URL(url);
                    connection = (HttpURLConnection) getURL.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("DELETE");
                    connection.setConnectTimeout(5000);
                    connection.setUseCaches(false);        //设置不进行缓存
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    OutputStream os = connection.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK){
                        String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                        EventBus.getDefault().post(new HttpDeleteEvent(deletetype,StringUtil.decodeUnicode(result),true));
                    }else {
                        connection.disconnect();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static void postHttpResult(final String url, final postType postType , final HttpConstant.HttpCmd cmd, final String body){
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes = body.getBytes();
                HttpURLConnection connection;
                try{
                    URL postURL = new URL(url);
                    connection = (HttpURLConnection) postURL.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setRequestProperty("Content-Type","application/json");
                    connection.setRequestProperty("Content-Length",String.valueOf(bytes.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();
                    outputStream.close();
                    int response = connection.getResponseCode();
                    if (response == HttpURLConnection.HTTP_OK){
                        String result = StreamToStringUtil.StreamToString(connection.getInputStream());
                        EventBus.getDefault().post(new HttpPostEvent(postType,StringUtil.decodeUnicode(result),true,cmd));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
