package com.xiaoantech.electrombile.ui.main.MainFragment.activity.NotifyHistoryActivity;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityNotifyHistoryBinding;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map.MapActivity;

/**
 * Created by yangxu on 2017/2/23.
 */

public class NotifyHistoryActivity extends BaseAcitivity {
    private ActivityNotifyHistoryBinding mBinding;
    private NotifyHistoryContract mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notify_history);
    }

    @Override
    protected void initView() {
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("车辆位置");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyHistoryActivity.this.finish();
            }
        });
    }
}
