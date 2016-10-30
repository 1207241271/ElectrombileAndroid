package com.xiaoantech.electrombile.ui.login.LoginMain;



import android.util.Log;

import com.xiaoantech.electrombile.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangxu on 2016/10/28.
 */
public class LoginMainPresenter implements LoginMainContract.Presenter{
    private LoginMainContract.View  mLoginMainView;
    private EventBus    mEventBus;

    public LoginMainPresenter(LoginMainContract.View mLoginMainView){
        this.mLoginMainView = mLoginMainView;
        this.mLoginMainView.setPresenter(this);
    }

    @Override
    public void subscribe(){
        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
    }
    @Override
    public void unsubscribe(){
        mEventBus.unregister(this);
    }

    @Override
    public int getBackgroundImage(){
        return R.drawable.back ;
    }
    @Override
    public void register(){
        Log.d("test","register");
    }
    @Override
    public void login(){

    }
}
