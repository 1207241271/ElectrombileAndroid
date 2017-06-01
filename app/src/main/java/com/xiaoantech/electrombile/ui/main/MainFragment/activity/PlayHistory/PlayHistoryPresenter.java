package com.xiaoantech.electrombile.ui.main.MainFragment.activity.PlayHistory;

import com.xiaoantech.electrombile.event.http.HttpGetEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by yangxu on 2016/11/27.
 */

public class PlayHistoryPresenter implements PlayHistoryContract.Presenter{
    private PlayHistoryContract.View mPlayHistoryView;

    protected PlayHistoryPresenter(PlayHistoryContract.View playHistoryView){
        subscribe();
        this.mPlayHistoryView = playHistoryView;
        mPlayHistoryView.setPresenter(this);
    }

    @Override
    public void switchPlayStatus() {
        mPlayHistoryView.switchPlayStatus();
    }

    @Override
    public void switchPlaySpeed() {
        mPlayHistoryView.switchPlaySpeed();
    }

    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHttpEvent(HttpGetEvent event){

    }

}
