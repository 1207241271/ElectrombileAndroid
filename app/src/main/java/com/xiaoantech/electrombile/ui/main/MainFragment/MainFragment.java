package com.xiaoantech.electrombile.ui.main.MainFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.baidu.mapapi.map.MapPoi;
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
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.NotifyHistoryActivity.NotifyHistoryActivity;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;
import com.xiaoantech.electrombile.widget.Dialog.WeatherInfoDialog;

import org.json.JSONObject;

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
    private boolean             isVisible;

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
        mBinding.mapview.showZoomControls(false);
        mBinding.mapview.showScaleControl(false);
        mBaiduMap = mBinding.mapview.getMap();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                return true;
            }
        });
        mBaiduMap.getUiSettings().setScrollGesturesEnabled(false);
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                gotoMap();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        initMarker();
        setFonts();

        if(isVisible){
            MqttPublishManager.getInstance().getStatus(BasicDataManager.getInstance().getBindIMEI());
            mPresenter.getItinerary();
            mPresenter.getWeatherInfo();
        }
    }

    public void setFonts(){
        String fontPath = "fonts/dincond-regular.ttf";
        mBinding.txtBattery.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPath));
        mBinding.txtItinerary.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPath));
        mBinding.txtBatteryUnit.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPath));
        mBinding.txtItineraryUnit.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPath));
        mBinding.weatherTemperature.setTypeface(Typeface.createFromAsset(mContext.getAssets(),fontPath));
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



    private void initChangeCarDialog(){
        CustomDialog.Builder dialog = new CustomDialog.Builder(getContext());
        //设置ListView
        View view = getLayoutInflater(null).inflate(R.layout.content_change_car,null);
        final ListView listView = (ListView)view.findViewById(R.id.listView_change_car);
        carNameList = mPresenter.getCarListInfo();
        SimpleAdapter adapter = new SimpleAdapter(getContext(), carNameList,R.layout.cell_change_car,
                new String[]{"carName","isSelected"},new int[]{R.id.txt_car_name,R.id.btn_selected});
        listView.setAdapter(adapter);


        selectedView = null;
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
                mPresenter.getItinerary();
                dialog.dismiss();
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
        mPresenter.subscribe();
        mBinding.mapview.onResume();


    }
    @Override
    public void onPause(){
        super.onPause();
        mPresenter.unsubscribe();
        mBinding.mapview.onPause();
    }

    @Override
    public void showWeather(int temperature, String weather) {
        if (weather.contains("云")||weather.contains("阴")){
            mBinding.weatherImage.setImageDrawable(this.getResources().getDrawable(R.drawable.img_weather_cloudy));
        }else if (weather.contains("晴")){
            mBinding.weatherImage.setImageDrawable(this.getResources().getDrawable(R.drawable.img_weather_sunny));
        }else if (weather.contains("雨")){
            mBinding.weatherImage.setImageDrawable(this.getResources().getDrawable(R.drawable.img_weather_rainy));
        }else if (weather.contains("雪")){
            mBinding.weatherImage.setImageDrawable(this.getResources().getDrawable(R.drawable.img_weather_snowy));
        }
        mBinding.weatherTemperature.setText(temperature+"°");

    }

    @Override
    public void showWeatherDialog(JSONObject weatherInfo,String placeInfo) {
        WeatherInfoDialog.Builder dialog = new WeatherInfoDialog.Builder(getContext());
        dialog.setWeatherInfo(weatherInfo).setPlaceInfo(placeInfo).create().show();
    }

    @Override( )
    public void changeFenceStatus(Boolean isOn, boolean isGet) {
        if (isGet){
            if (isOn){
                mBinding.btnFence.setText("已上锁");
            }else {
                mBinding.btnFence.setText("未上锁");
            }
        }else {
            if (isOn){
               showToast("小安宝开启成功！");
                mBinding.btnFence.setText("已上锁");
            }else {
                showToast("小安宝关闭成功！");
                mBinding.btnFence.setText("未上锁");
            }
        }

    }

    @Override
    public void changeBattery(int battery,boolean showTip) {
        if (showTip) {
            showToast("电量查询成功！");
        }
        mBinding.txtBattery.setText(battery+"");
    }

    @Override
    public void changeItinerary(int itinerary) {
        showToast("里程查询成功！");
        mBinding.txtItinerary.setText(itinerary+"");
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
    public void gotoMessage() {
        Intent intent = new Intent(mContext, NotifyHistoryActivity.class);
        startActivity(intent);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            isVisible = true;
        }else {
            isVisible = false;
        }
    }
}
