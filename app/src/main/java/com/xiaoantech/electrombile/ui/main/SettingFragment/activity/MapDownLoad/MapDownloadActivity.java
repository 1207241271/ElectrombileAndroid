package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.MapDownLoad;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.orhanobut.logger.Logger;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.model.OfflineMapModel;
import com.xiaoantech.electrombile.ui.AddDevice.ChooseBindActivity;
import com.xiaoantech.electrombile.widget.Adapter.OffLineMapExpandableListAdapter;
import com.xiaoantech.electrombile.widget.Adapter.OfflineMapManagerAdaper;

import java.util.ArrayList;

import static com.baidu.mapapi.map.offline.MKOLUpdateElement.DOWNLOADING;
import static com.baidu.mapapi.map.offline.MKOLUpdateElement.FINISHED;
import static com.baidu.mapapi.map.offline.MKOLUpdateElement.SUSPENDED;
import static com.baidu.mapapi.map.offline.MKOLUpdateElement.WAITING;

/**
 * Created by yangxu on 2016/12/14.
 */

public class MapDownloadActivity extends Activity implements MKOfflineMapListener {
    private static MKOfflineMap mkOfflineMap = null;
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private static OffLineMapExpandableListAdapter ExpandableListadapter;
    private static OfflineMapManagerAdaper ManagerListadapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_map);
        mkOfflineMap = new MKOfflineMap();
        mkOfflineMap.init(this);
        initView();

    }

    private void initView() {
        View view = (View)findViewById(R.id.navigation);
        ((TextView)view.findViewById(R.id.navigation_title)).setText("地图下载");
        ((RelativeLayout)view.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapDownloadActivity.this.finish();
            }
        });
        ExpandableListView allCityList = (ExpandableListView) findViewById(R.id.lv_mapOffline_allCity);
        ListView managerList = (ListView) findViewById(R.id.lv_mapOffline_manager);
        ArrayList<OfflineMapModel> allCities = new ArrayList<OfflineMapModel>();
        //获取城市列表
        ArrayList<MKOLSearchRecord> records = mkOfflineMap.getOfflineCityList();
        if (records != null) {
            for (MKOLSearchRecord r : records) {
                OfflineMapModel bean = new OfflineMapModel();
                bean.setCityName(r.cityName);
                bean.setCityId(r.cityID);
                bean.setState(OfflineMapModel.STATE.NONE);
                ArrayList<OfflineMapModel> childCities = new ArrayList<OfflineMapModel>();
                if (r.childCities != null) {
                    for (MKOLSearchRecord childCity : r.childCities) {
                        OfflineMapModel childbean = new OfflineMapModel();
                        childbean.setCityName(childCity.cityName + "(" + childCity.cityID + ")" + "   --"
                                + this.formatDataSize(childCity.size));
                        childbean.setCityId(childCity.cityID);
                        childbean.setState(OfflineMapModel.STATE.NONE);
                        childCities.add(childbean);
                    }
                } else {
                    OfflineMapModel childbean = new OfflineMapModel();
                    childbean.setCityName(r.cityName + "(" + r.cityID + ")" + "   --"
                            + this.formatDataSize(r.size));
                    childbean.setCityId(r.cityID);
                    childbean.setState(OfflineMapModel.STATE.NONE);
                    childCities.add(childbean);
                }
                bean.setChildCities(childCities);
                allCities.add(bean);
            }
            ExpandableListadapter = new OffLineMapExpandableListAdapter(allCities, this, mkOfflineMap);
            allCityList.setAdapter(ExpandableListadapter);
            //根据本地信息更新状态
            localMapList = mkOfflineMap.getAllUpdateInfo();
            if (localMapList == null) {
                localMapList = new ArrayList<MKOLUpdateElement>();
            } else {
                for (MKOLUpdateElement element : localMapList) {
                    switch (element.status) {
                        case FINISHED:
                            ExpandableListadapter.getBeanbyId(element.cityID).setState(OfflineMapModel.STATE
                                    .FINISHED);
                            break;
                        case WAITING:
                            ExpandableListadapter.getBeanbyId(element.cityID).setState(OfflineMapModel.STATE.LOADING);
                            break;
                        case DOWNLOADING:
                            ExpandableListadapter.getBeanbyId(element.cityID).setState(OfflineMapModel.STATE.LOADING);
                            ExpandableListadapter.getBeanbyId(element.cityID).setProgress(element.ratio);
                            break;
                        case SUSPENDED:
                            ExpandableListadapter.getBeanbyId(element.cityID).setState(OfflineMapModel.STATE.SUSPENDED);
                    }
                }
                ExpandableListadapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this,"获取城市列表失败，请重试",Toast.LENGTH_LONG);
        }
        ManagerListadapter = new OfflineMapManagerAdaper(this, mkOfflineMap);
        managerList.setAdapter(ManagerListadapter);
    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }


    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                // 处理下载进度更新提示
                Logger.i("下载更新");
                MKOLUpdateElement update = mkOfflineMap.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    if (update.ratio == 100) {
                        ExpandableListadapter.getBeanbyId(state).setState(OfflineMapModel.STATE.FINISHED);
                    } else {
                        ExpandableListadapter.setDownProgress(update.ratio, state);
                    }
                    ExpandableListadapter.updateView();
                }
                ManagerListadapter.updateView();
            }
            break;

            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Logger.i("新的离线地图安装");
//                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                Logger.i("版本更新");
                MKOLUpdateElement update = mkOfflineMap.getUpdateInfo(state);
                if (update != null) {
                    ExpandableListadapter.getBeanbyId(state).setState(OfflineMapModel.STATE.UPDATE);
                }
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
        }
    }

    /**
     * 开始下载
     */
    public static void downloadCity(int cityId) {
        com.orhanobut.logger.Logger.i("开始下载离线地图ID是：" + cityId);
        ExpandableListadapter.getBeanbyId(cityId).setState(OfflineMapModel.STATE.LOADING);
        //MapOfflineActivity.ManagernotifyDataSetChanged();
        mkOfflineMap.start(cityId);
        ExpandableListadapter.updateView();
        ManagerListadapter.updateView();
    }

    /**
     * 暂停下载
     */
    public static void stop(int cityId) {
        mkOfflineMap.pause(cityId);
        ExpandableListadapter.getBeanbyId(cityId).setState(OfflineMapModel.STATE.SUSPENDED);
        ExpandableListadapter.updateView();
        ManagerListadapter.updateView();
    }

    /**
     * 删除离线地图
     */
    public static void remove(int cityId) {
        mkOfflineMap.remove(cityId);
        OfflineMapModel bean = ExpandableListadapter.getBeanbyId(cityId);
        if (bean != null) {
            bean.setState(OfflineMapModel.STATE.NONE);
            bean.setProgress(0);
        }
        //MapOfflineActivity.ManagernotifyDataSetChanged();
        ExpandableListadapter.updateView();
        ManagerListadapter.updateView();
    }
}
