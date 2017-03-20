package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.xiaoantech.electrombile.ui.main.Unkown.Record.RecordActivity;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;
import com.xiaoantech.electrombile.widget.Dialog.CommonDialog;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;

/**
 * Created by yangxu on 2016/12/14.
 */

public class SettingManagerActivity extends BaseAcitivity implements SettingManagerContract.View {
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
        mBinding.setPresenter(mPresenter);((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("设置");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingManagerActivity.this.finish();
            }
        });
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
    public void gotoPhoneAlarm() {
        Intent intent = new Intent(SettingManagerActivity.this, PhoneAlarmAgreementActivity.class);
        startActivity(intent);
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
                startActivity(intent);
                AVUser.logOut();
            }
        }).create().show();
    }
}
