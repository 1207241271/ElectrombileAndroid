package com.xiaoantech.electrombile.model;

import android.graphics.Bitmap;

import com.xiaoantech.electrombile.utils.TimeUtil;

import java.sql.Time;

/**
 * Created by yangxu on 2016/12/6.
 */

public class CarInfoModel {
    private String  name;
    private String  IMEI;
    private long    bindTime;
    private Bitmap  cropImage;

    public CarInfoModel(String IMEI,long bindTime){
        this.IMEI = IMEI;
        this.bindTime = bindTime;
    }
    public CarInfoModel(String IMEI){
        this.IMEI = IMEI;
        this.name = IMEI;

        this.bindTime = TimeUtil.getCurrentTime();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCropImage(Bitmap cropImage) {
        this.cropImage = cropImage;
    }


    public String getName() {
        return name;
    }

    public Bitmap getCropImage() {
        return cropImage;
    }

    public String getIMEI() {
        return IMEI;
    }

    public long getBindTime() {
        return bindTime;
    }
}
