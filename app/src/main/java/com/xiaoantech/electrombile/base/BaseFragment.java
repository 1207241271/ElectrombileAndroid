package com.xiaoantech.electrombile.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.xiaoantech.electrombile.constant.TimerConstant;

/**
 * Created by jk on 16-10-26.
 *
 * the base class of all fragment
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    protected ProgressDialog mProgressDialog;
    public abstract void initView();

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TimerConstant.TimerMessageWhat){
                showToast("连接超时");
            }
        }
    };

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(mContext);

    }
    public void showToast(String errorMeg) {
        Toast.makeText(mContext,errorMeg,Toast.LENGTH_SHORT).show();
        mProgressDialog.cancel();
        handler.removeMessages(TimerConstant.TimerMessageWhat);
    }

    public void showWaitingDialog(String dialogString) {
        try {
            mProgressDialog.setMessage(dialogString);
            mProgressDialog.show();
            handler.sendEmptyMessageDelayed(TimerConstant.TimerMessageWhat,TimerConstant.TimerIntervalShort);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideWaitingDialog() {
        mProgressDialog.cancel();
    }

}
