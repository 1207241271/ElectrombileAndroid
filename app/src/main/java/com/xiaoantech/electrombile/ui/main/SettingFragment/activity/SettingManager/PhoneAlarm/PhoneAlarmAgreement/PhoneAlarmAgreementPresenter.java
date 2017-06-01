package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.PhoneAlarm.PhoneAlarmAgreement;

import android.Manifest;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.Resources;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.constant.HttpConstant;
import com.xiaoantech.electrombile.event.http.HttpPostEvent;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.http.HttpManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.utils.ContactUtil;
import com.xiaoantech.electrombile.utils.PermissionCheckUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 73843 on 2017/3/18.
 */

public class PhoneAlarmAgreementPresenter implements PhoneAlarmAgreementContract.Presenter{
    private final static String TAG = "PhoneAlarmAgreementPresenter";
    private PhoneAlarmAgreementContract.View mPhoneAlarmAgreement;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS
    };

    protected PhoneAlarmAgreementPresenter(PhoneAlarmAgreementContract.View mPhoneAlarmAgreement){
        this.mPhoneAlarmAgreement = mPhoneAlarmAgreement;
        mPhoneAlarmAgreement.setPresenter(this);
    }


    public void subscribe(){
        EventBus.getDefault().register(this);
    }

    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    public void setPhoneAlarmPhone(){

        checkPhoneContract();

        mPhoneAlarmAgreement.showWaitingDialog("正在开启电话报警");

        String baseUrl = LocalDataManager.getInstance().getHTTPHost()+":"+LocalDataManager.getInstance().getHTTPPort();
        String url = baseUrl + "/v1/telephone/"+ BasicDataManager.getInstance().getBindIMEI();
        String tel = AVUser.getCurrentUser().getUsername();
        String body = "{\"telephone\":\"" + tel + "\"}";
        HttpManager.postHttpResult(url, HttpManager.postType.POST_TYPE_PHONE, body);
        mPhoneAlarmAgreement.showWaitingDialog("正在设置");
    }

    private void checkPhoneContract(){
        if (!LocalDataManager.getInstance().getIsAddContract()){
            LocalDataManager.getInstance().setIsAddContract(true);
            boolean isGranted = true;
            PermissionCheckUtil permissionCheckUtil1 = new PermissionCheckUtil(mPhoneAlarmAgreement.getContext());
            isGranted = permissionCheckUtil1.lakesPermissions(PERMISSIONS);
            if (!isGranted) {
                String[] items = mPhoneAlarmAgreement.getContext().getResources().getStringArray(R.array.alarmPhone);
                ContactUtil.addContract(items[LocalDataManager.getInstance().getContractIndex()], mPhoneAlarmAgreement.getContext());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpPostEvent(HttpPostEvent event){
        if (event.getRequestType() == HttpManager.postType.POST_TYPE_PHONE){
            try {
                JSONObject object = new JSONObject(event.getResult());
                int code = object.getInt("code");
                if (code == 0) {
                    mPhoneAlarmAgreement.hideWaitingDialog();
                    LocalDataManager.getInstance().setPhoneAlarmOpen(true);
                    mPhoneAlarmAgreement.toPhoneAlarmActivity();
                }else{
                    mPhoneAlarmAgreement.showToast("电话报警开通失败");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

}
