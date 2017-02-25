package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityCarinfoDetailBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.model.CarInfoModel;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise.CarInfoReviseActivity;

/**
 * Created by yangxu on 2016/12/21.
 */

public class CarInfoDetailActivity extends BaseAcitivity implements CarInfoDetailContract.View {
    private ActivityCarinfoDetailBinding mBinding;
    private CarInfoDetailContract.Presenter mPresenter;
    private CarInfoModel mCarInfoModel;
    private int carIndex;

    @Override
    protected void initBefore() {
        Intent intent = getIntent();
        carIndex = intent.getIntExtra("index",0);
        mCarInfoModel = BasicDataManager.getInstance().getCarInfoList().get(carIndex);
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_carinfo_detail);
    }

    @Override
    protected void initView() {
        mPresenter = new CarInfoDetailPresenter(this);
        mBinding.setPresenter(mPresenter);
        mBinding.setCarInfo(mCarInfoModel);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("车辆信息");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarInfoDetailActivity.this.finish();
            }
        });
        //TODO:设置车辆信息
    }

    @Override
    public void setPresenter(CarInfoDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void gotoRevise() {
        Intent intent = new Intent(CarInfoDetailActivity.this, CarInfoReviseActivity.class);
        intent.putExtra("index",carIndex);
        startActivity(intent);
    }
}
