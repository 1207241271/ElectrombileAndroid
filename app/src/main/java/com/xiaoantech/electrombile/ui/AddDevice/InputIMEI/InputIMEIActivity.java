package com.xiaoantech.electrombile.ui.AddDevice.InputIMEI;

import android.databinding.DataBindingUtil;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityInputimeiBinding;

/**
 * Created by yangxu on 2016/12/15.
 */

public class InputIMEIActivity extends BaseAcitivity implements InputIMEIContract.View{
    private static final String TAG = "InputIMEIActivity";
    private InputIMEIContract.Presenter mPresenter;
    private ActivityInputimeiBinding mBinding;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inputimei,null);
    }

    @Override
    protected void initView() {
        mPresenter = new InputIMEIPresenter(this);
        mBinding.setPresenter(mPresenter);
    }

    @Override
    public void setPresenter(InputIMEIContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
