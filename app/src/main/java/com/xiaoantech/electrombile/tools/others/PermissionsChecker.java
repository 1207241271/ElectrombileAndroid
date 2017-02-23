package com.xiaoantech.electrombile.tools.others;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.xiaoantech.electrombile.application.App;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;

/**
 * Created by yangxu on 2017/1/20.
 */

public class PermissionsChecker extends AppCompatActivity{
    private final Context mContext;
    private static final int PERMISSION_REQUEST_CODE = 0;
    private CustomDialog.Builder    requestDialog;


    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    // 判断是否缺少权限
    public boolean lakesPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lakesPermission(permission))
                return true;
        }
        return false;
    }

    private boolean lakesPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(String tipString, final String... permissions){
        requestDialog = new CustomDialog.Builder(mContext);
        requestDialog.setTitle("权限请求").setMessage(tipString).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(PermissionsChecker.this,permissions,PERMISSION_REQUEST_CODE);
                dialog.dismiss();
            }
        }).create().show();
    }

    // 权限管理的返回
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {

        } else {
            showMissingPermissionDialog();
        }
    }
    // 判断是否拥有所有权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
//        builder.setTitle(R.string.help).setMessage(R.string.string_help_text);
//        builder.setNegativeButton(R.string.quit, ((dialog, which) -> {
//            setResult(PERMISSIONS_DENIED);
//            finish();
//        }));
//        builder.setPositiveButton(R.string.settings, (dialog, which) -> {
//            startAppSettings();
//        });
//        builder.show();
    }
}
