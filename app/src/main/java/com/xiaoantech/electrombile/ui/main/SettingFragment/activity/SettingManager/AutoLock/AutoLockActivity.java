package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.AutoLock;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityAutolockBinding;
import com.xiaoantech.electrombile.manager.LocalDataManager;


/**
 * Created by yangxu on 2017/2/23.
 */

public class AutoLockActivity extends BaseAcitivity implements AutoLockContract.View  {
    private ActivityAutolockBinding mBinding;
    private AutoLockContract.Presenter mPresenter;

    @Override
    protected void initBefore() {

    }

    @Override
    protected void bindData() {
            mBinding = DataBindingUtil.setContentView(this, R.layout.activity_autolock);
    }

    @Override
    protected void initView() {
        mPresenter = new AutoLockPresenter(this);
        mBinding.setPresenter(mPresenter);
       if (LocalDataManager.getInstance().getAutoLock()){
            changeAutoLockPeriodImg(LocalDataManager.getInstance().getAutoLockPeriod());
        }else {
            changeAutoLockPeriodImg(0);
            mBinding.btnAutolock.setText("已关闭");
        }
        ((TextView)mBinding.navigation.findViewById(R.id.navigation_title)).setText("自动设防设置");
        ((RelativeLayout)mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoLockActivity.this.finish();
            }
        });
    }

    @Override
    public void setPresenter(AutoLockContract.Presenter Presenter) {
        this.mPresenter = Presenter;
    }

    @Override
    public void changeAutoLockState(boolean isOn) {
        if (isOn) {
            mBinding.btnAutolock.setText("已开启");
            changeAutoLockPeriodImg(5);
        } else {
            mBinding.btnAutolock.setText("已关闭");
            changeAutoLockPeriodImg(0);
        }
        showToast("设置成功");
    }

    public void changeAutoLockPeriodImg(int Period) {
        switch (Period){
            case 5:
                MarkImage_5minutes();
                break;
            case 10:
                MarkImage_10minutes();
                break;
            case 15:
                MarkImage_15minutes();
                break;
            default:
                MarkImage_Nominutes();
        }
    }

    public void MarkImage_Nominutes() {
        mBinding.img5Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img10Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img15Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
    }
    public void MarkImage_5minutes() {
        mBinding.img5Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_selected));
        mBinding.img10Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img15Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
    }
    public void MarkImage_10minutes() {
        mBinding.img5Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img10Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_selected));
        mBinding.img15Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
    }
    public void MarkImage_15minutes() {
        mBinding.img5Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img10Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_unselected));
        mBinding.img15Minutes.setImageDrawable(this.getResources().getDrawable(R.drawable.btn_selected));
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

}
