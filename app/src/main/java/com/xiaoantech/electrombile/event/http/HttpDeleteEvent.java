package com.xiaoantech.electrombile.event.http;

import com.xiaoantech.electrombile.http.HttpManager;

/**
 * Created by tanghuichuan on 2017/3/2.
 */

public class HttpDeleteEvent {
    protected HttpManager.deleteType type;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpDeleteEvent(HttpManager.deleteType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManager.deleteType getRequestType(){
        return type;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
