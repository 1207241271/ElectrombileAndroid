package com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.constant.TimerConstant;
import com.xiaoantech.electrombile.model.GPSPointModel;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.Map.MapContract;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.PlayHistory.PlayHistoryActivity;
import com.xiaoantech.electrombile.utils.TimeUtil;
import com.xiaoantech.electrombile.widget.HistoryRouteCell;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by yangxu on 2016/11/25.
 */

public class MapListActivity extends ListActivity implements MapListContract.View{
    private MapListContract.Presenter       mPresenter;
    protected ProgressDialog mProgressDialog;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maplist);

        ((TextView)findViewById(R.id.navigation_title)).setText("行车历史");
        ((RelativeLayout)findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapListActivity.this.finish();
            }
        });

        mPresenter = new MapListPresenter(this);
        mPresenter.getSevenDayRoute(0);
        mProgressDialog = new ProgressDialog(this);
//        setListAdapter();
    }


    @Override
    public void onDestroy(){
        this.mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void refreshList(List<List<Map<String,String>>> routeList){
        setListAdapter(new MyListAdapter(this, R.layout.cell_historyroute, android.R.id.text1,routeList));
    }

    @Override
    public void gotoPlayHistory(ArrayList<GPSPointModel> gpsPointModels){
        PlayHistoryActivity.pointList = gpsPointModels;
        Intent intent = new Intent(MapListActivity.this,PlayHistoryActivity.class);
        startActivity(intent);
    }

    public void showToast(String errorMeg) {
        Toast.makeText(this,errorMeg,Toast.LENGTH_SHORT).show();
        mProgressDialog.cancel();
    }

    public void showWaitingDialog(String dialogString) {
        try {
            mProgressDialog.setMessage(dialogString);
            mProgressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void hideWaitingDialog() {
        mProgressDialog.cancel();
    }

    static class Item{
        public static final int ITEM = 0;
        public static final int SECTION = 1;

        public final int type;
        public HistoryRouteCell cell;
        public int sectionPosition;
        public int listPosition;
        public String text;
        public int sectionIndex;
        public Item(int type,HistoryRouteCell listViewCell){
            this.type = type;
            this.cell = listViewCell;
            this.text = listViewCell.toString();
        }

        public Item(int type,int index){
            this.type = type;
            this.sectionIndex = index;
        }
    }

    public void setPresenter(MapListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    static class MyListAdapter extends ArrayAdapter<Item> implements PinnedSectionListView.PinnedSectionListAdapter,SectionIndexer{
        private Item[] sections;
        private ArrayList<Integer> mSectionToList;
        private List<List<Map<String,String>>> mRouteList;
        public MyListAdapter(Context context,int resource,int textViewResourceId,List<List<Map<String,String>>> routeList){
            super(context,resource,textViewResourceId);
            mRouteList = routeList;
            generateDataSet(false);
        }
        public void generateDataSet(boolean clear) {
            if (clear)clear();
            int sectionNum = 0;
            mSectionToList = new ArrayList<>();
            for (int i = 0;i < mRouteList.size();i++){
                if (mRouteList.get(i).size()>0){
                    mSectionToList.add(i);
                    sectionNum ++;
                }
            }

            prepareSections(sectionNum);
            int sectionPosition = 0, listPosition = 0;
            for (int i = 0;i < sectionNum;i++){
                Item section = new Item(Item.SECTION,mSectionToList.get(i));
                section.sectionPosition = sectionPosition;
                section.listPosition = listPosition++;
                onSectionAdded(section,sectionPosition);
                add(section);

                for (int j = 0;j < mRouteList.get(i).size();j++){
                    Map<String,String> map = mRouteList.get(i).get(j);
                    GPSPointModel startPoint = new GPSPointModel(Long.parseLong(map.get("startTimestamp"))
                            ,Double.parseDouble(map.get("startlat")),Double.parseDouble(map.get("startlon")));
                    GPSPointModel endPoint = new GPSPointModel(Long.parseLong(map.get("endTimestamp"))
                            ,Double.parseDouble(map.get("endlat")),Double.parseDouble(map.get("endlon")));
                    HistoryRouteCell cell = new HistoryRouteCell(getContext(),Integer.parseInt(map.get("miles")),startPoint,endPoint);
                    Item item = new Item(Item.ITEM,cell);
                    item.sectionPosition = sectionPosition;
                    item.listPosition = listPosition++;
                    add(item);
                }
                sectionPosition++;
            }

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item = getItem(position);
            if (item.type == Item.SECTION){
                TextView view = new TextView(getContext());
                view.setHeight(70);
                view.setTextSize(20);
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setText("      "+TimeUtil.getDateStringFromTimeStamp(TimeUtil.getCurrentTime()-item.sectionIndex*86400));
                return view;
            }else {
                return item.cell;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }

        protected void prepareSections(int sectionsNumber){
            sections = new Item[sectionsNumber];
        }

        protected void onSectionAdded(Item section,int sectionPosition){
            sections[sectionPosition] = section;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            if (sectionIndex >= sections.length){
                sectionIndex = sections.length-1;
            }
            return sections[sectionIndex].listPosition;
        }

        @Override
        public int getSectionForPosition(int position) {
            if (position >= getCount()){
                position = getCount() - 1;
            }
            Item item = getItem(position);
            if (null != item){
                return item.sectionPosition;
            }
            return 0;
        }

        @Override
        public Item[] getSections() {
            return sections;
        }


    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListView().getAdapter().getItem(position);
        if (item != null) {
            JSONObject jsonobject = item.cell.getTimeStamp();
            try {
                long startTimeStamp = jsonobject.getLong("start");
                long endTimeStamp = jsonobject.getLong("end");
                mPresenter.getGPSPoints(startTimeStamp,endTimeStamp);
            }catch (Exception e){
                e.printStackTrace();
            }
//            mPresenter.getGPSPoints(item.cell.);
            Toast.makeText(this, "Item " + position + ": " + item.text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item " + position, Toast.LENGTH_SHORT).show();
        }
    }

}
