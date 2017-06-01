package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmResend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmActivity;

/**
 * Created by 73843 on 2017/3/20.
 */

public class PhoneAlarmNoreceiveActivity extends BaseAcitivity{

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_phone_alarm_noreceive);
        View view = (View)findViewById(R.id.navigation);
        ((TextView)view.findViewById(R.id.navigation_title)).setText("未收到测试电话");
        ((RelativeLayout)view.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAlarmNoreceiveActivity.this.finish();
            }
        });
        Button button = (Button)findViewById(R.id.btn_try);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PhoneAlarmNoreceiveActivity.this, PhoneAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("resend", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
