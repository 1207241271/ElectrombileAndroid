package com.xiaoantech.electrombile.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.xiaoantech.electrombile.event.permission.PermissionEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangxu on 2017/4/8.
 */

public class PermissionCheckUtil{

    private final Context mContext;


    public PermissionCheckUtil(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断是否缺少权限
    public boolean lakesPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lakesPermission(permission))
                return true;
        }
        return false;
    }

    private boolean lakesPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED;
    }
}
