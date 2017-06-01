package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.net.Uri;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.databinding.ActivityPhoneAlarmAgreementBinding;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement.PhoneAlarmAgreementContract;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmContract;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmPresenter;

/**
 * Created by 73843 on 2017/3/18.
 */

public class PhoneAlarmAgreementActivity extends BaseAcitivity implements PhoneAlarmAgreementContract.View {
    private ActivityPhoneAlarmAgreementBinding mBinding;
    private PhoneAlarmAgreementContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_phone_alarm_agreement);
    }

    @Override
    protected void initView() {
        mPresenter = new PhoneAlarmAgreementPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("电话报警通知协议");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAlarmAgreementActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(PhoneAlarmAgreementContract.Presenter Presenter) {
        this.mPresenter = Presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void toPhoneAlarmActivity(){
        Intent intent = new Intent(PhoneAlarmAgreementActivity.this, PhoneAlarmActivity.class);
        this.finish();
        startActivity(intent);
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

    public void AddContacts(){
        Log.e("AddContacts", "yes");
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = getApplicationContext().getContentResolver();
        ContentValues values = new ContentValues();
        long contactId = ContentUris.parseId(resolver.insert(uri, values));

        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/name");
        values.put("data2", "小安宝报警");
        resolver.insert(uri, values);

        //caller:0
        values.clear();
        values.put("raw_contact_id", contactId);
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");
        values.put("data1", "01052729758");
        resolver.insert(uri, values);

    }
}
