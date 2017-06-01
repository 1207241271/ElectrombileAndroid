package com.xiaoantech.electrombile.ui.main.SettingFragment;

import android.graphics.Bitmap;
import android.net.Uri;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2016/12/13.
 */

public interface SettingFragmentContract {

    interface View extends BaseView<Presenter>{
        void gotoCarManager();
        void gotoUserManager();
        void gotoRecord();
        void gotoMapDownload();
        void gotoSettingManager();
        void gotoAboutUs();
        void showPopMenu();
        void showIcon();
    }

    interface Presenter extends BasePresenter{
        void gotoCarManager();
        void gotoUserManager();
        void gotoRecord();
        void gotoMapDownload();
        void gotoSettingManager();
        void gotoAboutUs();
        void saveImage();
        void changeUserIcon();
        void getIcon();
    }
}
