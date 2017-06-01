package com.xiaoantech.electrombile.event.http.httpPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/28.
 */

public class HttpPostDeviceMSGEvent {
    private int code;
    private String server;
    private int sw;
    private int period;
    private int defend;
    private String BTAddress;
    public HttpPostDeviceMSGEvent(String resultStr){
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
                if (code == 0){
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.server = result.getString("server");
                    JSONObject autodefend = result.getJSONObject("autodefend");
                    this.sw = autodefend.getInt("sw");
                    this.period = autodefend.getInt("period");
                    this.defend = result.getInt("defend");
                    this.BTAddress = result.getString("BTAddress");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCode(){
        return code;
    }

    public String getServer(){
        return server;
    }

    public int getSw(){
        return sw;
    }

    public int getPeriod(){
        return period;
    }

    public int getDefend(){
        return defend;
    }

    public String getBTAddress(){
        return BTAddress;
    }
}
