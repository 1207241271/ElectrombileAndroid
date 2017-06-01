package com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.constant.LayoutConstant;
import com.xiaoantech.electrombile.constant.MqttCommonConstant;
import com.xiaoantech.electrombile.event.cmd.LocationEvent;
import com.xiaoantech.electrombile.event.http.HttpGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGPSEvent;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.http.HttpPublishManager;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.GPSConvertUtil;
import com.xiaoantech.electrombile.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by yangxu on 2016/11/15.
 */

public class MapPresenter implements MapContract.Presenter,OnGetGeoCoderResultListener{
    private final static String TAG = "MapActivitypresenter";
    private MapContract.View   mMapView;
    private GeoCoder mSearch;

    protected MapPresenter(MapContract.View mapActivity){
        this.mMapView = mapActivity;
        mMapView.setPresenter(this);

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

    @Override
    public void refreshLocation() {
        mMapView.showWaitingDialog("正在查询");
        HttpPublishManager.getInstance().getGPS();

    }

    @Override
    public void getLatestHistoryLocation() {
        String url = LocalDataManager.getInstance().getHTTPHost()+ ":" +LocalDataManager.getInstance().getHTTPPort() + "/v1/history/"+BasicDataManager.getInstance().getBindIMEI();
        HttpManager.getHttpResult(url, HttpManager.getType.GET_TYPE_GPS_POINTS);
    }

    @Override
    public void changeMapType(){
        mMapView.changeMapType();
    }

    @Override
    public void gotoMapList(){
        mMapView.gotoMapList();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent event){
        JSONObject jsonObject = event.getJsonObject();
        try {
            int code = jsonObject.getInt("code");
            if (code != MqttCommonConstant.CODE_SUCCESS && code != MqttCommonConstant.CODE_WAITING){
                dealWithErrorCode(code);
            }else{
                if (code == MqttCommonConstant.CODE_SUCCESS)
                    mMapView.showToast("查询成功");
                JSONObject result = jsonObject.getJSONObject("result");
                double lat = result.getDouble("lat");
                double lng = result.getDouble("lng");
                long timestamp = result.getLong("timestamp");
                LatLng point = new LatLng(lat,lng);
                mMapView.changeGPSPoint(GPSConvertUtil.convertFromCommToBdll09(point));
                mMapView.changeDateInfo(TimeUtil.getMinuteStringFromTimeStamp(timestamp));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpGetEvent(HttpGetEvent event){
        if (event.getRequestType() == HttpManager.getType.GET_TYPE_GPS_POINTS){
            try{
                JSONObject jsonObject = new JSONObject(event.getResult());
                JSONArray array = jsonObject.getJSONArray("gps");
                if (array.length() > 0){
                    JSONObject gps = array.getJSONObject(0);
                    double lat = gps.getDouble("lat");
                    double lng = gps.getDouble("lon");
                    long timestamp = gps.getLong("timestamp");
                    LatLng point = new LatLng(lat,lng);
                    mMapView.changeGPSPoint(GPSConvertUtil.convertFromCommToBdll09(point));
                    mMapView.changeDateInfo(TimeUtil.getMinuteStringFromTimeStamp(timestamp));
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onHttpPostGPSEvent(HttpPostGPSEvent event){
        if (event.getCode() == 0) {
            LatLng point = new LatLng(event.getLat(), event.getLng());
            mMapView.changeGPSPoint(GPSConvertUtil.convertFromCommToBdll09(point));
            mMapView.changeDateInfo(TimeUtil.getMinuteStringFromTimeStamp(event.getTimestamp()));
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            mMapView.showToast("查询成功");
        }else {
            dealWithErrorCode(event.getCode());
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        ReverseGeoCodeResult.AddressComponent addressComponent = result.getAddressDetail();
        mMapView.changePlaceInfo(addressComponent.district+addressComponent.street+addressComponent.streetNumber);
    }



    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void gotoFindCar() {
//        mMapView.gotoFindCar();
    }

    private void dealWithErrorCode(int errorCode){
        String errStr = "";
        switch (errorCode) {
            case 100:
                errStr = "服务器内部错误";
                break;
            case 101:
                errStr = "请求设备号错误";
                break;
            case 102:
                errStr = "无请求内容";
                break;
            case 103:
                errStr = "请求内容错误";
                break;
            case 104:
                errStr = "请求URL错误";
                break;
            case 105:
                errStr = "请求范围过大";
                break;
            case 106:
                errStr = "服务器无响应";
                break;
            case 107:
                errStr = "服务器不在线";
                break;
            case 108:
                errStr = "设备无响应";
                break;
            case 109:
                errStr = "设备不在线";
                break;
            case 110:
                errStr = "设备不在线";
                break;
            default:
                break;
        }
        mMapView.showToast(errStr);
    }



}
