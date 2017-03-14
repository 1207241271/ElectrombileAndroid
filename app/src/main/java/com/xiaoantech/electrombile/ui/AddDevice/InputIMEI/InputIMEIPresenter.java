package com.xiaoantech.electrombile.ui.AddDevice.InputIMEI;

import com.xiaoantech.electrombile.event.LeanCloud.BindEvent;
import com.xiaoantech.electrombile.leancloud.LeanCloudManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yangxu on 2016/12/15.
 */

public class InputIMEIPresenter implements InputIMEIContract.Presenter{
    private final static String TAG = "InputIMEIPresenter";
    private InputIMEIContract.View mInputIMEIView;


    protected InputIMEIPresenter(InputIMEIContract.View inputIMEIView){
        this.mInputIMEIView = inputIMEIView;
        mInputIMEIView.setPresenter(this);
    }
    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void bindIMEI(String IMEI) {
        if (IMEI == null || IMEI.length() != 15) {
            mInputIMEIView.showToast("IMEI的长度错误或者为空");
        }else {
            mInputIMEIView.showWaitingDialog("正在绑定中");
            LeanCloudManager.getInstance().bindIMEI(IMEI);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindEvent(BindEvent event){
        mInputIMEIView.bindResult(event.getBindResult());
    }
}
