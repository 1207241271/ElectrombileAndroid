package com.xiaoantech.electrombile.leancloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxu on 2016/11/6.
 */

public class LeanCloudDataManager {
//    private static LeanCloudDataManager mInstance   =   null;
    private LeanCloudCallback leanCloudCallback;


    public LeanCloudDataManager(){
        leanCloudCallback = new LeanCloudCallback();
    }

    public void getTotalIniterary(String IMEI){
        AVQuery<AVObject> query = new AVQuery<>(LeanCloudConstant.DIDTable);
        query.whereEqualTo(LeanCloudConstant.IMEI,IMEI);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (!list.isEmpty()){
                        if (1 != list.size()){
                            leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_TOTAL_ITINERARY,"该设备IMEI在设备列表中重复！");
                            return;
                        }
                        AVObject avObject = list.get(0);
                        try {
                            int itierary = avObject.getInt(LeanCloudConstant.Itinerary);
                            leanCloudCallback.getTotoalItinerarySuccess(itierary);
                        }catch (Exception ee){
                            ee.printStackTrace();
                            leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_TOTAL_ITINERARY,ee.toString());
                        }
                    }else {
                        leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_TOTAL_ITINERARY,"该记录不存在！");
                    }
                }else {
                    leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_TOTAL_ITINERARY,e.toString());
                }
            }
        });
    }

    public void getIMEIList(){
        AVUser user = AVUser.getCurrentUser();
        AVQuery<AVObject> query = new AVQuery<>(LeanCloudConstant.BindTable);
        query.whereEqualTo(LeanCloudConstant.User,user.getObjectId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (!list.isEmpty()){
                        ArrayList<String> IMEIList = new ArrayList<String>();
                        for (AVObject bind:list) {
                            IMEIList.add(bind.getString(LeanCloudConstant.IMEI));
                        }
                        leanCloudCallback.getIMEIList(IMEIList);
                    }else {
                        leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_IMEI_LIST,"IMEI列表为空！");
                    }
                }else {
                    leanCloudCallback.dealWithError(LeanCloudConstant.leanCloudOptionType.LEAN_CLOUD_OPTION_TYPE_IMEI_LIST,e.toString());
                }
            }
        });
    }

//    public static LeanCloudDataManager getInstance(){
//        if (null == mInstance){
//            mInstance = new LeanCloudDataManager();
//        }
//        return mInstance;
//    }
//
//    public static void release(){
//        try{
//            if (null != mInstance){
//
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

}
