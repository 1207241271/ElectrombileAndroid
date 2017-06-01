package com.xiaoantech.electrombile.event.http;

import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.http.HttpManager;

/**
 * Created by yangxu on 2017/3/2.
 */

public class HttpPostEvent {
    protected HttpManager.postType      type;
    protected HttpConstant.HttpCmd      cmdType;
    protected String resultStr;
    protected boolean isSuccess;

    public HttpPostEvent(HttpManager.postType type, String result, boolean isSuccess , HttpConstant.HttpCmd cmdType){
        this.type = type;
        this.resultStr = result;
        this.isSuccess = isSuccess;
        this.cmdType = cmdType;
    }

    public String getResult() {
        return resultStr;
    }

    public HttpManager.postType getRequestType() {
        return type;
    }
    public HttpConstant.HttpCmd getCmdType() {return cmdType;}
}

