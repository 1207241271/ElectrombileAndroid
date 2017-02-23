package com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCar;

import com.xiaoantech.electrombile.event.cmd.SeekEvent;
import com.xiaoantech.electrombile.event.fourtt.FourTT;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.FindCarMap.FindCarMapPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by yangxu on 2017/1/6.
 */

public class FindCarPresenter implements FindCarContract.Presenter {
    private FindCarContract.View mFindCarView;

    protected FindCarPresenter(FindCarContract.View findCarView){
        mFindCarView = findCarView;
        mFindCarView.setPresenter(this);
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
    public void OnSeekEvent(SeekEvent event){
        try{
            JSONObject jsonObject = event.getJsonObject();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFourTT(FourTT fourTT){
        try{
            JSONObject jsonObject = fourTT.getJsonObject();
            int intensity = jsonObject.getInt("intensity");
            mFindCarView.changeIntensity(intensity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
