package com.xiaoantech.electrombile.http;

import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/26.
 */

public class HttpPublishManager {
    private static String TAG = "HttpPublishManager";
    private static HttpPublishManager mInstance = null;

    public static HttpPublishManager getInstance(){
        if (null == mInstance){
            mInstance = new HttpPublishManager();
        }
        return mInstance;
    }

    public void getStatus(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_STATUS, getStringWithCmd(0));
    }

    public void getGPS(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_GPS, getStringWithCmd(1));
    }

    public void setAutoLockOn(int period){
        try{
            JSONObject param = new JSONObject();
            param.put("sw", 1);
            param.put("period", period);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_AUTOLOCK_SET_ON, HttpConstant.HttpCmd.HTTP_CMD_SET_AUTOLOCK, getStringWithCmdAndParam(2, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setAutoLockOff(){
        try{
            JSONObject param = new JSONObject();
            param.put("sw", 0);
            param.put("period", 5);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_AUTOLOCK_SET_OFF, HttpConstant.HttpCmd.HTTP_CMD_SET_AUTOLOCK, getStringWithCmdAndParam(2, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void getAutoLock(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_AUTOLOCK, getStringWithCmd(3));
    }

    public void setFenceOn(){
        try {
            JSONObject param = new JSONObject();
            param.put("defend", 1);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_FENCE_SET_ON, HttpConstant.HttpCmd.HTTP_CMD_SET_FENCE, getStringWithCmdAndParam(4, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setFenceOff(){
        try {
            JSONObject param = new JSONObject();
            param.put("defend", 0);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_FENCE_SET_OFF, HttpConstant.HttpCmd.HTTP_CMD_SET_FENCE, getStringWithCmdAndParam(4, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    public void getFence(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_FENCE, getStringWithCmd(5));
    }

    public void getBattery(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_BATTERY, getStringWithCmd(6));
    }

    public void setBatteryType(int batterytype){
        try {
            JSONObject param = new JSONObject();
            param.put("batterytype", batterytype);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_BATTERY_TYPE, getStringWithCmdAndParam(7,param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void startRecord(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_START_RECORD, getStringWithCmd(8));
    }

    public void stopRecord(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_STOP_RECORD, getStringWithCmd(9));
    }

    public void addBluetoothId(int bluetoothId){
        try {
            JSONObject param = new JSONObject();
            param.put("bluetoothId", bluetoothId);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_DEFAULT, getStringWithCmdAndParam(10, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setServer(int type, String server){
        try {
            JSONObject param = new JSONObject();
            param.put("type", type);
            param.put("server",server);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_SERVER, getStringWithCmdAndParam(11, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setFileName(int use, String fileName){
        try {
            JSONObject param = new JSONObject();
            param.put("use", use);
            param.put("fileName",fileName);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_FILENAME, getStringWithCmdAndParam(12, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setBluetooth(int sw){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", sw);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_BLUETOOTH, getStringWithCmdAndParam(13, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setAlarm(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_PHONEALARM, getStringWithCmd(14));
    }


    public void setLockOn(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 1);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_LOCKON_SET_ON, HttpConstant.HttpCmd.HTTP_CMD_SET_LOCKON, getStringWithCmdAndParam(15, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setLockOFF(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 0);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_LOCKON_SET_OFF, HttpConstant.HttpCmd.HTTP_CMD_SET_LOCKON, getStringWithCmdAndParam(15, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getDeviceMSG(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_DEVICEMSG, getStringWithCmd(16));
    }

    public void getGPSSignal(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_GPS_SIGNAL, getStringWithCmd(17));
    }

    public void getGSMSignal(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_GSM_SIGNAL, getStringWithCmd(18));
    }

    public void setATTest(String AT){
        try {
            JSONObject param = new JSONObject();
            param.put("AT", AT);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_ATTEST, getStringWithCmdAndParam(19, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLog(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_LOG, getStringWithCmd(20));
    }

    public void setReset(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_SET_RESET, getStringWithCmd(21));
    }

    public void setLinkSwitchOn(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 1);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_SWITCH_LINK_SET_ON, HttpConstant.HttpCmd.HTTP_CMD_SET_LINK_SWITCH, getStringWithCmdAndParam(22, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setLinkSwitchOff(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 0);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_SWITCH_LINK_SET_OFF, HttpConstant.HttpCmd.HTTP_CMD_SET_LINK_SWITCH, getStringWithCmdAndParam(22, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLinkSwitch(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_LINK_SWITCH, getStringWithCmd(23));
    }

    public void getLockOn(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_LOCKON, getStringWithCmd(24));
    }

    public void setLinkElectricLockOn(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 1);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_ELECTRICLOCK_LINK_SET_ON, HttpConstant.HttpCmd.HTTP_CMD_SET_LINK_ELECTRICLOCK, getStringWithCmdAndParam(25, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setLinkElectricLockOff(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw", 0);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_ELECTRICLOCK_LINK_SET_OFF, HttpConstant.HttpCmd.HTTP_CMD_SET_LINK_ELECTRICLOCK, getStringWithCmdAndParam(25, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void getLinkElectricLock(){
        HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_GET_LINK_ELECTRICLOCK, getStringWithCmd(26));
    }

    public void delBluetoothIMEI(String bluetoothId){
        try {
            JSONObject param = new JSONObject();
            param.put("bluetoothId", bluetoothId);
            HttpManager.postHttpCmdResult(getDeviceUrl(), HttpManager.postType.POST_TYPE_DEFAULT, HttpConstant.HttpCmd.HTTP_CMD_DEL_BLUETOOTH_IMEI, getStringWithCmdAndParam(27, param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setWheelLockOn(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw",1);
            HttpManager.postHttpCmdResult(getDeviceUrl(),HttpManager.postType.POST_TYPE_WHEEL, HttpConstant.HttpCmd.HTTP_CMD_SET_WHEEL,getStringWithCmdAndParam(28,param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setWheelLockOff(){
        try {
            JSONObject param = new JSONObject();
            param.put("sw",0);
            HttpManager.postHttpCmdResult(getDeviceUrl(),HttpManager.postType.POST_TYPE_WHEEL, HttpConstant.HttpCmd.HTTP_CMD_SET_WHEEL,getStringWithCmdAndParam(28,param));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void openSeatLock(){
        HttpManager.postHttpCmdResult(getDeviceUrl(),HttpManager.postType.POST_TYPE_SEAT, HttpConstant.HttpCmd.HTTP_CMD_OPEN_SEAT,getStringWithCmd(29));
    }

    private String getDeviceUrl(){
        return LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort()+"/v1/device";
    }

    private String getStringWithCmd(int cmd){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonCmd = new JSONObject();
            jsonCmd.put("c", cmd);
            jsonObject.put("imei", BasicDataManager.getInstance().getBindIMEI());
            jsonObject.put("cmd", jsonCmd);
            return jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    private String getStringWithCmdAndParam(int cmd,JSONObject param){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonCmd = new JSONObject();
            jsonCmd.put("c", cmd);
            jsonCmd.put("param",param);
            jsonObject.put("imei", BasicDataManager.getInstance().getBindIMEI());
            jsonObject.put("cmd", jsonCmd);
            return jsonObject.toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
