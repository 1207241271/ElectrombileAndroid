package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivitySettingBinding;
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
        mBinding.setPresenter(mPresenter);
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
