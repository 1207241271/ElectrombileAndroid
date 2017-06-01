package com.xiaoantech.electrombile.event.http.httpPost;

import com.xiaoantech.electrombile.http.HttpManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/28.
 */

public class HttpPostFenceSetEvent {
    private int code;
    private HttpManager.postType postType;
    public HttpPostFenceSetEvent(String resultStr,HttpManager.postType postType){
        this.postType = postType;
        try {
            JSONObject jsonObject = new JSONObject(resultStr);
            if (jsonObject.has("code")){
                this.code = jsonObject.getInt("code");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public int getCode(){
        return code;
    }

    public HttpManager.postType getPostType() {
        return postType;
    }
}
