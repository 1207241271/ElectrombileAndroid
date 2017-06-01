package com.xiaoantech.electrombile.ui.Other;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.utils.VibratorUtil;
import com.xiaoantech.electrombile.widget.SlidingButton;

/**
 * Created by yangxu on 2017/3/23.
 */

public class OutageActivity extends Activity {
    MediaPlayer mPlayer;
    private SlidingButton mSlidingButton;
    private TextView tv_sliding;
    private TextView tv_alarm;
    private Animation operatingAnim;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            VibratorUtil.VibrateCancle(OutageActivity.this);
            mPlayer.stop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm_outage);
        super.onCreate(savedInstanceState);
        initViews();
        initEvents();
        alarm();
    }

    private void alarm() {
        VibratorUtil.Vibrate(this, new long[]{1000, 2000, 2000, 1000}, true);
        //播放警铃
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm);
        mPlayer.setLooping(true);
        mPlayer.start();
        Message msg = Message.obtain();
        mHandler.sendMessageDelayed(msg,1000*60);
    }

    public void initViews() {
        mSlidingButton = (SlidingButton) this.findViewById(R.id.mainview_answer_1_button);
        tv_sliding = (TextView) findViewById(R.id.tv_sliding);
        tv_alarm = (TextView) findViewById(R.id.tv_alarm);
    }
    public void initEvents() {
//        startMqttClient();
//        mac = mqttConnectManager.getMac();
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        tv_alarm.startAnimation(operatingAnim);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mqttConnectManager.sendMessage(mCenter.cmdFenceOff(), setManager.getIMEI());
        mPlayer.stop();
        VibratorUtil.VibrateCancle(OutageActivity.this);
        OutageActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSlidingButton.handleActivityEvent(event)) {
            //stop alarm
            tv_sliding.setText("停止告警！");
            tv_alarm.clearAnimation();
            VibratorUtil.VibrateCancle(OutageActivity.this);
            mPlayer.stop();
            OutageActivity.this.finish();
        } else {
            tv_sliding.setText("滑动关闭报警");
            tv_alarm.startAnimation(operatingAnim);
        }
        return super.onTouchEvent(event);
    }
}
