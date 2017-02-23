package com.xiaoantech.electrombile.model;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by yangxu on 2016/11/25.
 */

public class GPSPointModel {
    private long timestamp;
    private double lat;
    private double lng;
    private int speed;

    public GPSPointModel(long timestamp,double lat,double lng){
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
    }

    public GPSPointModel(long timestamp,double lat,double lng,int speed){
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
    }

    public LatLng getBaiduPoint(){
        LatLng gps = new LatLng(lat,lng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(gps);
        return  converter.convert();
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getSpeed(){
        return speed;
    }

    @Override
    public String toString(){
        return timestamp + "   " + lat + "    " + lng;
    }
}
