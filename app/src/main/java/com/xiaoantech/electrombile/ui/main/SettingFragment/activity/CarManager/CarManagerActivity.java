package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityCarmanagerBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.model.CarInfoModel;
import com.xiaoantech.electrombile.ui.AddDevice.CaptureActivity;
import com.xiaoantech.electrombile.ui.AddDevice.ChooseBindActivity;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail.CarInfoDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxu on 2016/12/14.
 */

public class CarManagerActivity extends BaseAcitivity implements  CarManagerContract.View{
    private ActivityCarmanagerBinding mBinding;
    private CarManagerContract.Presenter mPresenter;

    private List<Map<String,Object>> otherCarList;

    private static final String CarImage = "carImage";
    private static final String CarName = "carName";

    private SimpleAdapter mAdapter;

    @Override
    protected void initBefore() {
        getOtherList();
    }

    private void getOtherList(){
        otherCarList = new ArrayList<>();
        HashMap<String,Object> map;
        List<CarInfoModel> carInfoModelList = BasicDataManager.getInstance().getCarInfoList();
        for (int i = 1; i < carInfoModelList.size(); i++){
            map = new HashMap<>();
            map.put(CarName,carInfoModelList.get(i).getName());
            map.put(CarImage, R.drawable.othercar);
            otherCarList.add(map);
        }
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_carmanager);
    }

    @Override
    protected void initView() {
        mPresenter = new CarManagerPresenter(this);
        mBinding.setPresenter(mPresenter);

        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("车辆管理");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarManagerActivity.this.finish();
            }
        });

        //已绑定设备显示
        mBinding.imgBindCar.setImageResource(R.drawable.othercar);
        mBinding.txtBindCarName.setText(BasicDataManager.getInstance().getBindCarInfo().getName());

        //非绑定列表设备显示
        mAdapter = new SimpleAdapter(this,otherCarList,R.layout.item_othercar_list,new String[]{CarImage,CarName},new int[]{R.id.img_car,R.id.txt_car_name});
        mBinding.listView.setAdapter(mAdapter);

        mBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CarManagerActivity.this,CarInfoDetailActivity.class);
                intent.putExtra("index",position+1);
                try {
                    startActivityForResult(intent,0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setPresenter(CarManagerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void addDevice() {
        Intent intent = new Intent(CarManagerActivity.this, ChooseBindActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoBindedCarInfo(int index) {
        Intent intent = new Intent(CarManagerActivity.this, CarInfoDetailActivity.class);
        intent.putExtra("index",index);
        startActivity(intent);
    }
}
