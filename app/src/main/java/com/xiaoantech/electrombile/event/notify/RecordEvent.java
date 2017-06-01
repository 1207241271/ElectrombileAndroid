package com.xiaoantech.electrombile.event.notify;

import com.xiaoantech.electrombile.constant.EventBusConstant;

import org.json.JSONObject;

/**
 * Created by yangxu on 2017/3/6.
 */

public class RecordEvent  extends NotifyEvent{
    public RecordEvent(EventBusConstant.notifyType notifyType, JSONObject jsonObject){
        this.jsonObject = jsonObject;
        this.notifyType = notifyType;
    }
}
