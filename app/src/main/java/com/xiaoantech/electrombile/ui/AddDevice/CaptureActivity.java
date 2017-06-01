package com.xiaoantech.electrombile.ui.AddDevice;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.constant.LeanCloudConstant;
import com.xiaoantech.electrombile.constant.TimerConstant;
import com.xiaoantech.electrombile.event.LeanCloud.BindEvent;
import com.xiaoantech.electrombile.leancloud.LeanCloudManager;

import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.main.FragmentMainActivity;
import com.xiaoantech.electrombile.utils.JSONUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.Vector;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by yangxu on 2016/12/15.
 */

public class CaptureActivity extends Activity implements QRCodeView.Delegate{
    private QRCodeView qrCodeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_imei);
        initView();
        qrCodeView =(ZBarView)findViewById(R.id.zxingview);
        qrCodeView.setDelegate(this);
        qrCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result.contains("IMEI")){
            String IMEI;
            try{
                JSONObject jsonObject = new  JSONObject(result);
                 IMEI= jsonObject.getString("IMEI");
            }catch (Exception e){
                Toast.makeText(this,"扫描失败，请重新扫描！",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            if (IMEI == null || IMEI.length() != 15){
                Toast.makeText(this,"IMEI的长度错误或者为空",Toast.LENGTH_SHORT).show();
            }else {
                LeanCloudManager.getInstance().bindIMEI(IMEI);
                showToast("正在绑定设备");
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


//
//
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            qrCodeView.startSpot();
        }
    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_scan_imei);
//        initView();
//        CameraManager.init(getApplication());
//
//
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//        hasSurface = false;
//
//        inactivityTimer = new InactivityTimer(this);
//
//        Message msg = Message.obtain();
//        mHandler.sendMessageDelayed(msg,3000);
//
//    }
//
    private void initView(){
        View view = (View)findViewById(R.id.navigation);
        ((TextView)view.findViewById(R.id.navigation_title)).setText("二维码扫描");
        ((RelativeLayout)view.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CaptureActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder){
//        if (!hasSurface){
//            hasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        CameraManager.get().closeDriver();
//        hasSurface = false;
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder){
//        try{
//            CameraManager.get().openDriver(surfaceHolder);
//        }catch (Exception e){
//            e.printStackTrace();
//            return;
//        }
//        if (handler == null){
//            handler = new CaptureActivityHandler(this,decodeFormats,characterSet);
//        }
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder(){
//        viewfinderView.drawViewfinder();
//    }
//
//    public void handleDecode(Result result, Bitmap barcode){
//        inactivityTimer.onActivity();
//
//
//        String resultString = result.getText();
//        if (resultString.charAt(8) != '"'){
//            StringBuilder stringBuilder = new StringBuilder(resultString);
//            stringBuilder.insert(8,'"');
//            resultString = stringBuilder.toString();
//        }
//
//        String IMEI;
//        if (resultString.contains("IMEI")){
//            try{
//                IMEI = JSONUtil.ParseJSON(resultString,"IMEI");
//            }catch (Exception e){
//                Toast.makeText(this,"扫描失败，请重新扫描！",Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//                return;
//            }
//
//            if (IMEI == null || IMEI.length() != 15){
//                Toast.makeText(this,"IMEI的长度错误或者为空",Toast.LENGTH_SHORT).show();
//            }else {
//                LeanCloudManager.getInstance().bindIMEI(IMEI);
//                showToast("正在绑定设备");
//            }
//        }
//
//    }
//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindEvent(BindEvent event){
        bindResult(event.getBindResult());
        mHandler.sendEmptyMessage(0x01);
    }

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

    private void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
    }

    private void gotoMainActivity(){
        Intent intent = new Intent(CaptureActivity.this, FragmentMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}
