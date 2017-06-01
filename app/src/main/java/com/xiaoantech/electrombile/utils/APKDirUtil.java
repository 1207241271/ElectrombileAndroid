package com.xiaoantech.electrombile.utils;

import android.os.Environment;

import com.xiaoantech.electrombile.application.App;

import java.io.File;

/**
 * Created by yangxu on 2017/3/6.
 */

public class APKDirUtil {
    public static String initAPKDir() {
        String APK_dir = null;
        if (isHasSdcard())// 判断是否插入SD卡
        {
            APK_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Electromile/download/";// 保存到SD卡路径下
        }
        else{
            APK_dir = App.getContext().getFilesDir().getAbsolutePath() + "/Electromile/download/";// 保存到app的包名路径下
        }
        File destDir = new File(APK_dir);
        if (!destDir.exists()) {// 判断文件夹是否存在
            destDir.mkdirs();
        }
        return APK_dir;
    }

    private static boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

}
