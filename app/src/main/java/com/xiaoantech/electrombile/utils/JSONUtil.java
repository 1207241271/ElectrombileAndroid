package com.xiaoantech.electrombile.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yangxu on 2016/11/7.
 */

public class JSONUtil {
    public static String ParseJSON(String str, String name) throws JSONException {
        if(str.isEmpty()||name.isEmpty())
            return null;

        String result = null;
        try {
            JSONObject myJSobj= new JSONObject(str);
            result = myJSobj.has(name)?myJSobj.getString(name):null;
        } catch (JSONException e) {
            e.printStackTrace();
            throw e;
        }

        return result;
    }
}
