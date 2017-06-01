package com.xiaoantech.electrombile.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.constant.LayoutConstant;
import com.xiaoantech.electrombile.model.CarInfoModel;
import com.xiaoantech.electrombile.utils.BitmapUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxu on 2016/11/25.
 */

public class LocalDataManager {

    private final String SHARE_PREFERENCES = "SHARE_PREFERENCES";

    private final String MapType = "MapType";

    private final String Sex = "Sex";
    private final String Birth = "Birth";
    private final int    DefaultBirth = 19700101;
    private final String UserName = "UserName";
    private final String NickName = "NickName";
    private final String IdentityNum = "IdentityNum";

    private final String IMEI = "IMEI";
    private final String IMEIList = "IMEIList";
    private final String CarInfoList = "CarInfoList";

    private final String MQTTHost = "MQTTHost";
    private final String MQTTPort = "MQTTPort";
    private final String HTTPHost = "HTTPHost";
    private final String HTTPPort = "HTTPPort";

    public final String MQTTHost_Release = "mqtt.xiaoan110.com";
    public final String MQTTPort_Release = "1883";
    public final String HTTPHost_Release = "http://api.xiaoan110.com";
    public final String HTTPPort_Release = "8083";

    public final String MQTTHost_Test = "test.xiaoan110.com";
    public final String MQTTPort_Test = "1883";
    public final String HTTPHost_Test = "http://test.xiaoan110.com";
    public final String HTTPPort_Test = "8083";

    private int callerIndex;
    private boolean phoneAlarmOpen;

    private final String AutoLock = "AutoLock";
    private final String AutoLockPeriod = "AutoLockPeriod";
    private final String LatestStatus = "latestStatus";
    private final String TodayItinerary = "todayItinerary";

    private final String IsAddContract = "IsAddContract";
    private final String ContractIndex = "ContractIndex";

    private SharedPreferences sharedPreferences;

    private  static LocalDataManager mInstance = null;

    private final String IsFirstLaunch = "isFirstLaunch";
    private final String IsRelevanceOn = "isRelevance";

    private final String FenceStatus = "FenceStatus";
    private final String LockStatus = "FenceStatus";


    public static LocalDataManager getInstance() {
        if (mInstance == null){
            mInstance = new LocalDataManager();
        }
        return mInstance;
    }

    private LocalDataManager(){
        Context context = App.getContext();
        sharedPreferences = context.getSharedPreferences(SHARE_PREFERENCES,Context.MODE_PRIVATE);
    }

    public void cleanDevice(){
        setIMEI("");
    }


    public void setIMEI(String imei) {
        sharedPreferences.edit().putString(IMEI,imei).apply();
    }

    public String getIMEI() {
        return sharedPreferences.getString(IMEI,"");
    }

    public int getCallerIndex(){
        return this.callerIndex;
    }

    public void setCallerIndex(int caller){
        this.callerIndex = caller;
    }

    public boolean getPhoneAlarmOpen(){
        return this.phoneAlarmOpen;
    }

    public void setPhoneAlarmOpen(boolean isOn){
        this.phoneAlarmOpen = isOn;
    }

    public void setIMEIList(List<String> imeiList) {
        if (imeiList == null){
            sharedPreferences.edit().putString(IMEIList,"").apply();
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (String IMEI: imeiList){
            jsonArray.put(IMEI);
        }
        sharedPreferences.edit().putString(IMEIList,jsonArray.toString()).apply();
    }

    public List<String> getIMEIList(){
        List<String> imeiList = new ArrayList<>();
        JSONArray jsonArray;
        try{
            jsonArray = new JSONArray(sharedPreferences.getString(IMEIList,""));
            for (int i = 0; i < jsonArray.length(); i++){
                imeiList.add(jsonArray.getString(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return imeiList;
    }

    public void setCarInfoList(List<CarInfoModel> carInfoList){
        if (carInfoList == null){
            sharedPreferences.edit().putString(CarInfoList,"").apply();
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        try {
            for (CarInfoModel carInfoModel:carInfoList){
                jsonObject = new JSONObject();
                jsonObject.put("name",carInfoModel.getName());
                jsonObject.put("IMEI",carInfoModel.getIMEI());
                jsonObject.put("bindTime",carInfoModel.getBindTime());
                jsonObject.put("cropImage", BitmapUtil.convertBitmapTOString(carInfoModel.getCropImage()));
                jsonArray.put(jsonObject);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        sharedPreferences.edit().putString(CarInfoList,jsonArray.toString()).apply();
    }

    public List<CarInfoModel> getCarInfoList(){
        List<CarInfoModel> carInfoList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(sharedPreferences.getString(CarInfoList," "));
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CarInfoModel carInfoModel = new CarInfoModel(jsonObject.getString("IMEI"),jsonObject.getLong("bindTime"));
                carInfoModel.setCropImage(BitmapUtil.convertStringToBitmap(jsonObject.getString("cropImage")));
                carInfoModel.setName(jsonObject.getString("name"));
                carInfoList.add(carInfoModel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return carInfoList;
    }

    public void setMQTTHost(String mqttHost){
        sharedPreferences.edit().putString(MQTTHost,mqttHost).apply();
    }
    public String getMQTTHost() {
        return sharedPreferences.getString(MQTTHost,MQTTHost_Release);
    }

    public void setMQTTPort(String mqttPort) {
        sharedPreferences.edit().putString(MQTTPort,mqttPort).apply();
    }
    public String getMQTTPort() {
        return sharedPreferences.getString(MQTTPort,MQTTPort_Release);
    }

    public void setHTTPHost(String httpHost) {
        sharedPreferences.edit().putString(HTTPHost,httpHost).apply();
    }
    public String getHTTPHost(){
        return sharedPreferences.getString(HTTPHost,HTTPHost_Release);
    }
    public void setHTTPPort(String httpPort){
        sharedPreferences.edit().putString(HTTPPort,httpPort).apply();
    }
    public String getHTTPPort(){
        return sharedPreferences.getString(HTTPPort,HTTPPort_Release);
    }

    public LayoutConstant.MapType getMapType(){
        int mapTypeInt = sharedPreferences.getInt(MapType,0);
        switch (mapTypeInt){
            case 0:
                return LayoutConstant.MapType.MAP_TYPE_NORMAL;
            case 1:
                return LayoutConstant.MapType.MAP_TYPE_SATELLITE;
            case 2:
                return LayoutConstant.MapType.MAP_TYPE_3D;
            default:
                return LayoutConstant.MapType.MAP_TYPE_NORMAL;
        }
    }

    public void setMapType(LayoutConstant.MapType mapType){
        int mapTypeInt;
        switch (mapType){
            case MAP_TYPE_NORMAL:
                mapTypeInt = 0;
                break;
            case MAP_TYPE_SATELLITE:
                mapTypeInt = 1;
                break;
            case MAP_TYPE_3D:
                mapTypeInt = 2;
                break;
            default:
                mapTypeInt = 1;
        }
        sharedPreferences.edit().putInt(MapType,mapTypeInt).apply();
    }

    public void setSex(boolean isMale){
        sharedPreferences.edit().putBoolean(Sex,isMale).apply();
    }

    public boolean getSex(){
        return sharedPreferences.getBoolean(Sex,true);
    }

    //Birth存储并没有按照时间戳存储，而是使用8位数存储，格式为yyyymmdd,使用时再进行解析
    public void setBirth(int birth){
        sharedPreferences.edit().putInt(Birth,birth).apply();
    }

    public int getBirth(){
        return sharedPreferences.getInt(Birth,DefaultBirth);
    }

    public void setUserName(String userName) {
        sharedPreferences.edit().putString(UserName,userName).apply();
    }

    public String getUserName(){
        return sharedPreferences.getString(UserName,"名称");
    }

    public void setNickName(String nickName){
        sharedPreferences.edit().putString(NickName,nickName).apply();
    }

    public String getNickName(){
        return sharedPreferences.getString(NickName,"昵称");
    }

    public void setIdentityNum(String identityNum){
        sharedPreferences.edit().putString(IdentityNum,identityNum).apply();
    }

    public String getIdentityNum(){
        return sharedPreferences.getString(IdentityNum,"");
    }

    public void setAutoLock(boolean isOn){
        sharedPreferences.edit().putBoolean(AutoLock,isOn).apply();
    }

    public boolean getAutoLock(){
        return sharedPreferences.getBoolean(AutoLock,false);
    }

    public void setAutoLockPeriod(int period){
        sharedPreferences.edit().putInt(AutoLockPeriod,period).apply();
    }

    public int getAutoLockPeriod(){
        return sharedPreferences.getInt(AutoLockPeriod,5);
    }

    public void setLatestStatus(String latestStatus){
        sharedPreferences.edit().putString(LatestStatus,latestStatus).apply();
    }

    public String getLatestStatus(){
        return sharedPreferences.getString(LatestStatus,"");
    }

    public void setTodayItinerary(int todayItinerary){
        sharedPreferences.edit().putInt(TodayItinerary,todayItinerary).apply();
    }

    public int getTodayItinerary(){
        return sharedPreferences.getInt(TodayItinerary,0);
    }

    public void setIsFirstLaunch(Boolean isFirstLaunch){
        sharedPreferences.edit().putBoolean(IsFirstLaunch,isFirstLaunch).apply();
    }

    public boolean getIsFirstLaunch(){
        return sharedPreferences.getBoolean(IsFirstLaunch,true);
    }

    public void setIsAddContract(boolean isAddContract){
        sharedPreferences.edit().putBoolean(IsAddContract,isAddContract).apply();
    }

    public boolean getIsAddContract(){
        return sharedPreferences.getBoolean(IsAddContract,false);
    }

    public void setContractIndex(int contractIndex){
        sharedPreferences.edit().putInt(ContractIndex,contractIndex).apply();
    }

    public int getContractIndex(){
        return sharedPreferences.getInt(ContractIndex,0);
    }

    public void setIsRelevanceOn(boolean isRelevanceOn){
        sharedPreferences.edit().putBoolean(IsRelevanceOn,isRelevanceOn).apply();
    }

    public boolean getIsRelevanceOn(){
        return sharedPreferences.getBoolean(IsRelevanceOn,false);
    }

    public void setFenceStatus(boolean fenceStatus){
        sharedPreferences.edit().putBoolean(FenceStatus,fenceStatus).apply();
    }

    public boolean getFenceStatus(){
        return sharedPreferences.getBoolean(FenceStatus,false);
    }

    public void setLockStatus(boolean lockStatus){
        sharedPreferences.edit().putBoolean(LockStatus,lockStatus).apply();
    }

    public boolean getLockStatus(){
        return sharedPreferences.getBoolean(LockStatus,false);
    }
}
