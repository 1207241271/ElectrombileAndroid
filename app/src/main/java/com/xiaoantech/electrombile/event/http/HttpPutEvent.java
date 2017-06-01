package com.xiaoantech.electrombile.event.http;

import com.xiaoantech.electrombile.http.HttpManager;

/**
 * Created by tanghuichuan on 2017/3/2.
 */

public class HttpPutEvent {
    protected HttpManager.putType type;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpPutEvent(HttpManager.putType type, String resultStr, boolean isSuccess){
        this.type = type;
        this.resultStr = resultStr;
        this.isSuccess = isSuccess;
    }

    public String getResultStr(){
        return resultStr;
    }

    public HttpManager.putType getRequestType(){
        return type;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
