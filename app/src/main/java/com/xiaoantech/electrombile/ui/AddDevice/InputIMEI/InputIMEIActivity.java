package com.xiaoantech.electrombile.ui.AddDevice.InputIMEI;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.databinding.ActivityInputimeiBinding;
import com.xiaoantech.electrombile.ui.main.FragmentMainActivity;
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

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void bindResult(LeanCloudConstant.LeanCloudBindResult result) {
        switch (result){
            case LEAN_CLOUD_BIND_RESULT_BIND_SUCCESS:
                showToast("绑定成功");
                gotoMainActivity();
                break;
            case LEAN_CLOUD_BIND_RESULT_DID_NONE:
                showToast("未找到该设备");
                break;
            case LEAN_CLOUD_BIND_RESULT_BIND_MUCH:
                showToast("该设备已被绑定");
                break;
            case LEAN_CLOUD_BIND_RESULT_BIND_FAIL:
                showToast("绑定失败");
                break;
        }
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(InputIMEIActivity.this, FragmentMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
