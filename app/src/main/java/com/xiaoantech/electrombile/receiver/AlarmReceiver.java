package com.xiaoantech.electrombile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.LogUtil;
import com.xiaoantech.electrombile.constant.ActivityConstants;
import com.xiaoantech.electrombile.utils.JSONUtil;


import org.json.JSONException;

/**
 * Created by lybvinci on 2015/5/1.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private String destinationName;
    private String callbackStatus;
    private String callbackAction;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        callbackStatus = bundle.get(ActivityConstants.callbackStatus).toString();
        callbackAction = bundle.get(ActivityConstants.callbackAction).toString();
        if (ActivityConstants.OK.equals(callbackStatus)) {
            if (callbackAction.equals(ActivityConstants.messageArrived)) {
                destinationName = bundle.get(ActivityConstants.destinationName).toString();
                if (destinationName.contains("alarm")) {
                    String s = bundle.get(ActivityConstants.PARCEL).toString();
                    int type = 0;
                    try {
                        type = Integer.parseInt(JSONUtil.ParseJSON(s, "type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //处理异常
                    }
                    LogUtil.log.i("创建报警界面:" + bundle.toString());
                    // DeviceUtils.showNotifation(context, topic, msg);
//                    DeviceUtils.wakeUpAndUnlock(context);
//                    Intent intentMy = new Intent(context, AlarmActivity.class);
//                    intentMy.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intentMy.putExtra(ProtocolConstants.TYPE, type);
//                    context.startActivity(intentMy);
                }
            }
        }
    }
}