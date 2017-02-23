package com.xiaoantech.electrombile.ui.login.Login;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by yangxu on 2016/10/30.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final static String TAG = "LoginPresenter";
    private LoginContract.View  mLoginView;

    protected   LoginPresenter(LoginContract.View LoginView){
        this.mLoginView = LoginView;
        mLoginView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
    @Override
    public void login(String userphone,String password){
        if (StringUtil.isEmpty(userphone) || userphone.length() != 11){
            mLoginView.showToast("请输入正确的手机号！");
            return;
        }
        if (StringUtil.isEmpty(password)){
            mLoginView.showToast("请输入密码！");
            return;
        }

        mLoginView.showWaitingDialog("正在登录，请稍后");

        AVUser.logInInBackground(userphone, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null){
                    if(avUser != null){
                        if (avUser.isMobilePhoneVerified()){
                            mLoginView.loginSuccess();
                        }else {
                            mLoginView.showToast("该手机号未进行验证！");
                        }
                    }else {
                        mLoginView.showToast("登录超时，请检查网络连接！");
                    }
                }else {
                    Log.e(TAG,e.toString());
                    switch (e.getCode()){
                        case AVException.USERNAME_PASSWORD_MISMATCH:
                            mLoginView.showToast("用户名与密码不匹配！");
                            break;
                        case AVException.USER_DOESNOT_EXIST:
                            mLoginView.showToast("该用户不存在！");
                            break;
                        default:
                            mLoginView.showToast("登录超时，请检查网络连接！");
                            break;
                    }
                }
            }
        });
    }

    private void loginSuccess(AVUser user){
        AVQuery<AVObject> query = new AVQuery<AVObject>(LeanCloudConstant.BindTable);
        query.whereEqualTo(LeanCloudConstant.User,user);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (null == e){
                    if (list.size() > 0){
                        //TODO:has
                        BasicDataManager.getInstance().fetchBasicDataIMEIList();
                        mLoginView.gotoMainFragment();
                    }else {
                        //TODO:Not have
                        mLoginView.gotoBindDevice();
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void register() {
        if (MqttManager.getInstance().createConnect()){
            MqttManager.getInstance().subscribe(BasicDataManager.getInstance().getBindIMEI());
            Log.d(TAG,"subscribe success");
        }else {
            Log.d(TAG, "subscribe fail");
        }
        mLoginView.gotoRegister();
    }

    @Override
    public void forgetPass() {
//        MqttPublishManager.getInstance().getStatus("865067022373032");
        mLoginView.gotoForgetPass();
    }

}
