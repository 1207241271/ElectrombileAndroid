package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityPhoneAlarmBinding;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmResend.PhoneAlarmNoreceiveActivity;

/**
 * Created by yangxu on 2017/2/25.
 */

public class PhoneAlarmActivity extends BaseAcitivity implements PhoneAlarmContract.View{
    private ActivityPhoneAlarmBinding mBinding;
    private PhoneAlarmContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_phone_alarm);
    }

    @Override
    protected void initView() {
        mPresenter = new PhoneAlarmPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("电话推送测试");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAlarmActivity.this.finish();
            }
        });

        String phoneNum = AVUser.getCurrentUser().getUsername();
        String secretPhoneNum = phoneNum.substring(0,3) + "****" + phoneNum.substring(7);
        mBinding.txtPhoneNum.setText(secretPhoneNum);

        checkResend(getIntent().getExtras());
    }

    private void checkResend(Bundle bundle){
        if (bundle != null){
            boolean isResend = bundle.getBoolean("resend");
            if (isResend){
                mPresenter.AddCallerIndex();
            }
        }
    }

    @Override
    public void changeCutDownStatus(int num) {
        if (num>0 && num<60){
            mBinding.txtCutdown.setText(num + "");
        }
        if (num == 0){
            mBinding.txtCutdown.setText("60");
            mBinding.btnNoreceive.setEnabled(true);
            mBinding.btnNoreceive.setBackground(getResources().getDrawable(R.drawable.corners_bg_green));
        }
        if (num == 59){
            mBinding.btnStartTest.setEnabled(false);
            mBinding.btnStartTest.setBackground(getResources().getDrawable(R.drawable.corners_bg_gray));
            mBinding.btnNoreceive.setEnabled(false);
            mBinding.btnNoreceive.setBackground(getResources().getDrawable(R.drawable.corners_bg_gray));
        }
    }

    @Override
    public void gotoResendActivity() {
        Intent intent = new Intent(PhoneAlarmActivity.this, PhoneAlarmNoreceiveActivity.class);
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public void setPresenter(PhoneAlarmContract.Presenter Presenter) {
        this.mPresenter = Presenter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

}
