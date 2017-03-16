package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.AutoLock;

import com.xiaoantech.electrombile.constant.EventBusConstant;
import com.xiaoantech.electrombile.event.cmd.AutoLockEvent;
import com.xiaoantech.electrombile.event.cmd.AutoPeriodEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by yangxu on 2017/2/25.
 */

public class AutoLockPresenter implements AutoLockContract.Presenter {
    private final static String TAG = "AutoLockPresenter";
    private AutoLockContract.View mAutoLock;
    private int   autoLockPeriod;


    protected AutoLockPresenter(AutoLockContract.View autoLock){
        this.mAutoLock = autoLock;
        mAutoLock.setPresenter(this);
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
    public void changeAutoLock() {
        if (!LocalDataManager.getInstance().getAutoLock()){
            MqttPublishManager.getInstance().autoLockOn(BasicDataManager.getInstance().getBindIMEI());
        }else {
            MqttPublishManager.getInstance().autoLockOff(BasicDataManager.getInstance().getBindIMEI());
        }
        mAutoLock.showWaitingDialog("正在设置");
    }

    @Override
    public void changeAutoLockPeriod(int period) {
        if (LocalDataManager.getInstance().getAutoLock()){
            MqttPublishManager.getInstance().setAutoPeriod(BasicDataManager.getInstance().getBindIMEI(),period);
            LocalDataManager.getInstance().setAutoLockPeriod(period);
            mAutoLock.changeAutoLockPeriodImg(period);
            autoLockPeriod = period;
            mAutoLock.showWaitingDialog("正在设置");
        }else {
            mAutoLock.showToast("请先打开自动设防");
        }
    }

    private void dealWithErrorCode(int errorCode){
        if (errorCode == 100){
            mAutoLock.showToast("服务器内部错误!");
        }else  if (errorCode == 102){
            mAutoLock.showToast("设备离线，请检查设备！");
            //TODO:ErrorShow
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoLockEvent(AutoLockEvent event){
        JSONObject object = event.getJsonObject();
        try {
        int code = object.getInt("code");
        if (code == 0){
            if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_AUTOLOCK_ON){
                mAutoLock.changeAutoLockState(true);
                LocalDataManager.getInstance().setAutoLock(true);
            }else if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_AUTOLOCK_OFF){
                mAutoLock.changeAutoLockState(false);
                LocalDataManager.getInstance().setAutoLock(false);
            }
        }else {
            dealWithErrorCode(code);
        }
    }catch (Exception e){
        e.printStackTrace();
    }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoPeriodEvent(AutoPeriodEvent event){
        JSONObject object = event.getJsonObject();
        try {
            int code = object.getInt("code");
            int Period = 0;
            if (code == 0){
                if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_AUTOPERIOD_SET){
                    mAutoLock.showToast("设置成功");
                    mAutoLock.changeAutoLockPeriodImg(autoLockPeriod);
                    LocalDataManager.getInstance().setAutoLockPeriod(autoLockPeriod);
//                    Period= object.getJSONObject("result").getInt("period");
//                    if (Period != 0){
//                        mAutoLock.changeAutoLockPeriodImg(Period);
//                        LocalDataManager.getInstance().setAutoLockPeriod(Period);
//                    }
                }else if (event.getCmdType() == EventBusConstant.cmdType.CMD_TYPE_AUTOPERIOD_GET){
                    mAutoLock.showToast("设置成功");
                    mAutoLock.changeAutoLockPeriodImg(autoLockPeriod);
                    LocalDataManager.getInstance().setAutoLockPeriod(autoLockPeriod);
//                    Period= object.getJSONObject("result").getInt("period");
//                    if (Period != 0) {
//                        mAutoLock.changeAutoLockPeriodImg(Period);
//                        LocalDataManager.getInstance().setAutoLockPeriod(Period);
//                    }
                }
            }else {
                dealWithErrorCode(code);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
