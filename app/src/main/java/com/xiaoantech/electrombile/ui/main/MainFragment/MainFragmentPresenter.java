package com.xiaoantech.electrombile.ui.main.MainFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.constant.EventBusConstant;
import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.cmd.BatteryEvent;
import com.xiaoantech.electrombile.event.cmd.FenceEvent;
import com.xiaoantech.electrombile.event.cmd.LocationEvent;
import com.xiaoantech.electrombile.event.cmd.StatusEvent;
import com.xiaoantech.electrombile.event.http.HttpEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.HistoryRouteManager;
import com.xiaoantech.electrombile.manager.HttpManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.JSONUtil;
import com.xiaoantech.electrombile.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxu on 2016/11/6.
 */

public class MainFragmentPresenter implements MainFragmentContract.Presenter,OnGetGeoCoderResultListener{
    private final static String TAG = "MainFragmentPresenter";
    private MainFragmentContract.View  mMainFragmentView;
    private GeoCoder                mSearch;
    private boolean fenceStatus;
    private JSONObject              mWeatherInfo;
    private String                  mPlaceInfo;
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

    protected MainFragmentPresenter(MainFragmentContract.View mainFragmentView){
        this.mMainFragmentView = mainFragmentView;
        mainFragmentView.setPresenter(this);

        fenceStatus = false;
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    public void getWeatherInfo() {
        String city = StringUtil.encode("武汉市");
        String urlStr = "http://wthrcdn.etouch.cn/weather_mini?city=%E6%AD%A6%E6%B1%89";
        HttpManager.getHttpResult(urlStr, HttpManager.getType.GET_TYPE_WEATHER);

    }

    @Override
    public void changeFenceStatus() {
        mMainFragmentView.showWaitingDialog("正在设置");
        if (fenceStatus){
            MqttPublishManager.getInstance().fenceOff(BasicDataManager.getInstance().getBindIMEI());
        }else {
            MqttPublishManager.getInstance().fenceOn(BasicDataManager.getInstance().getBindIMEI());
        }
    }

    @Override
    public void getBattery(){
        mMainFragmentView.showWaitingDialog("正在查询");
        MqttPublishManager.getInstance().getBattery(BasicDataManager.getInstance().getBindIMEI());
    }

    @Override
    public void getItinerary(){
        mMainFragmentView.showWaitingDialog("正在查询");
        HistoryRouteManager.getInstance().getTodayItineray();
    }

    @Override
    public void getGPSInfo(){
        MqttPublishManager.getInstance().getLocation(BasicDataManager.getInstance().getBindIMEI());
    }

    @Override
    public void gotoMap(){
        mMainFragmentView.gotoMap();
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
            mWeatherInfo = new JSONObject(weatherStr);
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
            if (itineraryObject.has("code")) {
                int code = itineraryObject.getInt("code");
                if (code != 0){
                    dealWithErrorCode(code);
                    mMainFragmentView.changeItinerary(0);
                }
            }else {
                JSONArray itineraryArray = itineraryObject.getJSONArray(HttpConstant.ITINERARY);
                if (null != itineraryArray) {
                    int totalItinerary = 0;
                    for (int i = 0; i < itineraryArray.length(); i++) {
                        JSONObject jsonObject = itineraryArray.getJSONObject(i);
                        totalItinerary += jsonObject.getInt(HttpConstant.ROUTEMILE);
                    }
                    mMainFragmentView.changeItinerary(totalItinerary / 1000);
                } else {
                    //TODO:
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        ReverseGeoCodeResult.AddressComponent addressComponent = result.getAddressDetail();
        mMainFragmentView.changePlaceInfo(addressComponent.district+addressComponent.street+addressComponent.streetNumber);
        mPlaceInfo = addressComponent.province + "·" + addressComponent.city;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void showWeatherInfo(){
        mMainFragmentView.showWeatherDialog(mWeatherInfo,mPlaceInfo);
    }

    @Override
    public void changeCar() {
        mMainFragmentView.changeCar();
    }

    @Override
    public void gotoMessage() {
        mMainFragmentView.gotoMessage();
    }

    @Override
    public List<Map<String, Object>> getCarListInfo() {
        List<String> carNameList = BasicDataManager.getInstance().getIMEIList();

        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("carName",carNameList.get(0));
        map.put("isSelected", R.drawable.btn_selected);
        list.add(map);

        for (int i = 1;i < carNameList.size();i++){
            map = new HashMap<>();
            map.put("carName",carNameList.get(i));
            map.put("isSelected",R.drawable.btn_unselected);
            list.add(map);
        }

        return list;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFenceEvent(FenceEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try{
            int code = jsonObject.getInt("code");
            if (code != 0){
                dealWithErrorCode(code);
                return;
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
            if (code !=0 && code != 103){
                dealWithHTTPErrorCode(code);
            }else if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_BATTERY){
                JSONObject result = jsonObject.getJSONObject("result");
                mMainFragmentView.changeBattery(result.getInt("percent"),true);
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

    private void dealWithHTTPErrorCode(int errorCode){
        if (errorCode == 100){
            mMainFragmentView.showToast("服务器内部错误!");
        }else  if (errorCode == 102){
            mMainFragmentView.showToast("无内容，请检查设备！");
            //TODO:ErrorShow
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try {
            int code = jsonObject.getInt("code");
            if (code != 0){
                dealWithErrorCode(code);
            }else{
                JSONObject result = jsonObject.getJSONObject("result");
                double lat = result.getDouble("lat");
                double lng = result.getDouble("lng");
                LatLng point = new LatLng(lat,lng);
                mMainFragmentView.changeGPSPoint(point);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatusEvent(StatusEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try {
            int code = jsonObject.getInt("code");
            if (code != 0){
                dealWithErrorCode(code);
            }else {
                JSONObject result = jsonObject.getJSONObject("result");
                //GPS定位
                JSONObject gps = result.getJSONObject("gps");
                double lat = gps.getDouble("lat");
                double lng = gps.getDouble("lng");
                LatLng point = new LatLng(lat,lng);
                mMainFragmentView.changeGPSPoint(point);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
                //小安宝状态
                boolean lock = result.getBoolean("lock");
                if (lock){
                    mMainFragmentView.changeFenceStatus(true,true);
                    fenceStatus = true;
                }else {
                    mMainFragmentView.changeFenceStatus(false,true);
                    fenceStatus = false;
                }
                //自动落锁状态
                JSONObject autoLock = result.getJSONObject("autolock");
                boolean autolockState = autoLock.getBoolean("isOn");

                //电池电量
                JSONObject battery = result.getJSONObject("battery");
                mMainFragmentView.changeBattery(battery.getInt("percent"),false);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }




}
