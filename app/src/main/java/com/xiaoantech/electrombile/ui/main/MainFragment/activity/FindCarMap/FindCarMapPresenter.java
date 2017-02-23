package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCarMap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise.CarInfoReviseContract;
import com.xiaoantech.electrombile.widget.Dialog.CommonDialog;

import java.util.List;

/**
 * Created by yangxu on 2017/1/5.
 */

public class FindCarMapPresenter implements FindCarMapContract.Presenter{
    private FindCarMapContract.View mFindCarMapView;
    private LocationManager mLocationManager;
    private LocationListener locationListener;
    private boolean NetworkLocationListenerRemoved = false;


    protected FindCarMapPresenter(FindCarMapContract.View findCarMapView){
        this.mFindCarMapView = findCarMapView;
        findCarMapView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void getPhoneLocation() {

        try {
            if (locationListener == null) {
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if (mFindCarMapView.isGPSOpen(mLocationManager) && !NetworkLocationListenerRemoved) {
                            mLocationManager.removeUpdates(locationListener);
                            NetworkLocationListenerRemoved = true;
                            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                        }
                        LatLng sourceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CoordinateConverter converter = new CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(sourceLatLng);
                        mFindCarMapView.setUserPosition(converter.convert());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
            }

            String provider;
            mFindCarMapView.isGPSOpen(mLocationManager);
            List<String> locationList = mLocationManager.getProviders(true);
            if (locationList.contains(LocationManager.GPS_PROVIDER)) {
                NetworkLocationListenerRemoved = true;
                provider = LocationManager.GPS_PROVIDER;
                mLocationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
            } else if (locationList.contains(LocationManager.NETWORK_PROVIDER)) {
                NetworkLocationListenerRemoved = false;
                provider = LocationManager.NETWORK_PROVIDER;
                mLocationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
            } else {
                //TODO:无定位服务
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void gotoFindCarView() {
        mFindCarMapView.gotoFindCar();
    }

    @Override
    public void setLocationManager(LocationManager locationManager) {
        mLocationManager = locationManager;
    }
}
