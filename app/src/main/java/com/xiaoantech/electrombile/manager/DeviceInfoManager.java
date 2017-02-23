package com.xiaoantech.electrombile.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.tools.others.PermissionsChecker;

/**
 * Created by yangxu on 2016/11/2.
 */

public class DeviceInfoManager {
    private static DeviceInfoManager   mInstance = null;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    public static DeviceInfoManager getInstance(){
        if (null == mInstance){
            mInstance = new DeviceInfoManager();
        }
        return  mInstance;
    }

    public static String getDeviceId(){
        PermissionsChecker permissionsChecker = new PermissionsChecker(App.getContext());
        if (!permissionsChecker.lakesPermissions(PERMISSIONS)){
            TelephonyManager telecomManager = (TelephonyManager) App.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("test",telecomManager.getDeviceId());
            return telecomManager.getDeviceId();
        }
        return null;
    }
}
