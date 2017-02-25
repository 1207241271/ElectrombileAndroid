package com.xiaoantech.electrombile.ui.login.Register;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityRegisterBinding;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.login.Login.LoginActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise.UserInfoReviseActivity;

/**
 * Created by yangxu on 2016/10/31.
 */

public class RegisterActivity extends BaseAcitivity implements RegisterContract.View {
    private final static String         TAG =   "RegisterActivity";
    private ActivityRegisterBinding     mBinding;
    private RegisterContract.Presenter  mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
    }

    @Override
    protected void initView() {
        mPresenter = new RegisterPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding .navigation.findViewById(R.id.navigation_title)).setText("注册");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(RegisterContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishRegister(){
        Intent intent = new Intent(RegisterActivity.this, UserInfoReviseActivity.class);
        startActivity(intent);
    }
}
