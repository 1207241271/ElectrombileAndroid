package com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.constant.LayoutConstant;
import com.xiaoantech.electrombile.databinding.ActivityMapBinding;
import com.xiaoantech.electrombile.databinding.ContentChangeMapBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory.MapListActivity;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;

/**
 * Created by yangxu on 2016/11/15.
 */

public class MapActivity extends BaseAcitivity implements MapContract.View{
    private final static String         TAG = "MapActivity";
    private ActivityMapBinding          mBinding;
    private MapContract.Presenter       mPresenter;
    private ProgressDialog              mProgressDialog;
    private BaiduMap                    mBaiduMap;
    private Marker                      mMarker;
    private String                      mLatestPlaceInfo;
    private String                      mLatestDateInfo;
    private LayoutConstant.MapType      mMapType;
    private static LayoutConstant.MapType currentMapType;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_map);
    }

    @Override
    protected void initView() {
        mPresenter = new MapPresenter(this);
        mBinding.setPresenter(mPresenter);

        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("车辆位置");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.this.finish();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mBinding.mapView.showZoomControls(false);
        mBaiduMap = mBinding.mapView.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        initMarker();
        mMapType = LocalDataManager.getInstance().getMapType();
        setMapType(mMapType);
        mBinding.txtCarName.setText(BasicDataManager.getInstance().getBindIMEI());
    }

    private void initMarker(){
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.img_map_location);
        LatLng point = new LatLng(30.5171, 114.4392);
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmapDescriptor);
        //在地图上添加Marker，并显示
        mMarker = (Marker) mBaiduMap.addOverlay(option);
    }

    private void moveMapToCenter(LatLng point){
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    public void setPresenter(MapContract.Presenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void changeGPSPoint(LatLng point) {
        if (null == mBaiduMap){
            mBaiduMap = mBinding.mapView.getMap();
        }
        if (null == mMarker){
            initMarker();
        }

        mMarker.setPosition(point);
        moveMapToCenter(point);
    }

    @Override
    public void changePlaceInfo(String placeInfo) {
        mBinding.txtCarPlace.setText(placeInfo);
        mLatestPlaceInfo = placeInfo;
    }

    @Override
    public void changeDateInfo(String timeDate) {
        mBinding.txtLocationTime.setText(timeDate);
        mLatestDateInfo = timeDate;
    }

    @Override
    public void gotoMapList(){
        Intent intent = new Intent(MapActivity.this,MapListActivity.class);
        startActivity(intent);
    }


    public void initChangeMapType(){
        CertainDialog.Builder dialog = new CertainDialog.Builder(this);
        final LayoutConstant.MapType mapType = mMapType;
        final ContentChangeMapBinding changeMapBinding = DataBindingUtil.bind(View.inflate(this,R.layout.content_change_map,null));
        changeMapBinding.constraintLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCurrentMapType(LayoutConstant.MapType.MAP_TYPE_NORMAL,changeMapBinding,MapActivity.this);
            }
        });
        changeMapBinding.constraintLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCurrentMapType(LayoutConstant.MapType.MAP_TYPE_SATELLITE,changeMapBinding,MapActivity.this);
            }
        });

        changeMapBinding.constraintLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCurrentMapType(LayoutConstant.MapType.MAP_TYPE_3D,changeMapBinding,MapActivity.this);
            }
        });
        changeCurrentMapType(mMapType,changeMapBinding,this);
        dialog.setTitle("地图切换").setContentView(changeMapBinding.getRoot()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setMapType(currentMapType);
                mMapType = currentMapType;
                LocalDataManager.getInstance().setMapType(mMapType);
                dialog.dismiss();
            }
        }).create().show();
    }

    static void changeCurrentMapType(LayoutConstant.MapType mapType, ContentChangeMapBinding changeMapBinding, Context context){
        currentMapType = mapType;
        changeMapBinding.selectBtnPlane.setBackground(context.getResources().getDrawable(R.drawable.btn_unselected));
        changeMapBinding.selectBtnSatellite.setBackground(context.getResources().getDrawable(R.drawable.btn_unselected));
        changeMapBinding.selectBtn3D.setBackground(context.getResources().getDrawable(R.drawable.btn_unselected));
        switch (mapType){
            case MAP_TYPE_NORMAL:
                changeMapBinding.selectBtnPlane.setBackground(context.getResources().getDrawable(R.drawable.btn_selected));
                break;
            case MAP_TYPE_SATELLITE:
                changeMapBinding.selectBtnSatellite.setBackground(context.getResources().getDrawable(R.drawable.btn_selected));
                break;
            case MAP_TYPE_3D:
                changeMapBinding.selectBtn3D.setBackground(context.getResources().getDrawable(R.drawable.btn_selected));
                break;
            default:
                changeMapBinding.selectBtnPlane.setBackground(context.getResources().getDrawable(R.drawable.btn_selected));
        }
    }


    @Override
    public void changeMapType() {
        initChangeMapType();
    }

    public void setMapType(LayoutConstant.MapType mapType){
        switch (mapType){
            case MAP_TYPE_NORMAL:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                MapStatus mapStatusPlain = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(0).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatusPlain));
                break;
            case MAP_TYPE_SATELLITE:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case MAP_TYPE_3D:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                MapStatus mapStatus3D = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(-45).build();
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus3D));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        mPresenter.getLatestHistoryLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }
}
