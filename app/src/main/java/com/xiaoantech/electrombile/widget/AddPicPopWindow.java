package com.xiaoantech.electrombile.widget;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangxu on 2017/4/26.
 */

public class AddPicPopWindow implements View.OnClickListener{
    private PopWindowDelegate popWindowDelegate;
    private Context context;

    public AddPicPopWindow(PopWindowDelegate popWindowDelegate){
        this.popWindowDelegate =popWindowDelegate;
    }

    public PopupWindow showPopMenu(Context context){
        this.context = context;
        View view = View.inflate(context, R.layout.popwindow_changepic,null);
        TextView tv_pop_FromGallery = (TextView) view.findViewById(R.id.tv_pop_FromGallery);
        TextView tv_pop_camera = (TextView) view.findViewById(R.id.tv_pop_camera);
        TextView tv_pop_cancel = (TextView) view.findViewById(R.id.tv_pop_cancel);
        tv_pop_FromGallery.setOnClickListener(this);
        tv_pop_camera.setOnClickListener(this);
        tv_pop_cancel.setOnClickListener(this);
        view.setOnClickListener(this);

        view.startAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_in));
        LinearLayout ll_popup_carpic = (LinearLayout) view.findViewById(R.id.ll_popup_carpic);
        ll_popup_carpic.startAnimation(AnimationUtils.loadAnimation(context,R.anim.push_bottom_in));

        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(colorDrawable);

        popupWindow.setContentView(view);
        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pop_FromGallery:
                File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                popWindowDelegate.choosePhoto(Uri.fromFile(outputImage));
                break;
            case R.id.tv_pop_camera:
                int permisson = PermissionChecker.checkSelfPermission(context, Manifest.permission.CAMERA);
                if (PackageManager.PERMISSION_GRANTED == permisson){
                    File outputImage1 = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
                    try{
                        if(outputImage1.exists()){
                            outputImage1.delete();
                        }
                        outputImage1.createNewFile();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    popWindowDelegate.takePhoto(Uri.fromFile(outputImage1));

                    break;
                }else {
                    //TODO:没有权限
                    popWindowDelegate.requestPermisson();
                }
            case R.id.tv_pop_cancel:
                popWindowDelegate.cancel();
                break;

            default:
                break;
        }
    }

    public interface PopWindowDelegate{
        void choosePhoto(Uri imageUri);
        void takePhoto(Uri imageUri);
        void cancel();
        void requestPermisson();
    }


}
