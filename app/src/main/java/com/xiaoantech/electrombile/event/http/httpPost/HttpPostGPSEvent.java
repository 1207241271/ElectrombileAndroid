package com.xiaoantech.electrombile.event.http.httpPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/28.
 */

public class HttpPostGPSEvent {
    private int code;
    private int timestamp;
    private double lat;
    private double lng;
    private int speed;
    private boolean isGPS;
    private int mcc;
    private int mnc;
    private int lac;
    private int ci;

    public HttpPostGPSEvent(String resultStr){
        try{
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
                if (code == 0){
                    JSONObject result = jsonObject.getJSONObject("result");
                    if (result.has("gps")){
                        JSONObject gps = result.getJSONObject("gps");
                        this.timestamp = gps.getInt("timestamp");
                        this.lat = gps.getDouble("lat");
                        this.lng = gps.getDouble("lng");
                        this.speed = gps.getInt("speed");
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
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCode(){
        return code;
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
}

