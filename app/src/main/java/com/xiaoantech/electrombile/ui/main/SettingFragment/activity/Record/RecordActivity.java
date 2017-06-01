package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.Record;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.constant.TimerConstant;
import com.xiaoantech.electrombile.databinding.ActivityRecordBinding;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory.MapListActivity;
import com.xiaoantech.electrombile.widget.Waveform.RendererFactory;

import java.io.IOException;

/**
 * Created by yangxu on 2017/3/2.
 */

public class RecordActivity extends BaseAcitivity implements RecordContract.View{
    private ActivityRecordBinding mBinding;
    private RecordContract.Presenter mPresenter;
    private Visualizer mVisualizer;
    private MediaPlayer mMediaPlayer;


    private static final int CAPTURE_SIZE = 256; // 获取这些数据, 用于显示

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_record);
    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.navigation_title)).setText("远程窃听");
        ((RelativeLayout)findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.finish();
            }
        });
        mPresenter = new RecordPresenter(this);
        mBinding.setPresenter(mPresenter);
        mMediaPlayer = new MediaPlayer();
        RendererFactory rendererFactory = new RendererFactory();
        mBinding.wiretapWvWaveform.setRenderer(rendererFactory.createSimpleWaveformRender(ContextCompat.getColor(this, R.color.red), ContextCompat.getColor(this,R.color.appOrange)));
    }

    @Override
    public void setPresenter(RecordContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void startRecord() {
        changeButtonStatus(mBinding.btnPlay,false);
        changeButtonStatus(mBinding.btnStop,true);
        hideWaitingDialog();
        mBinding.txtTitle.setText("正录音");
        mBinding.btnPlay.setText("正录音");
    }

    @Override
    public void stopRecord() {
        changeButtonStatus(mBinding.btnStop,false);
        mBinding.txtTitle.setText("已结束");
        mBinding.btnPlay.setText("播放");
        showWaitingDialog("正在下载");
        handler.removeMessages(TimerConstant.TimerMessageWhat);
    }

    @Override
    public void startPlay(String filePath) {
        try {
            hideWaitingDialog();
            startVisualiser();
            mBinding.txtCutdown.setVisibility(View.INVISIBLE);
            changeButtonStatus(mBinding.btnStop,true);
            mBinding.btnPlay.setText("暂停");
            changeButtonStatus(mBinding.btnPlay,true);
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(filePath);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void changePlayStatus() {
        if (mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            mBinding.btnPlay.setText("播放");
        }else {
            mMediaPlayer.start();
            mBinding.btnPlay.setText("暂停");
        }
    }

    @Override
    public void stopPlay() {
        mMediaPlayer.stop();
    }

    @Override
    public void resetView() {
        mMediaPlayer.stop();
        changeButtonStatus(mBinding.btnPlay,true);
        changeButtonStatus(mBinding.btnStop,false);
        mBinding.btnPlay.setText("录音");
        mBinding.txtTitle.setText("录音");
        mBinding.txtCutdown.setText("60" + "");
        mBinding.txtCutdown.setVisibility(View.VISIBLE);
    }

    private void startVisualiser() {
        if (mVisualizer == null) {
            try {
                mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId()); // 初始化
            } catch (Exception e) {
                e.printStackTrace();
            }
            mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                    if (mBinding.wiretapWvWaveform != null) {
                        mBinding.wiretapWvWaveform.setWaveform(waveform);
                    }
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

                }
            }, Visualizer.getMaxCaptureRate(), true, false);
            mVisualizer.setCaptureSize(CAPTURE_SIZE);
            mVisualizer.setEnabled(true);
        }
    }

    @Override
    public void changeCutDownText(String title) {
        mBinding.txtCutdown.setText(title);
    }


    public void changeButtonStatus(Button button, boolean isEnable) {
        if (!isEnable){
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.corners_bg_gray));
        }else if (button.equals(mBinding.btnPlay)){
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.corners_bg_green));
        }else if (button.equals(mBinding.btnStop)){
            button.setBackground(ContextCompat.getDrawable(this,R.drawable.corners_bg_orange));
        }
        button.setEnabled(isEnable);
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
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
        }
    }
}

