package com.xiaoantech.electrombile.ui.AddDevice.InputIMEI;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityInputimeiBinding;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map.MapActivity;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.PlayHistory.PlayHistoryContract;

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
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("序列号添加");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputIMEIActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(InputIMEIContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
