package com.xiaoantech.electrombile.ui.main.MainFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.base.BaseFragment;
import com.xiaoantech.electrombile.databinding.FragmentMainBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.manager.LocationManager;
import com.xiaoantech.electrombile.mqtt.MqttPublishManager;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map.MapActivity;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by yangxu on 2016/11/3.
 */

public class MainFragment extends BaseFragment implements MainFragmentContract.View {
    private FragmentMainBinding mBinding;
    private MainFragmentContract.Presenter mPresenter;
    private BaiduMap            mBaiduMap;
    private Marker              mMarker;
    private static int          selectedCarIndex;
    private static View         selectedView;
    private List<Map<String,Object>> carNameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        mPresenter = new MainFragmentPresenter(this);
        mBinding.setPresenter(mPresenter);
        mBaiduMap = mBinding.mapview.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                return true;
            }
        });
        initMarker();
    }

    private void initMarker(){
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.online);
        LatLng point = new LatLng(30.5171, 114.4392);
        MarkerOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmapDescriptor);
        //在地图上添加Marker，并显示
        mMarker = (Marker) mBaiduMap.addOverlay(option);
    }



    private void initChangeCarDialog(){
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext());
        //设置ListView
        View view = getLayoutInflater(null).inflate(R.layout.content_change_car,null);
        final ListView listView = (ListView)view.findViewById(R.id.listView_change_car);
        carNameList = mPresenter.getCarListInfo();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), carNameList,R.layout.cell_change_car,
                new String[]{"carName","isSelected"},new int[]{R.id.txt_car_name,R.id.btn_selected});
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedView == null){
                    selectedView = parent.getChildAt(0);
                }
                carNameList.get(selectedCarIndex).remove("isSelected");
                carNameList.get(selectedCarIndex).put("isSelected",R.drawable.btn_unselected);
                ImageView preImageView = (ImageView)selectedView.findViewById(R.id.btn_selected);
                Drawable drawable = getContext().getResources().getDrawable(R.drawable.btn_unselected);
                preImageView.setImageDrawable(drawable);


                selectedCarIndex = position;
                selectedView = view;
                carNameList.get(selectedCarIndex).remove("isSelected");
                carNameList.get(selectedCarIndex).put("isSelected",R.drawable.btn_selected);
                ImageView curImageView = (ImageView)view.findViewById(R.id.btn_selected);
                curImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.btn_selected));
            }
        });

        dialog.setTitle("切换车辆").setContentView(view).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String imei =  BasicDataManager.getInstance().getIMEIList().get(selectedCarIndex);
                BasicDataManager.getInstance().changeBindIMEI(imei,true);
                MqttPublishManager.getInstance().getStatus(imei);
            }
        }).create().show();
    }

    private void moveMapToCenter(LatLng point){
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    public void setPresenter(MainFragmentContract.Presenter presenter){
        mPresenter = presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        super.onCreateOptionsMenu(menu,inflater);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
        mBinding.mapview.onDestroy();
    }
    @Override
    public void onResume(){
        super.onResume();
        mBinding.mapview.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mBinding.mapview.onPause();
    }

    @Override
    public void showWeather(int temperature, String weather) {
            mBinding.weatherImage.setText(weather);
            mBinding.weatherTemperature.setText(temperature+"");

    }

    @Override( )
    public void changeFenceStatus(Boolean isOn, boolean isGet) {
        if (isGet){
            //TODO:查询成功
        }else {
            if (isOn){
               showToast("小安宝开启成功！");
                mBinding.btnFencne.setText("开");
            }else {
                showToast("小安宝关闭成功！");
                mBinding.btnFencne.setText("关");
            }
        }

    }

    @Override
    public void changeBattery(int battery) {
        showToast("电量查询成功！");
        mBinding.btnBattery.setText(battery+"");
    }

    @Override
    public void changeItinerary(int itinerary) {
        showToast("里程查询成功！");
        mBinding.btnItinerary.setText(itinerary+"");
    }

    @Override
    public void changeGPSPoint(LatLng point) {
        if (null == mBaiduMap){
            mBaiduMap = mBinding.mapview.getMap();
        }
        if (null == mMarker){
            initMarker();
        }

        mMarker.setPosition(point);
        moveMapToCenter(point);
    }

    @Override
    public void changePlaceInfo(String placeInfo) {
        mBinding.textView.setText(placeInfo);
    }

    @Override
    public void gotoMap() {
        Intent intent = new Intent(mContext, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void changeCar() {
        initChangeCarDialog();
    }
}
