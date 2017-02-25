package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.ChangePass;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityChangePassBinding;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;

/**
 * Created by yangxu on 2017/1/4.
 */

public class ChangePassActivity extends BaseAcitivity implements ChangePassContract.View{
    private ActivityChangePassBinding mBinding;
    private ChangePassContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_pass);
    }

    @Override
    protected void initView() {
        mPresenter = new ChangePassPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("修改密码");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(ChangePassContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
