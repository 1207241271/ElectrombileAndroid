package com.xiaoantech.electrombile.ui.login.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.widget.Toast;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityLoginBinding;
import com.xiaoantech.electrombile.ui.login.ForgetPass.ForgetPassActivity;
import com.xiaoantech.electrombile.ui.login.Register.RegisterActivity;
import com.xiaoantech.electrombile.ui.main.FragmentMainActivity;

/**
 * Created by yangxu on 2016/10/28.
 */
public class LoginActivity extends BaseAcitivity implements LoginContract.View{
    private final static String         TAG = "LoginActivity";
    private ActivityLoginBinding        mBinding;
    private LoginContract.Presenter     mPresenter;
    private ProgressDialog              mProgressDialog;


    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);

    }

    @Override
    protected void initView() {
        mPresenter = new LoginPresenter(this);
        mBinding.setPresenter(mPresenter);
        mProgressDialog = new ProgressDialog(this);
    }

    public void setPresenter(LoginContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
        mProgressDialog.cancel();
    }

    @Override
    public void showWaitingDialog(String dialogString){
        mProgressDialog.setMessage(dialogString);
        mProgressDialog.show();
    }

    @Override
    public void loginSuccess() {
        Log.d(TAG,"Login Success!");
        mProgressDialog.cancel();
        //TODO:登录成功
        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void gotoRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoForgetPass() {
        Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoMainFragment() {
        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoBindDevice() {

    }

}
