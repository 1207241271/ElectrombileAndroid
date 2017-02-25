package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityUserInfoBinding;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise.UserInfoReviseActivity;

/**
 * Created by yangxu on 2016/12/14.
 */

public class UserInfoActivity extends BaseAcitivity implements UserInfoContract.View {
    private ActivityUserInfoBinding mBinding;
    private UserInfoContract.Presenter mPresenter;

    @Override
    protected void initBefore() {
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
    }

    @Override
    protected void initView() {
        mPresenter = new UserInfoPresenter(this);
        mBinding.setPresenter(mPresenter);
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("车主信息");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void gotoUserInfoRevise() {
        Intent intent = new Intent(UserInfoActivity.this, UserInfoReviseActivity.class);
        try {
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isMale = LocalDataManager.getInstance().getSex();
        if (isMale){
            mBinding.txtSex.setText("男");
        }else{
            mBinding.txtSex.setText("女");
        }
        int birth = LocalDataManager.getInstance().getBirth();
        int year = birth/10000;
        int month = (birth - 10000*year)/100;
        final int day = birth - 10000*year - month*100;
        mBinding.txtBirth.setText(year + "年" + (month+1) + "月" + day + "日");
        mBinding.txtUserName.setText(LocalDataManager.getInstance().getUserName());
        mBinding.txtNickName.setText(LocalDataManager.getInstance().getNickName());
        mBinding.txtIdentityNum.setText(LocalDataManager.getInstance().getIdentityNum());
    }
}
