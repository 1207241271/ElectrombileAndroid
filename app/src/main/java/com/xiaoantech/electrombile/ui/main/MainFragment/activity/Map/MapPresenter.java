package com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xiaoantech.electrombile.constant.LayoutConstant;
import com.xiaoantech.electrombile.constant.MqttCommonConstant;
import com.xiaoantech.electrombile.event.cmd.LocationEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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

        MqttPublishManager.getInstance().getLocation(BasicDataManager.getInstance().getBindIMEI());
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
        MqttPublishManager.getInstance().getLocation(BasicDataManager.getInstance().getBindIMEI());
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
                mMapView.changeGPSPoint(point);
                mMapView.changeDateInfo(TimeUtil.getMinuteStringFromTimeStamp(timestamp));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            }
        }catch (Exception e) {
            e.printStackTrace();
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
        mMapView.gotoFindCar();
    }

    private void dealWithErrorCode(int errorCode){
        if (errorCode == MqttCommonConstant.CODE_INTERNAL_ERR){
            mMapView.showToast("服务器内部错误!");
    }else  if (errorCode == MqttCommonConstant.CODE_DEVICE_OFFLINE){
            mMapView.showToast("设备离线，请检查设备！");
            //TODO:ErrorShow
        }
    }



}
