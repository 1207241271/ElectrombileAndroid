package com.xiaoantech.electrombile.event.http;

import com.xiaoantech.electrombile.manager.HttpManager;

import org.json.JSONObject;

/**
 * Created by yangxu on 2016/11/7.
 */

public class HttpEvent {
    protected HttpManager.getType      type;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpEvent(HttpManager.getType type,String result,boolean isSuccess){
        this.type = type;
        this.resultStr = result;
        this.isSuccess = isSuccess;
    }

    public String getResult() {
        return resultStr;
    }

    public HttpManager.getType getRequestType() {
        return type;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
