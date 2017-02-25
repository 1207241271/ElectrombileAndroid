package com.xiaoantech.electrombile.ui.login.ForgetPass;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityForgetpassBinding;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;

/**
 * Created by yangxu on 2016/10/31.
 */

public class ForgetPassActivity extends BaseAcitivity implements ForgetPassContract.View{
    private static final String             TAG =   "ForgetPassActivity";
    private ActivityForgetpassBinding       mBinding;
    private ForgetPassContract.Presenter    mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgetpass);
    }

    @Override
    protected void initView() {
        mPresenter = new ForgetPassPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("重置密码");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(ForgetPassContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishResetPass(){
        Log.d(TAG,"resetPass success");
    }
}
