package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass;

import android.databinding.DataBindingUtil;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityChangePassBinding;

/**
 * Created by yangxu on 2017/1/4.
 */

public class ChangePassActivity extends BaseAcitivity implements ChangePassContract.View{
    private ActivityChangePassBinding mBinding;
    private ChangePassContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);
    }

    @Override
    protected void initView() {
        mPresenter = new ChangePassPresenter(this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void setPresenter(ChangePassContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
