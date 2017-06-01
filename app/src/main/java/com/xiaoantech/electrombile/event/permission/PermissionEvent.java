package com.xiaoantech.electrombile.event.permission;

/**
 * Created by yangxu on 2017/4/8.
 */

public class PermissionEvent {
    private String  permission;
    private int     code;
    public PermissionEvent(String permission,int code){
        this.permission = permission;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getPermission() {
        return permission;
    }
}
