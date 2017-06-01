package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.Record;

import com.xiaoantech.electrombile.base.BasePresenter;
import com.xiaoantech.electrombile.base.BaseView;

/**
 * Created by yangxu on 2017/3/2.
 */

public interface RecordContract {

    interface View extends BaseView<Presenter> {
        void startRecord();
        void stopRecord();
        void changeCutDownText(String title);
        void startPlay(String filePath);
        void changePlayStatus();
        void stopPlay();

        void resetView();
//        void changeButtonStatus(int buttonID,boolean isEnable);
    }

    interface Presenter extends BasePresenter{
        void onPlay();
        void onStop();
    }
}
