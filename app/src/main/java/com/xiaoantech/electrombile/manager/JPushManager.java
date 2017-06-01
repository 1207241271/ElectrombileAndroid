package com.xiaoantech.electrombile.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xiaoantech.electrombile.application.App;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by yangxu on 2016/12/16.
 */

public class JPushManager {
    private static final String TAG = "JPushManager";
    private static final int MSG_SET_ALIAS = 1001;
    private Context mContext;


    private final static  JPushManager mInstance = new JPushManager();

    public static JPushManager getInstance() {
        return mInstance;
    }

    private JPushManager(){
        mContext = App.getContext();
    }



    public void setPushAlias(final String IMEI){
        String aliasStr = "simcom_" + IMEI;
        JPushInterface.setAlias(mContext, aliasStr, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                String logs;
                switch (code){
                    case 0:
                        //订阅成功

                        break;
                    case 6002:
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("IMEI",IMEI);
                        message.setData(bundle);
                        message.what = 6002;
//                        mHandler.sendMessage(message);
                        //订阅超时
                        break;
                    default:
                        logs = "Failed with errorCode" + code;
                        Log.e(TAG,logs);
                }
            }
        });
    }

    public void deleteAlias(){
        String aliasStr = "";
        JPushInterface.setAlias(mContext, aliasStr, new TagAliasCallback() {
            @Override
            public void gotResult(int code, String s, Set<String> set) {
                String logs;
                switch (code){
                    case 0:
                        //订阅成功

                        break;
                    case 6002:

                        break;
                    default:
                        logs = "Failed with errorCode" + code;
                        Log.e(TAG,logs);
                }
            }
        });
    }


}
