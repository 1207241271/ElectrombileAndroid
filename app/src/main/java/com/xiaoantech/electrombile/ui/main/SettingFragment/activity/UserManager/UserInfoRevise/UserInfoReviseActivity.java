package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.UserManager.UserInfoRevise;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityUserInfoReviseBinding;
import com.xiaoantech.electrombile.databinding.ContentChooseSexBinding;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.tools.others.PermissionsChecker;
import com.xiaoantech.electrombile.widget.Dialog.CertainDialog;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangxu on 2017/1/3.
 */

public class UserInfoReviseActivity extends BaseAcitivity implements UserInfoReviseContract.View {
    private ActivityUserInfoReviseBinding mBinding;
    private UserInfoReviseContract.Presenter mPresenter;
    private Uri imageUri;
    private static boolean isMale;
    private static int birth;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA};

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
    public void setPresenter(UserInfoReviseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void changeIcon() {
        try {
            PermissionsChecker permissionsChecker = new PermissionsChecker(this);
            if (!permissionsChecker.lakesPermissions(PERMISSIONS)) {
                CertainDialog.Builder dialog = new CertainDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.content_change_user_icon, null);
                TextView txt_album = (TextView) view.findViewById(R.id.txt_icon_album);
                TextView txt_camera = (TextView) view.findViewById(R.id.txt_icon_camera);

                txt_album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File outPutImage = new File(Environment.getExternalStorageDirectory(), "output_image.png");
                        try {
                            if (outPutImage.exists())
                                outPutImage.delete();
                            outPutImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageUri = Uri.fromFile(outPutImage);
                        Intent intent = new Intent("android.intent.action.PICK");
                        intent.setType("image/*");
                        startActivityForResult(intent, 3);
                    }
                });

                txt_camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File outPutImage = new File(Environment.getExternalStorageDirectory(), "outputImage.png");
                        try {
                            if (outPutImage.exists())
                                outPutImage.delete();
                            outPutImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageUri = Uri.fromFile(outPutImage);
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, 1);
                    }
                });

                dialog.setTitle("更换头像").setContentView(view).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        }catch (Exception e){
            e.printStackTrace();
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
