package com.xiaoantech.electrombile.event.http.httpPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangxu on 2017/3/27.
 */

public class HttpPostBatteryEvent {
    private int code;
    private int percent;

    public HttpPostBatteryEvent(String resultStr){
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
                if (code == 0) {
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.percent = result.getInt("percent");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public int getPercent() {
        return percent;
    }
}
