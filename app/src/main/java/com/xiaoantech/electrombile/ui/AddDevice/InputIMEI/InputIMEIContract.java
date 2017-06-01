package com.xiaoantech.electrombile.ui.AddDevice.InputIMEI;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;

/**
 * Created by yangxu on 2016/12/15.
 */

public interface InputIMEIContract {

    interface View extends BaseView<Presenter>{
        void bindResult (LeanCloudConstant.LeanCloudBindResult result);
    }

    interface Presenter extends BasePresenter{
        void bindIMEI(String IMEI);
    }



}
