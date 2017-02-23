package com.xiaoantech.electrombile.model;

import java.util.ArrayList;

/**
 * Created by yangxu on 2017/1/4.
 */

public class OfflineMapModel {
    private String cityName;
    private int cityId;
    private STATE state;
    private int progress;
    public ArrayList<OfflineMapModel> childCities;

    public enum STATE{
        //NONE("未安装",1),LOADING("下载中",2),FINISHED("已完成",3),UPDATE("需更新",4);
        NONE,LOADING, FINISHED,UPDATE,SUSPENDED;
    }

    public ArrayList<OfflineMapModel> getChildCities() {
        return childCities;
    }

    public void setChildCities(ArrayList<OfflineMapModel> childCities) {
        this.childCities = childCities;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
