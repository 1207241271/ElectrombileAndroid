package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager;

import android.content.Intent;
import android.util.Log;

import com.xiaoantech.electrombile.event.LeanCloud.DeleteEvent;
import com.xiaoantech.electrombile.leancloud.LeanCloudManager;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.JPushManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.ui.login.Login.LoginActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.SettingManagerActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.SettingManagerContract;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yangxu on 2016/12/14.
 */

public class CarManagerPresenter implements CarManagerContract.Presenter{
    private final static String TAG = "SettingManagerPresenter";
    private CarManagerContract.View mCarManagerView;
    private int indexToDelete;

    protected  CarManagerPresenter(CarManagerContract.View carManagerView){
        this.mCarManagerView = carManagerView;
        carManagerView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void deleteDevice(int index) {
        indexToDelete = index;
        mCarManagerView.showDeleteDialog();
    }

    @Override
    public void delete() {
        LeanCloudManager.getInstance().deleteIMEI(BasicDataManager.getInstance().getIMEIList().get(indexToDelete));
        mCarManagerView.showWaitingDialog("正在解除绑定");
    }

    private void deleteLocalData(){
        BasicDataManager.getInstance().getIMEIList().remove(indexToDelete);
        BasicDataManager.getInstance().getCarInfoList().remove(indexToDelete);
        LocalDataManager.getInstance().setIMEIList(BasicDataManager.getInstance().getIMEIList());
        LocalDataManager.getInstance().setCarInfoList(BasicDataManager.getInstance().getCarInfoList());
        if (LocalDataManager.getInstance().getIMEIList().size() == 0){
            LocalDataManager.getInstance().setIMEI(null);
            BasicDataManager.getInstance().setBindIMEI(null);
            mCarManagerView.quit();
        }else if(indexToDelete == 0){
            String IMEI = BasicDataManager.getInstance().getIMEIList().get(0);
            BasicDataManager.getInstance().changeBindIMEI(IMEI,true);
            LocalDataManager.getInstance().setIMEI(IMEI);
            JPushManager.getInstance().setPushAlias(IMEI);
            mCarManagerView.reSetBind();
        }else {
            mCarManagerView.reSetOther();
        }
        mCarManagerView.showToast("解除绑定成功");
    }


    @Override
    public void addDevice() {
        mCarManagerView.addDevice();
    }

    @Override
    public void gotoBindedCarInfo() {
        mCarManagerView.gotoBindedCarInfo(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteEvent(DeleteEvent event){
        if (event.isDelete()){
            deleteLocalData();
        }else {
            mCarManagerView.showToast("解除绑定失败");
        }
    }
}
