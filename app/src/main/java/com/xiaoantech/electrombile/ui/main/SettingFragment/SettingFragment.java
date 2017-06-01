package com.xiaoantech.electrombile.ui.main.SettingFragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseFragment;
import com.xiaoantech.electrombile.databinding.FragmentSettingBinding;
import com.xiaoantech.electrombile.ui.Other.CropActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.AboutUs.AboutUsActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarManagerActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.MapDownLoad.MapDownloadActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.Record.RecordActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.SettingManagerActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfo.UserInfoActivity;
import com.xiaoantech.electrombile.utils.BitmapUtil;
import com.xiaoantech.electrombile.utils.BitmapUtils;
import com.xiaoantech.electrombile.widget.AddPicPopWindow;

/**
 * Created by yangxu on 2016/11/3.
 */

public class SettingFragment extends BaseFragment implements SettingFragmentContract.View,AddPicPopWindow.PopWindowDelegate{
    private FragmentSettingBinding  mBinding;
    private SettingFragmentContract.Presenter mPresenter;
    private PopupWindow mPopUpWindow;
    private Uri imageUri;

    public static final int TAKE_PHOTE=1;
    public static final int CROP_PHOTO=2;
    public static final int CHOOSE_PHOTO=3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting,container,false);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void initView() {
        mPresenter = new SettingFragmentPresenter(this);
        mBinding.setPresenter(mPresenter);
        Bitmap bitmap = BitmapUtils.compressImageFromFile("user.png");
        if (bitmap != null){
            mBinding.headImage.setImageBitmap(bitmap);
        }else {
            mPresenter.getIcon();
        }
    }

    @Override
    public void setPresenter(SettingFragmentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void gotoCarManager() {
        Intent intent = new Intent(mContext, CarManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoUserManager() {
        Intent intent = new Intent(mContext, UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoRecord() {
        Intent intent = new Intent(mContext, RecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoMapDownload() {
        Intent intent = new Intent(mContext, MapDownloadActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoSettingManager() {
        Intent intent = new Intent(mContext, SettingManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoAboutUs() {
        Intent intent = new Intent(mContext, AboutUsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showPopMenu() {
        AddPicPopWindow addPicPopWindow = new AddPicPopWindow(this);
        mPopUpWindow = addPicPopWindow.showPopMenu(mContext);
        mPopUpWindow.showAtLocation(mBinding.headImage, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        mPopUpWindow.update();
    }


    @Override
    public void cancel() {
        mPopUpWindow.dismiss();
    }

    @Override
    public void takePhoto(Uri imageUri) {
        this.imageUri = imageUri;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTE);
        mPopUpWindow.dismiss();
    }

    @Override
    public void choosePhoto(Uri imageUri) {
        this.imageUri = imageUri;
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
        mPopUpWindow.dismiss();
    }

    @Override
    public void requestPermisson() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA)){
        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},0);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK != resultCode){
            return;
        }
        switch (requestCode){
            case TAKE_PHOTE:
                Intent intentTake = new Intent(mContext,CropActivity.class);
                intentTake.setData(imageUri);
                startActivityForResult(intentTake, CROP_PHOTO);
                break;
            case CHOOSE_PHOTO:
                imageUri = data.getData();
                Intent intent = new Intent(mContext,CropActivity.class);
                intent.setData(imageUri);
                startActivityForResult(intent, CROP_PHOTO);
                break;
            case CROP_PHOTO:
                mBinding.headImage.setImageBitmap(null);
                Bitmap bitmap = BitmapUtils.compressImageFromFile("user.png");
                if (bitmap != null){
                    mBinding.headImage.setImageBitmap(bitmap);
                    saveImage();
                }
                break;
            default:
                break;
        }
    }

    private void saveImage(){
        mPresenter.saveImage();
    }

    @Override
    public void showIcon() {
        Bitmap bitmap = BitmapUtils.compressImageFromFile("user.png");
        if (bitmap != null){
            mBinding.headImage.setImageBitmap(bitmap);
        }
    }
}
