package com.xiaoantech.electrombile.event.LeanCloud;

import com.xiaoantech.electrombile.constant.LeanCloudConstant;

/**
 * Created by yangxu on 2017/4/23.
 */

public class DeleteEvent {
    protected boolean isDelete;
    public DeleteEvent(boolean isDelete){
        this.isDelete = isDelete;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
