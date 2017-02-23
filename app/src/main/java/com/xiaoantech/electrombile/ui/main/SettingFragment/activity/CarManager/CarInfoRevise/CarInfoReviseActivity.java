package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoRevise;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityCarinfoReviseBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.model.CarInfoModel;

/**
 * Created by yangxu on 2017/1/3.
 */

public class CarInfoReviseActivity extends BaseAcitivity implements CarInfoReviseContract.View{
    private ActivityCarinfoReviseBinding mBinding;
    private CarInfoReviseContract.Presenter mPresenter;
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
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_carinfo_revise);
    }

    @Override
    protected void initView() {
        mPresenter = new CarInfoRevisePresenter(this);
        mBinding.setPresenter(mPresenter);
        mBinding.setCarInfo(mCarInfoModel);
        //TODO:设置车辆信息
    }

    @Override
    public void setPresenter(CarInfoReviseContract.Presenter presenter) {
        mPresenter = presenter;
    }

}

