package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityPhoneAlarmBinding;

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
        setAlarmPhoneTest();
    }

    @Override
    public void setPresenter(PhoneAlarmContract.Presenter Presenter) {
        this.mPresenter = Presenter;
    }

    public void setAlarmPhoneTest(){
        Button button =(Button)findViewById(R.id.btn_start_test);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mPresenter.putAlarmPhoneFormHttp();
            }
        });
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
