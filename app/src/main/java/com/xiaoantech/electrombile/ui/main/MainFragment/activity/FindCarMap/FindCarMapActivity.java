package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCarMap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityFindCarMapBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCar.FindCarActivity;
import com.xiaoantech.electrombile.widget.Dialog.CommonDialog;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;

/**
 * Created by yangxu on 2017/1/5.
 */

public class FindCarMapActivity extends BaseAcitivity implements FindCarMapContract.View{
    private ActivityFindCarMapBinding  mBinding;
    private FindCarMapContract.Presenter mPresenter;
    private BaiduMap            mBaiduMap;
    private Marker              mCarMarker;
    private Marker              mUserMarker;
    private String              mLatestPlaceInfo;
    private String              mLatestDateInfo;
    private LatLng              carLatLng;
    private LocationManager     locationManager;
    private LocationListener    locationListener;

    @Override
    protected void initBefore() {
        try {
            Intent intent = getIntent();
            mLatestDateInfo = intent.getStringExtra("DateInfo");
            mLatestPlaceInfo = intent.getStringExtra("PlaceInfo");
            carLatLng = new LatLng(intent.getDoubleExtra("Lat", 0), intent.getDoubleExtra("Lng", 0));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_car_map);
    }

    @Override
    protected void initView() {
        try {
            mPresenter = new FindCarMapPresenter(this);
            mPresenter.setLocationManager((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
            mBinding.setPresenter(mPresenter);
            mBaiduMap = mBinding.baiduMap.getMap();
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return true;
                }
            });

            initCarLostInfo();
            initUserMarker();
            initCarMarker();
            mPresenter.getPhoneLocation();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initCarLostInfo(){

        mBinding.txtCarName.setText(BasicDataManager.getInstance().getBindCarInfo().getName());
        mBinding.txtCarPlace.setText(mLatestPlaceInfo);
        mBinding.txtGpsMode.setText("GPS信号");

    }

    private void initCarMarker(){
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.online);
        MarkerOptions option = new MarkerOptions().position(carLatLng).icon(bitmapDescriptor);
        mCarMarker = (Marker) mBaiduMap.addOverlay(option);
    }

    private void initUserMarker(){
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.marker_person);
        LatLng latLng = new LatLng(30.5171, 114.4392);
        MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        mUserMarker = (Marker) mBaiduMap.addOverlay(options);
    }

    @Override
    public boolean isGPSOpen(LocationManager locationManager) {
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            CustomDialog.Builder customDialog = new CustomDialog.Builder(this);
            customDialog.setTitle("请打开GPS开关")
                    .setMessage("GPS定位手机位置更加准确,请开启GPS!")
                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void setUserPosition(LatLng latLng) {
        mUserMarker.setPosition(latLng);
        scaleMap();
    }

    private void scaleMap(){
        LatLng point1 = mUserMarker.getPosition();
        LatLng point2 = mCarMarker.getPosition();

        double latitude_min,latitude_max;
        double longitude_min,longitude_max;

        if (point1.latitude < point2.latitude){
            latitude_min = point1.latitude;
            latitude_max = point2.latitude;
        }else {
            latitude_min = point2.latitude;
            latitude_max = point1.latitude;
        }

        if (point1.longitude < point2.longitude){
            longitude_min = point1.longitude;
            longitude_max = point2.longitude;
        }else {
            longitude_min = point2.longitude;
            longitude_max = point1.longitude;
        }

        LatLng southwest = new LatLng(latitude_min,longitude_min);
        LatLng northeast = new LatLng(latitude_max,longitude_max);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.setMapStatus(mapStatusUpdate);

    }

    @Override
    public void gotoFindCar() {
        Intent intent = new Intent(FindCarMapActivity.this, FindCarActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPresenter(FindCarMapContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
