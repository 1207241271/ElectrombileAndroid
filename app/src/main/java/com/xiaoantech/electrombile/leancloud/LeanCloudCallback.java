package com.xiaoantech.electrombile.leancloud;

import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.event.LeanCloud.BindEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by yangxu on 2016/11/7.
 */

public class LeanCloudCallback {

    public void dealWithError(LeanCloudConstant.leanCloudOptionType type,String msg){

    }

    public void dealWithBindResult(LeanCloudConstant.LeanCloudBindResult result){
        BindEvent event = new BindEvent(result);
        EventBus.getDefault().post(event);
    }

    public void getTotoalItinerarySuccess(int itinerary){

    }

    public void getIMEIList(ArrayList<String> IMEIList){

    }
}
