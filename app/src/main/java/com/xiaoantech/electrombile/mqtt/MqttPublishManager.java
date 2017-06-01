package com.xiaoantech.electrombile.mqtt;

import android.util.Log;

import com.xiaoantech.electrombile.constant.MqttCommonConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangxu on 2016/11/2.
 */

public class MqttPublishManager {
    private static String TAG = "MqttPublishManager";
    private static MqttPublishManager mInstance = null;

    public static MqttPublishManager getInstance() {
        if (null == mInstance){
            mInstance = new MqttPublishManager();
        }
        return mInstance;
    }


    private byte[] getPayloadWithCMD(int cmd){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(MqttCommonConstant.CMD,cmd);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG,jsonObject.toString());
        return jsonObject.toString().getBytes();
    }

    private byte[] getPayloadWithCMD(int cmd,String name,int value) {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(MqttCommonConstant.CMD,cmd);
            jsonObject.put(name,value);
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.d(TAG,jsonObject.toString());
        return jsonObject.toString().getBytes();
    }

    private void publishCMD(String IMEI, byte[] payload){
        if (null != IMEI) {
            String topic = "app2dev/" + IMEI + "/cmd";
            MqttManager.getInstance().publish(topic, payload);
        }
        //TODO:Error
    }

}
