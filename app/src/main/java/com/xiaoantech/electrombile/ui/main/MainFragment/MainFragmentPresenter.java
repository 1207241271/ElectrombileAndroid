package com.xiaoantech.electrombile.ui.main.MainFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

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
import com.xiaoantech.electrombile.event.http.HttpGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostFenceSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGPSEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGSMSignalEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostLockSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostStatusEvent;
import com.xiaoantech.electrombile.http.HttpPublishManager;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.HistoryRouteManager;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.ErrorCodeConvertUtil;
import com.xiaoantech.electrombile.utils.GPSConvertUtil;
import com.xiaoantech.electrombile.utils.JSONUtil;
import com.xiaoantech.electrombile.utils.StringUtil;
import com.xiaoantech.electrombile.utils.TimeUtil;

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
    private boolean lockStatus;
    private JSONObject              mWeatherInfo;
    private String                  mPlaceInfo;
    private String                  mCity;
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

        fenceStatus = LocalDataManager.getInstance().getFenceStatus();
        lockStatus = LocalDataManager.getInstance().getLockStatus();
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
        if (mCity != null){
            String city = StringUtil.encode(mCity);
            String urlStr = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
            HttpManager.getHttpResult(urlStr, HttpManager.getType.GET_TYPE_WEATHER);
        }
    }

    @Override
    public void refresh() {
        mMainFragmentView.showWaitingDialog("正在刷新");
        HttpPublishManager.getInstance().getStatus();
        HistoryRouteManager.getInstance().getTodayItineray();
        getWeatherInfo();
        getGSM();
    }

    private void getGSM(){
        HttpPublishManager.getInstance().getGSMSignal();
        mMainFragmentView.hideWaitingDialog();
    }

    @Override
    public void changeFenceStatus() {
        mMainFragmentView.showWaitingDialog("正在设置");
        if (fenceStatus){
            HttpPublishManager.getInstance().setFenceOff();
        }else {
            HttpPublishManager.getInstance().setFenceOn();
        }
    }

    @Override
    public void changeLockStatus() {
        mMainFragmentView.showWaitingDialog("正在设置");
        if (lockStatus)
            HttpPublishManager.getInstance().setLockOn();
        else
            HttpPublishManager.getInstance().setLockOn();

    }

    @Override
    public void getItinerary(){
        mMainFragmentView.showWaitingDialog("正在查询");
        HistoryRouteManager.getInstance().getTodayItineray();
    }

    @Override
    public void getGPSInfo(){
        HttpPublishManager.getInstance().getGPS();
    }

    @Override
    public void gotoMap(){
        mMainFragmentView.gotoMap();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpGetEvent event){
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
        if (addressComponent.countryCode != -1) {
            mMainFragmentView.changePlaceInfo(addressComponent.district + addressComponent.street + addressComponent.streetNumber);
            mPlaceInfo = addressComponent.province + "·" + addressComponent.city;

            //获取天气信息
            mCity = addressComponent.city.substring(0, addressComponent.city.length() - 1);
            getWeatherInfo();
        }
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
    public void gotoHistory() {
        mMainFragmentView.gotoHistory();
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
    public void onHttpPostFenceSetEvent(HttpPostFenceSetEvent event){
        if (event.getCode() == 0){
            if (event.getPostType() == HttpManager.postType.POST_TYPE_FENCE_SET_ON) {
                fenceStatus = true;
            }else if (event.getPostType() == HttpManager.postType.POST_TYPE_FENCE_SET_OFF){
                fenceStatus = false;
            }
            mMainFragmentView.changeFenceStatus(fenceStatus,false);
        }else {
            dealWithHTTPErrorCode(event.getCode());
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onFenceEvent(FenceEvent event){
//        JSONObject jsonObject = event.getJsonObject();
//        try{
//            int code = jsonObject.getInt("code");
//            if (code != 0){
//                dealWithErrorCode(code);
//                return;
//            }
//
//            mMainFragmentView.changeBackground(true);
//            switch (event.getCmdType()){
//                case CMD_TYPE_FENCE_ON:
//                    mMainFragmentView.changeFenceStatus(true,false);
//                    fenceStatus = true;
//                    break;
//                case CMD_TYPE_FENCE_OFF:
//                    fenceStatus = false;
//                    mMainFragmentView.changeFenceStatus(false,false);
//                    break;
//                case CMD_TYPE_FENCE_GET:
//                    JSONObject result = jsonObject.getJSONObject("result");
//                    if (result.getInt("state") == 0){
//                        fenceStatus = true;
//                        mMainFragmentView.changeFenceStatus(true,true);
//                    }else {
//                        fenceStatus = false;
//                        mMainFragmentView.changeFenceStatus(false,true);
//                    }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

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
            mMainFragmentView.changeBackground(false);
            //TODO:ErrorShow
        }
    }

    private void dealWithHTTPErrorCode(int errorCode){
        mMainFragmentView.showToast(ErrorCodeConvertUtil.getHttpErrorStrWithCode(errorCode));
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onHttpPostGPSEvent(HttpPostGPSEvent event){
        if (event.getCode() == 0) {
            if (event.getisGPS() == true){
                LatLng point = new LatLng(event.getLat(), event.getLng());
                Log.e("LatLng", "Lat:"+event.getLat()+"Lng"+event.getLng());
                mMainFragmentView.changeGPSPoint(GPSConvertUtil.convertFromCommToBdll09(point));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            }else {
                HttpPublishManager.getInstance().getCell(event.getMcc(), event.getMnc(), event.getLac(), event.getCi());
            }
        }else {
            dealWithErrorCode(event.getCode());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostStatusEvent(HttpPostStatusEvent event){
        if (event.getCode() == 0){
            mMainFragmentView.changeBackground(true);
            LocalDataManager.getInstance().setLatestStatus(event.getString());
            convertStatusFromString(event.getString());
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

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void setStatusFromString(String string) {
        convertStatusFromString(string);
    }

    private void convertStatusFromString(String string){
        try {
            JSONObject result = new JSONObject(string);
            //小安宝状态
            int lock = result.getInt("defend");
            if (lock == 1){
                mMainFragmentView.changeFenceStatus(true,true);
                fenceStatus = true;            }else {
                mMainFragmentView.changeFenceStatus(false,true);
                fenceStatus = false;
            }
            //自动落锁状态
            JSONObject autoLock = result.getJSONObject("autolock");
            int autolockState = autoLock.getInt("sw");
            if(autolockState == 1){
                int autoLockPeriod = autoLock.getInt("period");
                mMainFragmentView.changeAutoLockStatus(true,autoLockPeriod);
            }else {
                mMainFragmentView.changeAutoLockStatus(false,0);
            }
            //电池电量
            JSONObject battery = result.getJSONObject("battery");
            mMainFragmentView.changeBattery(battery.getInt("percent"),false);

            //GPS定位
            if (result.has("gps")) {
                JSONObject gps = result.getJSONObject("gps");
                double lat = gps.getDouble("lat");
                double lng = gps.getDouble("lng");
                LatLng point = new LatLng(lat, lng);
                LatLng newPoint = GPSConvertUtil.convertFromCommToBdll09(point);
                mMainFragmentView.changeGPSPoint(newPoint);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(newPoint));
                mMainFragmentView.setGPSSignal(true);
            }else {
                mMainFragmentView.setGPSSignal(false);
                if (result.has("cell")){
                    JSONObject cell = result.getJSONObject("cell");
                    int mcc = cell.getInt("mcc");
                    int mnc = cell.getInt("mnc");
                    int lac = cell.getInt("lac");
                    int ci = cell.getInt("ci");
                    HttpPublishManager.getInstance().getCell(mcc, mnc, lac, ci);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpGetEvent(HttpGetEvent event){
        if (event.isSuccess() == true && event.getRequestType() == HttpManager.getType.GET_TYPE_CELL){
            try {
                JSONObject result = new JSONObject(event.getResult());
                double lat = result.getDouble("lat");
                double lon = result.getDouble("lon");
                LatLng point = new LatLng(lat, lon);
                LatLng newPoint = GPSConvertUtil.convertFromCommToBdll09(point);
                mMainFragmentView.changeGPSPoint(newPoint);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(newPoint));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostLockSetEvent(HttpPostLockSetEvent event){
        if (event.getCode() == 0){
            lockStatus = !lockStatus;
            mMainFragmentView.changeBackground(true);
            LocalDataManager.getInstance().setLockStatus(lockStatus);
            mMainFragmentView.changeLockStatus(lockStatus);
            mMainFragmentView.showToast("设置成功");
        }else {
            dealWithErrorCode(event.getCode());
        }
    }


    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onHttpPostGSMSignalEvent(HttpPostGSMSignalEvent event){
        if (event.getCode() == 0){
            int signal =  event.getGSMSignal();
            int level = 0;
            if (signal >= 4 && signal < 6){
                level = 1;
            }else if (signal < 9){
                level = 2;
            }else if (signal < 11){
                level = 3;
            }else {
                level = 4;
            }
            mMainFragmentView.changeSignal(level);
        }
    }
}
