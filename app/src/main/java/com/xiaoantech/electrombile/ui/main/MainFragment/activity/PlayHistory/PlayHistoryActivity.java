package com.xiaoantech.electrombile.ui.main.MainFragment.activity.PlayHistory;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityPlayhistoryBinding;
import com.xiaoantech.electrombile.model.GPSPointModel;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map.MapPresenter;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory.MapListActivity;
import com.xiaoantech.electrombile.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangxu on 2016/11/27.
 */

public class PlayHistoryActivity extends BaseAcitivity implements PlayHistoryContract.View ,BaiduMap.OnMapLoadedCallback{
    private final static String         TAG = "PlayHistoryActivity";
    private BaiduMap    mBaiduMap;
    private ActivityPlayhistoryBinding mBinding;
    private PlayHistoryContract.Presenter   mPresenter;
    private Marker      carMarker;
    private LatLngBounds mBounds;
    private int mCurrentPointIndex;
    private int mPlaySpeed;
    public static List<GPSPointModel> pointList;
    private playStatus  mPlayStatus;

    enum playStatus{
        PLAY_STATUS_PLAYING,
        PLAY_STATUS_PAUSE,
        PLAY_STATUS_END
    }

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 201;
            handler.sendMessage(message);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            switch (message.what){
                case 100:
                    refreshBounds();
                    break;
                case 201:
                    changeMarker();
                    break;
            }
        }

    };


    @Override
    protected void initBefore() {
        timer.schedule(task,1000,1000);
        mCurrentPointIndex = 0;
        mPlaySpeed = 0;
        mPlayStatus = playStatus.PLAY_STATUS_END;
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_playhistory);

    }

    @Override
    protected void initView() {
        ((TextView)findViewById(R.id.navigation_title)).setText("历史轨迹");
        ((RelativeLayout)findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayHistoryActivity.this.finish();
            }
        });
        mPresenter = new PlayHistoryPresenter(this);
        mBinding.setPresenter(mPresenter);
        mProgressDialog = new ProgressDialog(this);
        mBinding.mapView.showZoomControls(false);
        mBaiduMap = mBinding.mapView.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        mBinding.skr.setMax(pointList.size()-1);
        mBinding.skr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCurrentPointIndex = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //松手后立即响应变化
                mCurrentPointIndex =  seekBar.getProgress();
                changeMarker();
            }
        });
        adjustLinePlace();
        drawLine();
        initMarker();
    }

    public void setPresenter(PlayHistoryContract.Presenter presenter){this.mPresenter = presenter;}


    private void drawLine(){
        ArrayList<LatLng> points = new ArrayList<>();
        for (GPSPointModel gpsPointModel:pointList){
            points.add(gpsPointModel.getBaiduPoint());
        }
        OverlayOptions overlayOptions = new PolylineOptions().points(points).width(15).color(0xFF0000);
        mBaiduMap.addOverlay(overlayOptions);
    }

    private void adjustLinePlace(){
        double latMin = pointList.get(0).getBaiduPoint().latitude;
        double latMax = pointList.get(0).getBaiduPoint().latitude;
        double lngMin = pointList.get(0).getBaiduPoint().longitude;
        double lngMax = pointList.get(0).getBaiduPoint().longitude;
        for (int i = 0;i < pointList.size();i ++){
            double lat = pointList.get(i).getBaiduPoint().latitude;
            if (lat > latMax) latMax = lat;
            if (lat < latMin) latMin = lat;

            double lng = pointList.get(i).getBaiduPoint().longitude;
            if (lng > lngMax) lngMax = lng;
            if (lng < lngMin) lngMin = lng;
        }
        LatLng southWest = new LatLng(latMin,lngMin);
        LatLng northEast = new LatLng(latMax,lngMax);


        mBounds = new LatLngBounds.Builder().include(northEast).include(southWest).build();

        refreshBounds();
        //必须在地图出现之后才能画图
        Message msg = new Message();
        msg.what = 100;
        handler.sendMessageDelayed(msg,800);
    }

    private void initMarker(){
        BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R.drawable.online);
        MarkerOptions options = new MarkerOptions().position(pointList.get(0).getBaiduPoint()).icon(bitMap);
        mBaiduMap.addOverlay(options);

        carMarker = (Marker)mBaiduMap.addOverlay(options);
        carMarker.setPosition(pointList.get(0).getBaiduPoint());

    }

    private void changeMarker(){
        if (mBaiduMap == null) {
            return;
        }
        if (mPlaySpeed == 0 || mPlayStatus == playStatus.PLAY_STATUS_PAUSE || mPlayStatus == playStatus.PLAY_STATUS_END){
            return;
        }

        mCurrentPointIndex = mCurrentPointIndex + mPlaySpeed;
        if (mCurrentPointIndex >= pointList.size()){
            //到达终点并暂停
            mCurrentPointIndex = pointList.size() - 1;
            playPause();
            mPlayStatus = playStatus.PLAY_STATUS_END;
        }
        LatLng point = pointList.get(mCurrentPointIndex).getBaiduPoint();
        long timestamp = pointList.get(mCurrentPointIndex).getTimestamp();
        mBinding.txtTime.setText(TimeUtil.getMinuteStringFromTimeStamp(timestamp));
        mBinding.txtSpeed.setText("速度:" + pointList.get(mCurrentPointIndex).getSpeed() + "km/h");
        carMarker.setPosition(point);
        mBinding.skr.setProgress(mCurrentPointIndex);
    }

    private void playPause(){
        mBinding.playBtn.setText("播放");
        mPlayStatus = playStatus.PLAY_STATUS_PAUSE;
    }

    private void playStart(){
        if (mPlayStatus == playStatus.PLAY_STATUS_END){
            mPlaySpeed = 1;
            mCurrentPointIndex = 0;
        }
        mPlayStatus = playStatus.PLAY_STATUS_PLAYING;
        mBinding.playBtn.setText("暂停");
    }

    @Override
    public void switchPlayStatus() {
        if (mPlayStatus == playStatus.PLAY_STATUS_PLAYING){
            playPause();
        }else {
            playStart();
        }
    }

    @Override
    public void switchPlaySpeed() {
        if (mPlaySpeed == 0 || mPlaySpeed == 1){
            mPlaySpeed = 2;
            mBinding.speedBtn.setText("x2");
        }else if (mPlaySpeed == 2){
            mPlaySpeed = 5;
            mBinding.speedBtn.setText("x5");
        }else if (mPlaySpeed == 5){
            mPlaySpeed = 1;
            mBinding.speedBtn.setText("x1");
        }
    }

    private void refreshBounds(){
//        mBaiduMap.setMapStatusLimits(mBounds);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(mBounds);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    public void onMapLoaded(){
        adjustLinePlace();
    }

    @Override
    public void onResume(){
        super.onResume();
        mBinding.mapView.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
        mBinding.mapView.onPause();

    }

    @Override
    public void onDestroy(){
        mBaiduMap.clear();
        mBinding.mapView.onDestroy();
        super.onDestroy();
    }
}
