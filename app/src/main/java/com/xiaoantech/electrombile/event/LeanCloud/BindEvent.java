package com.xiaoantech.electrombile.event.LeanCloud;

import com.xiaoantech.electrombile.constant.LeanCloudConstant;

/**
 * Created by yangxu on 2016/12/16.
 */

public class BindEvent {
    protected LeanCloudConstant.LeanCloudBindResult bindResult;
    public BindEvent(LeanCloudConstant.LeanCloudBindResult result){
        this.bindResult = result;
    }

    public LeanCloudConstant.LeanCloudBindResult getBindResult() {
        return bindResult;
    }
}
