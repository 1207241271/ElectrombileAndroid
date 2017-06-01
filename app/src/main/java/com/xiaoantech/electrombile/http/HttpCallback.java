package com.xiaoantech.electrombile.http;

import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.http.HttpPostEvent;
import com.xiaoantech.electrombile.event.http.HttpPutEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostATTestEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostAlarmEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostAutoLockGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostAutoLockSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostBatteryEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostBatteryTypeEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostBluetoothBindEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostBluetoothDelEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostBluetoothSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostDeviceMSGEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostRecordStartEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostRecordStopEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostElectricGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostElectricSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostFenceGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostFenceSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostFilenameEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGPSEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGPSSignalEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostGSMSignalEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostLockGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostLockSetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostLogEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostResetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostServerEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostStatusEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostSwitchGetEvent;
import com.xiaoantech.electrombile.event.http.httpPost.HttpPostSwitchSetEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangxu on 2017/3/27.
 */

public class HttpCallback {

    public static void dealWithHttpCmdPost(HttpManager.postType postType, HttpConstant.HttpCmd cmd, String result) {
        switch (cmd) {
            case HTTP_CMD_GET_STATUS:
                EventBus.getDefault().post(new HttpPostStatusEvent(result));
                break;
            case HTTP_CMD_GET_GPS:
                EventBus.getDefault().post(new HttpPostGPSEvent(result));
                break;
            case HTTP_CMD_SET_AUTOLOCK:
                EventBus.getDefault().post(new HttpPostAutoLockSetEvent(result, postType));
                break;
            case HTTP_CMD_GET_AUTOLOCK:
                EventBus.getDefault().post(new HttpPostAutoLockGetEvent(result));
                break;
            case HTTP_CMD_SET_FENCE:
                EventBus.getDefault().post(new HttpPostFenceSetEvent(result, postType));
                break;
            case HTTP_CMD_GET_FENCE:
                EventBus.getDefault().post(new HttpPostFenceGetEvent(result));
                break;
            case HTTP_CMD_GET_BATTERY:
                EventBus.getDefault().post(new HttpPostBatteryEvent(result));
                break;
            case HTTP_CMD_SET_BATTERY_TYPE:
                EventBus.getDefault().post(new HttpPostBatteryTypeEvent(result));
                break;
            case HTTP_CMD_START_RECORD:
                EventBus.getDefault().post(new HttpPostRecordStartEvent(result));
                break;
            case HTTP_CMD_STOP_RECORD:
                EventBus.getDefault().post(new HttpPostRecordStopEvent(result));
                break;
            case HTTP_CMD_SET_BLUETOOTH_IMEI:
                EventBus.getDefault().post(new HttpPostBluetoothBindEvent(result));
                break;
            case HTTP_CMD_SET_SERVER:
                EventBus.getDefault().post(new HttpPostServerEvent(result));
                break;
            case HTTP_CMD_SET_FILENAME:
                EventBus.getDefault().post(new HttpPostFilenameEvent(result));
                break;
            case HTTP_CMD_SET_BLUETOOTH:
                EventBus.getDefault().post(new HttpPostBluetoothSetEvent(result));
                break;
            case HTTP_CMD_SET_PHONEALARM:
                EventBus.getDefault().post(new HttpPostAlarmEvent(result));
                break;
            case HTTP_CMD_SET_LOCKON:
                EventBus.getDefault().post(new HttpPostLockSetEvent(result));
                break;
            case HTTP_CMD_GET_DEVICEMSG:
                EventBus.getDefault().post(new HttpPostDeviceMSGEvent(result));
                break;
            case HTTP_CMD_GET_GPS_SIGNAL:
                EventBus.getDefault().post(new HttpPostGPSSignalEvent(result));
                break;
            case HTTP_CMD_GET_GSM_SIGNAL:
                EventBus.getDefault().post(new HttpPostGSMSignalEvent(result));
                break;
            case HTTP_CMD_SET_ATTEST:
                EventBus.getDefault().post(new HttpPostATTestEvent(result));
                break;
            case HTTP_CMD_GET_LOG:
                EventBus.getDefault().post(new HttpPostLogEvent(result));
                break;
            case HTTP_CMD_SET_RESET:
                EventBus.getDefault().post(new HttpPostResetEvent(result));
                break;
            case HTTP_CMD_SET_LINK_SWITCH:
                EventBus.getDefault().post(new HttpPostSwitchSetEvent(result));
                break;
            case HTTP_CMD_GET_LINK_SWITCH:
                EventBus.getDefault().post(new HttpPostSwitchGetEvent(result));
                break;
            case HTTP_CMD_GET_LOCKON:
                EventBus.getDefault().post(new HttpPostLockGetEvent(result));
                break;
            case HTTP_CMD_SET_LINK_ELECTRICLOCK:
                EventBus.getDefault().post(new HttpPostElectricSetEvent(result));
                break;
            case HTTP_CMD_GET_LINK_ELECTRICLOCK:
                EventBus.getDefault().post(new HttpPostElectricGetEvent(result));
                break;
            case HTTP_CMD_DEL_BLUETOOTH_IMEI:
                EventBus.getDefault().post(new HttpPostBluetoothDelEvent(result));
                break;

        }
    }

    public static void dealWithHttpPost(HttpManager.postType postType, String result) {
        switch (postType) {
            case POST_TYPE_PHONE:
                EventBus.getDefault().post(new HttpPostEvent(HttpManager.postType.POST_TYPE_PHONE,result,true, HttpConstant.HttpCmd.HTTP_CMD_DEFAULT));
        }
    }

}