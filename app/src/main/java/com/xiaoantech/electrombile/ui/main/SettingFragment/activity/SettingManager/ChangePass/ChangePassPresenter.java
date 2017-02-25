package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass;

import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise.CarInfoReviseContract;

/**
 * Created by yangxu on 2017/1/4.
 */

public class ChangePassPresenter implements ChangePassContract.Presenter {
    private final static String TAG = "ChangePassPresenter";
    private ChangePassContract.View mChangePassView;

    private final static int ChangeSuccess = 101;
    private final static int ChangeFail = 102;

    protected ChangePassPresenter(ChangePassContract.View changePassView){
        this.mChangePassView = changePassView;
        mChangePassView.setPresenter(this);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ChangeSuccess){
                mChangePassView.showToast("修改成功");
            }else {
                mChangePassView.showToast("修改失败");
            }
        }
    };
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void changePassword(String pass1, String pass2) {
        if (pass1 == null || pass2 == null){
            mChangePassView.showToast("输入密码为空！");
            return;
        }
        if (!pass1.equals(pass2)){
            mChangePassView.showToast("两次输入密码不同！");
            return;
        }

        mChangePassView.showWaitingDialog("正在修改");

        AVUser user = AVUser.getCurrentUser();
        user.setPassword(pass1);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null){
                    mHandler.sendEmptyMessage(ChangeSuccess);
                }else {
                    mHandler.sendEmptyMessage(ChangeFail);
                    e.printStackTrace();
                }
            }
        });

    }
}
