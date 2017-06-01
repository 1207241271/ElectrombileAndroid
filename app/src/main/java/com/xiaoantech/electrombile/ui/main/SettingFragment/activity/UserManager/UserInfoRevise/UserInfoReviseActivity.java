package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityUserInfoReviseBinding;
import com.xiaoantech.electrombile.databinding.ContentChooseSexBinding;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.tools.others.PermissionsChecker;
import com.xiaoantech.electrombile.ui.Other.CropActivity;
import com.xiaoantech.electrombile.utils.BitmapUtils;
import com.xiaoantech.electrombile.widget.AddPicPopWindow;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangxu on 2017/1/3.
 */

public class UserInfoReviseActivity extends BaseAcitivity implements UserInfoReviseContract.View ,AddPicPopWindow.PopWindowDelegate{
    private ActivityUserInfoReviseBinding mBinding;
    private UserInfoReviseContract.Presenter mPresenter;
    private Uri imageUri;
    private static boolean isMale;
    private static int birth;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA};
    private PopupWindow mPopUpWindow;



    public static final int TAKE_PHOTE=1;
    public static final int CROP_PHOTO=2;
    public static final int CHOOSE_PHOTO=3;

    @Override
    protected void initBefore() {
        isMale = LocalDataManager.getInstance().getSex();
        birth = LocalDataManager.getInstance().getBirth();
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_info_revise);
    }

    @Override
    protected void initView() {
        mPresenter = new UserInfoRevisePresenter(this);
        mBinding.setPresenter(mPresenter);


        if (isMale){
            mBinding.txtSex.setText("男");
        }else{
            mBinding.txtSex.setText("女");
        }

        int year = birth/10000;
        int month = (birth - 10000*year)/100;
        final int day = birth - 10000*year - month*100;
        mBinding.txtBirth.setText(year + "年" + (month+1) + "月" + day + "日");
        mBinding.txtUserName.setText(LocalDataManager.getInstance().getUserName());
        mBinding.txtNickName.setText(LocalDataManager.getInstance().getNickName());
        mBinding.txtIdentityNum.setText(LocalDataManager.getInstance().getIdentityNum());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bitmap = BitmapUtils.compressImageFromFile("user.png");
        if (bitmap != null){
            mBinding.imgUserIcon.setImageBitmap(bitmap);
        }
    }

    @Override
    public void setPresenter(UserInfoReviseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void changeIcon() {
        AddPicPopWindow addPicPopWindow = new AddPicPopWindow(this);
        mPopUpWindow = addPicPopWindow.showPopMenu(this);
        mPopUpWindow.showAtLocation(mBinding.imgUserIcon, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK != resultCode){
            return;
        }
        switch (requestCode){
            case TAKE_PHOTE:
                Intent intentTake = new Intent(this,CropActivity.class);
                intentTake.setData(imageUri);
                startActivityForResult(intentTake, CROP_PHOTO);
                break;
            case CHOOSE_PHOTO:
                imageUri = data.getData();
                Intent intent = new Intent(this,CropActivity.class);
                intent.setData(imageUri);
                startActivityForResult(intent, CROP_PHOTO);
                break;
            case CROP_PHOTO:
                mBinding.imgUserIcon.setImageBitmap(null);
                Bitmap bitmap = BitmapUtils.compressImageFromFile("user.png");
                if (bitmap != null){
                    mBinding.imgUserIcon.setImageBitmap(bitmap);
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
    public void requestPermisson() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},0);
        }
    }


    @Override
    public void chooseSex() {
        CertainDialog.Builder dialog = new CertainDialog.Builder(this);
        final ContentChooseSexBinding  contentViewBinding = DataBindingUtil.bind(View.inflate(this,R.layout.content_choose_sex,null));
        if (isMale){
            contentViewBinding.imgSelectedMale.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected));
        }else {
            contentViewBinding.imgSelectedFemale.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected));

        }
        contentViewBinding.constraintLayoutSexMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = true;
                contentViewBinding.imgSelectedMale.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected));
                contentViewBinding.imgSelectedFemale.setImageDrawable(getResources().getDrawable(R.drawable.btn_unselected));
            }
        });
        contentViewBinding.constraintLayoutSexFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMale = false;
                contentViewBinding.imgSelectedMale.setImageDrawable(getResources().getDrawable(R.drawable.btn_unselected));
                contentViewBinding.imgSelectedFemale.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected));
            }
        });

        dialog.setTitle("选择性别").setContentView(contentViewBinding.getRoot()).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isMale) {
                    mBinding.txtSex.setText("男");
                }else {
                    mBinding.txtSex.setText("女");
                }
            }
        }).create().show();
    }

    @Override
    public void chooseBirthDate() {
        CertainDialog.Builder dialog = new CertainDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.content_choose_date,null);
        DatePicker datePicker = (DatePicker)view.findViewById(R.id.datePicker);
        int year = birth/10000;
        int month = (birth - 10000*year)/100;
        final int day = birth - 10000*year - month*100;
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                birth = year*10000 + monthOfYear*100 + dayOfMonth;
            }
        });

        dialog.setTitle("设置生日").setContentView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = birth/10000;
                int month = (birth - 10000*year)/100;
                final int day = birth - 10000*year - month*100;
                mBinding.txtBirth.setText(year + "年" + (month+1) + "月" + day + "日");
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void confirmModify() {
        LocalDataManager.getInstance().setBirth(birth);
        LocalDataManager.getInstance().setSex(isMale);
        LocalDataManager.getInstance().setIdentityNum(mBinding.txtIdentityNum.getText().toString());
        LocalDataManager.getInstance().setUserName(mBinding.txtUserName.getText().toString());
        LocalDataManager.getInstance().setNickName(mBinding.txtNickName.getText().toString());
        this.finish();
    }
}
