package com.xiaoantech.electrombile.leancloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.JPushManager;

import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by yangxu on 2016/11/7.
 */

public class LeanCLoudManager {
    private static LeanCLoudManager mInstance   =   null;
    private LeanCloudDataManager    leanCloudDataManager;
    private LeanCloudCallback leanCloudCallback;


    private boolean bindErrorFlag   ;
    private int    bindFlag;
    private AVObject didObject;

    private LeanCLoudManager(){
        leanCloudDataManager = new LeanCloudDataManager();
        leanCloudCallback = new LeanCloudCallback();
    }

    public LeanCLoudManager getInstance(){
        if (null == mInstance){
            mInstance = new LeanCLoudManager();
        }
        return mInstance;
    }

    public void getTotalItinerary(String IMEI){

    }

    public void getIMEIList(){

    }

    public void bindIMEI(final String IMEI){
        bindErrorFlag = false;
        bindFlag = 0;
        didObject = null;

        AVQuery<AVObject> queryDID = new AVQuery<>(LeanCloudConstant.DIDTable);
        queryDID.whereEqualTo(LeanCloudConstant.IMEI,IMEI);
        queryDID.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (list.size() <= 0){
                        checkIsBindOK(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_DID_NONE,0,IMEI);
                    }else if (list.size() > 1){
                        checkIsBindOK(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_DID_MUCH,0,IMEI);
                    }else {
                        didObject = list.get(0);
                        checkIsBindOK(null,LeanCloudConstant.BindFlagDID,IMEI);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });

        AVQuery<AVObject> queryBind = new AVQuery<>(LeanCloudConstant.BindTable);
        queryBind.whereEqualTo(LeanCloudConstant.IMEI,IMEI);
        queryBind.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (list.size() > 0){
                        checkIsBindOK(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_BIND_MUCH, 0,IMEI);
                    }else {
                        checkIsBindOK(null,LeanCloudConstant.BindFlagBIND,IMEI);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }


    private void checkIsBindOK(LeanCloudConstant.LeanCloudBindResult result,int flag,String IMEI){
        if (bindErrorFlag){
            return;
        }
        if (null != result){
            switch (result){
                case LEAN_CLOUD_BIND_RESULT_BIND_MUCH:
                    leanCloudCallback.dealWithBindResult(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_BIND_MUCH);
                    break;
                case LEAN_CLOUD_BIND_RESULT_DID_NONE:
                    leanCloudCallback.dealWithBindResult(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_DID_NONE);
                    break;
                case LEAN_CLOUD_BIND_RESULT_DID_MUCH:
                    leanCloudCallback.dealWithBindResult(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_DID_MUCH);
                    break;
                default:

            }
        }

        bindFlag = bindFlag | flag;
        if (bindFlag == (LeanCloudConstant.BindFlagDID | LeanCloudConstant.BindFlagBIND)){
            startBind(IMEI);
        }
    }

    private void startBind(final String IMEI){
        AVObject bindDevice = new AVObject(LeanCloudConstant.BindTable);
        if (didObject == null){
            return;
        }
        bindDevice.put(LeanCloudConstant.Device,didObject);
        bindDevice.put(LeanCloudConstant.Admin,true);
        bindDevice.put(LeanCloudConstant.IMEI,IMEI);
        bindDevice.put(LeanCloudConstant.User,AVUser.getCurrentUser());
        bindDevice.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (null == e){
                    //TODO:绑定成功
                    JPushManager.getInstance().setPushAlias(IMEI);
                    BasicDataManager.getInstance().changeBindIMEI(IMEI,false);
                    leanCloudCallback.dealWithBindResult(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_BIND_SUCCESS);
                } else {
                    leanCloudCallback.dealWithBindResult(LeanCloudConstant.LeanCloudBindResult.LEAN_CLOUD_BIND_RESULT_BIND_FAIL);
                }
            }
        });
    }


}
