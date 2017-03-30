package com.xiaoantech.electrombile.event.http.httpPost;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/28.
 */

public class HttpPostAutoLockGet {
    private int code;
    private int sw;
    private int period;
    private static HttpPostAutoLockGet mInstance = null;

    public static HttpPostAutoLockGet getmInstance(){
        if (mInstance == null){
            mInstance = new HttpPostAutoLockGet();
        }
        return mInstance;
    }

    public void AutoLockGetResult(String resultStr){
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
                if (code == 0){
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.sw = result.getInt("sw");
                    this.period = result.getInt("period");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCode(){
        return code;
    }

    public int getSw(){
        return sw;
    }

    public int getPeriod(){
        return period;
    }
}
