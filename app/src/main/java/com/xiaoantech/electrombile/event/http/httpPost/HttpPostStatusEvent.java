package com.xiaoantech.electrombile.event.http.httpPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/27.
 */

public class HttpPostStatusEvent {
    private int code;
    private int sw;
    private int period;
    private int percent;
    private int type;
    private int defend;
    private int timestamp;
    private double lat;
    private double lng;
    private int speed;
    private int course;
    private int mcc;
    private int mnc;
    private int lac;
    private int ci;
    private boolean isGPS;
    private String string;


    public HttpPostStatusEvent(String resultStr) {
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")) {
                this.code = jsonObject.getInt("code");
                if (code == 0) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.string = result.toString();
                    JSONObject autolock = result.getJSONObject("autolock");
                    this.sw = autolock.getInt("sw");
                    this.period = autolock.getInt("period");
                    JSONObject battery = result.getJSONObject("battery");
                    this.percent = battery.getInt("percent");
                    this.type = battery.getInt("type");
                    this.defend = result.getInt("defend");
                    if (result.has("gps")){
                        JSONObject gps = result.getJSONObject("gps");
                        this.timestamp = gps.getInt("timestamp");
                        this.lat = gps.getDouble("lat");
                        this.lng = gps.getDouble("lng");
                        this.speed = gps.getInt("speed");
                        this.course = gps.getInt("course");
                        this.isGPS = true;
                    }else if (result.has("cell")){
                        JSONObject cell = result.getJSONObject("cell");
                        this.mcc = cell.getInt("mcc");
                        this.mnc = cell.getInt("mnc");
                        this.lac = cell.getInt("lac");
                        this.ci = cell.getInt("ci");
                        this.isGPS = false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public int getSw() {
        return sw;
    }

    public int getPeriod() {
        return period;
    }

    public int getPercent(){
        return percent;
    }

    public int getType(){
        return type;
    }

    public int getDefend(){
        return defend;
    }

    public int getTimestamp(){
        return timestamp;
    }

    public double getLat(){
        return lat;
    }

    public double getLng(){
        return lng;
    }

    public int getSpeed(){
        return speed;
    }

    public int getCourse(){
        return course;
    }

    public int getMcc() {
        return mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public int getLac() {
        return lac;
    }

    public int getCi() {
        return ci;
    }

    public boolean getisGPS(){
        return isGPS;
    }

    public String getString(){
        return string;
    }
}