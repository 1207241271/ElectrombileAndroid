package com.xiaoantech.electrombile.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.xiaoantech.electrombile.constant.TimerConstant;

/**
 * Created by jk on 16-10-26.
 *
 * the base class of all activity
 */

public abstract class BaseAcitivity extends AppCompatActivity {
    protected ProgressDialog mProgressDialog;
    protected BackKeyStatus isKeyDown = BackKeyStatus.BACK_KEY_STATUS_START;
    protected abstract void initBefore();

    protected abstract void initView();

    protected abstract void bindData();

    enum BackKeyStatus{
        BACK_KEY_STATUS_START,
        BACK_KEY_STATUS_DOWN,
        BACK_KEY_STATUS_SIMPLE,
        BACK_KEY_STATUS_DOUBLE
    }

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TimerConstant.TimerMessageWhat){
                showToast("连接超时");
            }else if (msg.what == 0x01){
                if (isKeyDown == BackKeyStatus.BACK_KEY_STATUS_DOWN) {
                    isKeyDown = BackKeyStatus.BACK_KEY_STATUS_SIMPLE;
                    onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBefore();
        bindData();
        initView();
        mProgressDialog = new ProgressDialog(this);
    }

    public void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
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
        handler.removeMessages(TimerConstant.TimerMessageWhat);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            switch (isKeyDown){
                case BACK_KEY_STATUS_START:
                    isKeyDown = BackKeyStatus.BACK_KEY_STATUS_DOWN;
                    handler.sendEmptyMessageDelayed(0x01,500);
                    return true;
                case BACK_KEY_STATUS_DOWN:
                    handler.removeMessages(0x01);
                    isKeyDown = BackKeyStatus.BACK_KEY_STATUS_START;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                    return true;
                case BACK_KEY_STATUS_SIMPLE:
                    isKeyDown = BackKeyStatus.BACK_KEY_STATUS_START;
                    this.finish();
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
