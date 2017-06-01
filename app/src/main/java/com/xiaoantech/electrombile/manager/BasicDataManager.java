package com.xiaoantech.electrombile.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.model.CarInfoModel;
import com.xiaoantech.electrombile.mqtt.MqttManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxu on 2016/11/7.
 */

public class BasicDataManager {
    private static BasicDataManager  mInstance;
    private String bindIMEI;
    private CarInfoModel bindCarInfo;
    private List<String> IMEIList;
    private List<CarInfoModel> carInfoList;
    private BasicDataManager(){
        this.bindIMEI = LocalDataManager.getInstance().getIMEI();
        this.IMEIList = LocalDataManager.getInstance().getIMEIList();
    }

    public static BasicDataManager getInstance() {
        if (null == mInstance){
            mInstance = new BasicDataManager();
        }
        return mInstance;
    }

    public void initFromLocal(){
        bindIMEI = LocalDataManager.getInstance().getIMEI();
        IMEIList = LocalDataManager.getInstance().getIMEIList();
        carInfoList = LocalDataManager.getInstance().getCarInfoList();
        if (carInfoList.size() > 0)
            bindCarInfo = carInfoList.get(0);
    }

    public void fetchBasicDataIMEIList(){
        AVQuery<AVObject> query = new AVQuery<>(LeanCloudConstant.BindTable);
        query.whereEqualTo(LeanCloudConstant.User,AVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (list.size() > 0){
                        //存储第一个IMEI
                        String firstIMEI = list.get(0).get(LeanCloudConstant.IMEI).toString();
                        LocalDataManager.getInstance().setIMEI(firstIMEI);


                        IMEIList = new ArrayList<String>();
                        carInfoList = new ArrayList<CarInfoModel>();
                        for (int i = 0; i < list.size(); i++){

                            String IMEI = list.get(i).get(LeanCloudConstant.IMEI).toString();
                            IMEIList.add(IMEI);

                            //设置车辆信息
                            Date bindDate = list.get(i).getDate(LeanCloudConstant.CreatedAt);
                            long bindTime = bindDate.getTime()/1000;
                            CarInfoModel carInfoModel = new CarInfoModel(IMEI,bindTime);
                            carInfoList.add(carInfoModel);
                            if (i == 0){
                                bindIMEI = IMEI;
                                bindCarInfo = carInfoModel;
                            }
                            //查询DID表中的车辆头像及车辆名称信息
                            fetchBasicDataCarName(IMEI,i);
                        }
                        LocalDataManager.getInstance().setIMEIList(IMEIList);
                        LocalDataManager.getInstance().setCarInfoList(carInfoList);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchBasicDataCarName(String imei, final int index){
        final CarInfoModel carInfoModel = carInfoList.get(index);

        AVQuery<AVObject> query = new AVQuery<>(LeanCloudConstant.DIDTable);
        query.whereEqualTo(LeanCloudConstant.IMEI,imei);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (list.size() > 0) {
                        AVObject object = list.get(0);
                        //设置名称
                        try {
                            if (object.get(LeanCloudConstant.CarName) != null) {
                                carInfoModel.setName(object.get(LeanCloudConstant.CarName).toString());
                            }else {
                                carInfoModel.setName(object.get(LeanCloudConstant.IMEI).toString());
                            }
                            carInfoList.set(index,carInfoModel);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }


                        //设置照片
                        if (null != object.get(LeanCloudConstant.Image)){
                           fetchBasicDataCarImage(object.getAVFile(LeanCloudConstant.Image),index);
                        }else {

                            Bitmap bitmap = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.othercar);
                            carInfoModel.setCropImage(bitmap);
                            //TODO:User didn't set picture
                        }
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fetchBasicDataCarImage(AVFile file,final int index){
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, AVException e) {
                if (null == e){
                    if (null != bytes){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        CarInfoModel carInfoModel = carInfoList.get(index);
                        carInfoModel.setCropImage(bitmap);
                        carInfoList.set(index,carInfoModel);
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setBindIMEI(String bindIMEI) {
        this.bindIMEI = bindIMEI;
    }
    public void setIMEIList(ArrayList<String> IMEIList){
        this.IMEIList = IMEIList;
    }
    public void setCarInfoList(ArrayList<CarInfoModel> carInfoList) {
        this.carInfoList = carInfoList;
    }

    public String getBindIMEI() {
        return bindIMEI;
    }

    public CarInfoModel getBindCarInfo() {
        return bindCarInfo;
    }

    public List<String> getIMEIList() {
        if (IMEIList == null){
            IMEIList = LocalDataManager.getInstance().getIMEIList();
        }
        return IMEIList;
    }

    public List<CarInfoModel> getCarInfoList() {
        return carInfoList;
    }

    public void changeBindIMEI(String IMEI,Boolean isBind){
        if (!isBind){
            IMEIList.add(0,IMEI);
            carInfoList.add(0,new CarInfoModel(IMEI));
        }else {
            for (int i = 0; i < IMEIList.size(); i++) {
                if (IMEIList.get(i).equals(IMEI)) {
                    CarInfoModel tmp = carInfoList.get(i);

                    IMEIList.remove(i);
                    IMEIList.add(0, IMEI);

                    carInfoList.remove(i);
                    carInfoList.add(0, tmp);
                }
            }
        }
        bindCarInfo = carInfoList.get(0);
        MqttManager.getInstance().unsubScribe(bindIMEI);
        MqttManager.getInstance().subscribe(IMEI);
        BasicDataManager.getInstance().setBindIMEI(IMEI);
        LocalDataManager.getInstance().setIMEI(IMEI);
        LocalDataManager.getInstance().setIMEIList(IMEIList);
        LocalDataManager.getInstance().setCarInfoList(carInfoList);
    }


}
