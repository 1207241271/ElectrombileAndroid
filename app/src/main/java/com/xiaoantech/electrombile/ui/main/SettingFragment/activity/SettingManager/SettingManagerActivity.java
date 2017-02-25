package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivitySettingBinding;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass.ChangePassActivity;

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
}
