package com.xiaoantech.electrombile.event.http.httpPost;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/29.
 */

public class HttpPostSwitchGet {
    private int code;
    private int sw;
    private static HttpPostSwitchGet mInstance = null;

    public static HttpPostSwitchGet getmInstance(){
        if (mInstance == null){
            mInstance = new HttpPostSwitchGet();
        }
        return mInstance;
    }

    public void SwitchGetResult(String resultStr){
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
                if (code == 0){
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.sw = result.getInt("sw");
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
}
