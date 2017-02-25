package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCar;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityFindCarBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;
import com.xiaoantech.electrombile.widget.Dialog.SimpleDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangxu on 2017/1/6.
 */

public class FindCarActivity extends BaseAcitivity implements FindCarContract.View {
    private ActivityFindCarBinding mBinding;
    private FindCarContract.Presenter   mPresenter;

    private TextView txt_cutDown;
    private TextView txt_intensity;
    private SimpleDialog.Builder    cutDownDialog;
    private CertainDialog.Builder   signalGuideDialog;
    private Timer timer;
    private int secondTimeLeft;
    private int intensityMax;
    private int collectCount;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            secondTimeLeft --;
            if (secondTimeLeft <= 0){
                timer.cancel();
                MqttPublishManager.getInstance().seekOff(BasicDataManager.getInstance().getBindIMEI());
                cutDownDialog.getSimpleDialog().dismiss();
                setIntensityResult(intensityMax);
            }else {
                txt_cutDown.setText(secondTimeLeft + "s");
            }
        }
    };



    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_car);
    }

    @Override
    protected void initView() {
        mPresenter = new FindCarPresenter(this);
        mBinding.setPresenter(mPresenter);

        userGuide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    private void userGuide(){
        View view = getLayoutInflater().inflate(R.layout.findcar_guide3,null);
        CertainDialog.Builder certainDialog = new CertainDialog.Builder(this);
        certainDialog.setTitle("精确找车").setContentView(view).setPositiveButton("开始采集", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                initCountDownDialog();
                TimeCutDown();
                dialog.dismiss();
                collectCount = 1;

            }
        }).create().show();
    }


    private void initCountDownDialog(){
        View view = getLayoutInflater().inflate(R.layout.dialog_cutdown,null);

        txt_cutDown = (TextView)view.findViewById(R.id.tv_countdownSecond);
        txt_intensity = (TextView)view.findViewById(R.id.tv_intensity);

        cutDownDialog = new SimpleDialog.Builder(this);
        cutDownDialog.setContentView(view).setTitle("采集数据").create().show();
        MqttPublishManager.getInstance().seekOn(BasicDataManager.getInstance().getBindIMEI());
        cutDownDialog.getSimpleDialog().setCancelable(false);
    }

    private void initSignalGuideDialog(){
        View view = getLayoutInflater().inflate(R.layout.findcar_user_guide,null);

        signalGuideDialog = new CertainDialog.Builder(this);
        signalGuideDialog.setContentView(view).setTitle("进行第二次数据采集").setPositiveButton("我已移动了10米", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                cutDownDialog.getSimpleDialog().show();
                TimeCutDown();
            }
        });

    }

    private void TimeCutDown(){
        secondTimeLeft = 30;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        },1000,1000);
    }

    @Override
    public void changeIntensity(int intensity) {
        if (intensity > intensityMax){
            intensityMax = intensity;
        }
        txt_intensity.setText("" + intensity);
    }

    public void setIntensityResult(int intensity){
        switch (collectCount){
            case 1:
                ((TextView)mBinding.llResult1.findViewById(R.id.tv_signalResult)).setText(intensity + "");
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    @Override
    public void setPresenter(FindCarContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
