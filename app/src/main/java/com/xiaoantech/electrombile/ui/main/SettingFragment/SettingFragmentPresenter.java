package com.xiaoantech.electrombile.ui.main.SettingFragment;

import android.graphics.Bitmap;
import android.net.Uri;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.xiaoantech.electrombile.ui.main.MainFragment.MainFragmentContract;
import com.xiaoantech.electrombile.utils.BitmapUtils;
import com.xiaoantech.electrombile.utils.FileUtil;

import java.util.List;

/**
 * Created by yangxu on 2016/12/13.
 */

public class SettingFragmentPresenter implements SettingFragmentContract.Presenter{
    private final static String TAG = "SettingFragmentPresenter";
    private SettingFragmentContract.View    mSettingFragmentView;

    protected  SettingFragmentPresenter(SettingFragmentContract.View settingFragmentView){
        this.mSettingFragmentView = settingFragmentView;
        mSettingFragmentView.setPresenter(this);
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void gotoCarManager() {
        mSettingFragmentView.gotoCarManager();
    }

    @Override
    public void gotoUserManager() {
        mSettingFragmentView.gotoUserManager();
    }

    @Override
    public void gotoRecord() {
        mSettingFragmentView.gotoRecord();
    }

    @Override
    public void gotoMapDownload() {
        mSettingFragmentView.gotoMapDownload();
    }

    @Override
    public void gotoSettingManager() {
        mSettingFragmentView.gotoSettingManager();
    }

    @Override
    public void gotoAboutUs() {
        mSettingFragmentView.gotoAboutUs();
    }

    @Override
    public void changeUserIcon() {
        mSettingFragmentView.showPopMenu();
    }

    @Override
    public void saveImage() {
        try {
            AVFile file = AVFile.withFile(AVUser.getCurrentUser().getUsername()+".png",BitmapUtils.getImageFile("user.png"));
            file.saveInBackground();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void getIcon() {
        AVQuery avQuery = new AVQuery("_File");
        final String fileName = AVUser.getCurrentUser().getUsername()+".png";
        avQuery.whereEqualTo("name",fileName);
        avQuery.findInBackground(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (list.size()>0){
                    AVFile file = AVFile.withAVObject((AVObject) list.get(0));
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            FileUtil.saveBytesToFile(bytes,"user.png");
                            mSettingFragmentView.showIcon();
                        }
                    });
                }
            }
        });
    }
}
