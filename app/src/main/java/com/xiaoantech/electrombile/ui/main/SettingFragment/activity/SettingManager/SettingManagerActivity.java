package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivitySettingBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.login.Login.LoginActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.AutoLock.AutoLockActivity;

import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass.ChangePassActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement.PhoneAlarmAgreementActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.Record.RecordActivity;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;
import com.xiaoantech.electrombile.widget.Dialog.CommonDialog;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;

import java.util.Set;

/**
 * Created by yangxu on 2016/12/14.
 */

public class SettingManagerActivity extends BaseAcitivity implements SettingManagerContract.View,CompoundButton.OnCheckedChangeListener {
    private ActivitySettingBinding mBinding;
    private SettingManagerContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
    }

    @Override
    protected void initView() {
        mPresenter = new SettingManagerPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("设置");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingManagerActivity.this.finish();
            }
        });
        mBinding.switchRelevence.setOnCheckedChangeListener(this);
        mBinding.switchLock.setOnCheckedChangeListener(this);
        mBinding.switchRearwheel.setOnCheckedChangeListener(this);

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(mBinding.switchRelevence)){
            if (isChecked){
                CustomDialog.Builder customDialog = new  CustomDialog.Builder(SettingManagerActivity.this);
                customDialog.setTitle("温馨提示").setMessage("该操作将存在风险隐患，小偷可用万能钥匙开启电门预警系统将无法自动识别该项情况").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.relevenceSwitchChange(true);
                        dialog.dismiss();
                    }
                }).create().show();
            }else {
                mPresenter.relevenceSwitchChange(false);
            }
        }else if (buttonView.equals(mBinding.switchLock)){
            if (isChecked){
                mPresenter.lockSwitchChange(true);
            }else {
                mPresenter.lockSwitchChange(false);
            }
        }else if (buttonView.equals(mBinding.switchRearwheel)){
            if (isChecked){
                mPresenter.rearWheelSwitchChange(true);
            }else {
                mPresenter.rearWheelSwitchChange(false);
            }
        }
    }
    @Override
    public void setPresenter(SettingManagerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void gotoChangePass() {
        Intent intent = new Intent(SettingManagerActivity.this, ChangePassActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoAutoLock() {
        Intent intent = new Intent(SettingManagerActivity.this, AutoLockActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoRecord() {
        Intent intent = new Intent(SettingManagerActivity.this, RecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoPhoneAlarm() {
        mPresenter.isPhoneAlarmOpen();
    }

    @Override
    public void logout() {
        CustomDialog.Builder customDialog = new  CustomDialog.Builder(this);
        customDialog.setTitle("退出").setMessage("确认退出登录？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MqttManager.getInstance().unsubScribe(BasicDataManager.getInstance().getBindIMEI());
                LocalDataManager.getInstance().cleanDevice();
                Intent intent = new Intent(SettingManagerActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SettingManagerActivity.this.finish();
                startActivity(intent);
                AVUser.logOut();
            }
        }).create().show();
    }


    @Override
    public void setSwitch(boolean isChecked) {
        mBinding.switchRelevence.setOnCheckedChangeListener(null);
        mBinding.switchRelevence.setChecked(isChecked);
        mBinding.switchRelevence.setOnCheckedChangeListener(this);
    }

    @Override
    public void setLock(boolean isChecked) {
        mBinding.switchLock.setOnCheckedChangeListener(null);
        mBinding.switchLock.setChecked(isChecked);
        mBinding.switchLock.setOnCheckedChangeListener(this);
    }

    @Override
    public void setWheelLock(boolean isChecked) {
        mBinding.switchRearwheel.setOnCheckedChangeListener(null);
        mBinding.switchRearwheel.setChecked(isChecked);
        mBinding.switchRearwheel.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        setSwitch(LocalDataManager.getInstance().getIsRelevanceOn());
        setLock(LocalDataManager.getInstance().getLockStatus());
        setWheelLock(LocalDataManager.getInstance().getWheelLock());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    public void PhoneAlarmOpen(boolean isOn){
        Intent intent = new Intent();
        if (isOn == true){
            intent.setClass(SettingManagerActivity.this, PhoneAlarmActivity.class);
        }else {
            intent.setClass(SettingManagerActivity.this, PhoneAlarmAgreementActivity.class);
        }
        startActivity(intent);
    }
}
