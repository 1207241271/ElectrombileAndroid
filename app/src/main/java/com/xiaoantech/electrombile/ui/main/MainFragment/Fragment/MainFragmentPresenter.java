package com.xiaoantech.electrombile.ui.main.MainFragment.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.MainThread;

import com.xiaoantech.electrombile.constant.EventBusConstant;
import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.cmd.BatteryEvent;
import com.xiaoantech.electrombile.event.cmd.FenceEvent;
import com.xiaoantech.electrombile.event.http.HttpEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.HistoryRouteManager;
import com.xiaoantech.electrombile.manager.HttpManager;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.JSONUtil;
import com.xiaoantech.electrombile.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangxu on 2016/11/6.
 */

public class MainFragmentPresenter implements MainFragmentConstract.Presenter{
    private final static String TAG = "MainFragmentPresenter";
    private MainFragmentConstract.View  mMainFragmentView;
    private boolean fenceStatus;
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case weatherMessage:
                    Bundle bundle = msg.getData();
                    mMainFragmentView.showWeather(bundle.getInt("temperature"),bundle.getString("weather"));
            }
            super.handleMessage(msg);
        }
    };

    private final int weatherMessage = 101;

    protected MainFragmentPresenter(MainFragmentConstract.View mainFragmentView){
        subscribe();
        this.mMainFragmentView = mainFragmentView;
        mainFragmentView.setPresenter(this);
        fenceStatus = false;
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
    public void getWeatherInfo() {
        String city = StringUtil.encode("武汉市");
        String urlStr = "http://wthrcdn.etouch.cn/weather_mini?city=%E6%AD%A6%E6%B1%89";
        HttpManager.getHttpResult(urlStr, HttpManager.getType.GET_TYPE_WEATHER);
    }

    @Override
    public void changeFenceStatus() {
        mMainFragmentView.showWaitingDialog("正在设置");
        if (fenceStatus){
            MqttPublishManager.getInstance().fenceOff(BasicDataManager.getInstance().getIMEI());
        }else {
            MqttPublishManager.getInstance().fenceOn(BasicDataManager.getInstance().getIMEI());
        }
    }

    @Override
    public void getBattery(){
        mMainFragmentView.showWaitingDialog("正在查询");
        MqttPublishManager.getInstance().getBattery(BasicDataManager.getInstance().getIMEI());
    }

    @Override
    public void getItinerary(){
        HistoryRouteManager.getInstance().getTodayItineray();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpEvent event){
        if (event.getRequestType() == HttpManager.getType.GET_TYPE_WEATHER){
            didReceiveWeather(event.getResult());
        }else if (event.getRequestType() == HttpManager.getType.GET_TYPE_TODAYITINERARY){
            didReceiveItinerary(event.getResult());
        }
    }

    private void didReceiveWeather(String weatherStr){
        try{
            String desc = JSONUtil.ParseJSON(weatherStr,"desc");
            if (null != desc && desc.equals("OK")){
                String data = JSONUtil.ParseJSON(weatherStr,"data");
                String temperature = JSONUtil.ParseJSON(data,"wendu");
                String forecast = JSONUtil.ParseJSON(data,"forecast");
                JSONArray jsonArray = new JSONArray(forecast);
                String type = "";
                if (jsonArray.length()>0){
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    type = jsonObject.optString("type");

                }
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("temperature",Integer.parseInt(temperature));
                bundle.putString("weather",type);
                message.setData(bundle);
                message.what = 101;
                myHandler.sendMessage(message);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void didReceiveItinerary(String itinerary){
        try{
            JSONObject itineraryObject = new JSONObject(itinerary);
            JSONArray itineraryArray = itineraryObject.getJSONArray(HttpConstant.ITINERARY);
            if (null != itineraryArray) {
                int totalItinerary = 0;
                for (int i = 0; i < itineraryArray.length(); i++) {
                    JSONObject jsonObject = itineraryArray.getJSONObject(i);
                    totalItinerary += jsonObject.getInt(HttpConstant.ROUTEMILE);
                }
                mMainFragmentView.changeItinerary(totalItinerary/1000);
            }else {
                //TODO:
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFenceEvent(FenceEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try{
            int code = jsonObject.getInt("code");
            if (code !=0){
                dealWithErrorCode(code);
            }

            switch (event.getCmdType()){
                case CMD_TYPE_FENCE_ON:
                    mMainFragmentView.changeFenceStatus(true,false);
                    fenceStatus = true;
                    break;
                case CMD_TYPE_FENCE_OFF:
                    fenceStatus = false;
                    mMainFragmentView.changeFenceStatus(false,false);
                    break;
                case CMD_TYPE_FENCE_GET:
                    JSONObject result = jsonObject.getJSONObject("result");
                    if (result.getInt("state") == 0){
                        fenceStatus = true;
                        mMainFragmentView.changeFenceStatus(true,true);
                    }else {
                        fenceStatus = false;
                        mMainFragmentView.changeFenceStatus(false,true);
                    }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onBatteryEvent(BatteryEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try{
            int code = jsonObject.getInt("code");
            if (code !=0){
                dealWithErrorCode(code);
            }

            if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_BATTERY){
                JSONObject result = jsonObject.getJSONObject("result");
                mMainFragmentView.changeBattery(result.getInt("percent"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void dealWithErrorCode(int errorCode){
        if (errorCode == 100){
            mMainFragmentView.showToast("服务器内部错误!");
        }else  if (errorCode == 102){
            mMainFragmentView.showToast("设备离线，请检查设备！");
            //TODO:ErrorShow
        }
    }


}
