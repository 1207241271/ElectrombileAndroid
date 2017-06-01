package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.databinding.ActivityCarmanagerBinding;
import com.xiaoantech.electrombile.manager.BasicDataManager;
import com.xiaoantech.electrombile.manager.LocalDataManager;
import com.xiaoantech.electrombile.model.CarInfoModel;
import com.xiaoantech.electrombile.model.GPSPointModel;
import com.xiaoantech.electrombile.mqtt.MqttManager;
import com.xiaoantech.electrombile.ui.AddDevice.CaptureActivity;
import com.xiaoantech.electrombile.ui.AddDevice.ChooseBindActivity;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;
import com.xiaoantech.electrombile.ui.login.Login.LoginActivity;
import com.xiaoantech.electrombile.ui.main.MainFragment.activity.MapHistory.MapListActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.CarManager.CarInfoDetail.CarInfoDetailActivity;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.SettingManager.SettingManagerActivity;
import com.xiaoantech.electrombile.utils.TimeUtil;
import com.xiaoantech.electrombile.widget.Dialog.CustomDialog;
import com.xiaoantech.electrombile.widget.HistoryRouteCell;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.halfbit.pinnedsection.PinnedSectionListView;

/**
 * Created by yangxu on 2016/12/14.
 */

public class CarManagerActivity extends BaseAcitivity implements  CarManagerContract.View {
    private ActivityCarmanagerBinding mBinding;
    private CarManagerContract.Presenter mPresenter;

    private List<Map<String, Object>> otherCarList;

    private static final String CarImage = "carImage";
    private static final String CarName = "carName";

    private myListAdapter mAdapter;

    @Override
    protected void initBefore() {
        getOtherList();
    }

    private void getOtherList() {
        otherCarList = new ArrayList<>();
        HashMap<String, Object> map;
        List<CarInfoModel> carInfoModelList = BasicDataManager.getInstance().getCarInfoList();
        for (int i = 1; i < carInfoModelList.size(); i++) {
            map = new HashMap<>();
            map.put(CarName, carInfoModelList.get(i).getName());
            map.put(CarImage, R.drawable.othercar);
            otherCarList.add(map);
        }
    }

    @Override
    protected void bindData() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_carmanager);
    }

    @Override
    protected void initView() {
        mPresenter = new CarManagerPresenter(this);
        mBinding.setPresenter(mPresenter);

        ((TextView) mBinding.navigation.findViewById(R.id.navigation_title)).setText("车辆管理");
        ((RelativeLayout) mBinding.navigation.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarManagerActivity.this.finish();
            }
        });

        //已绑定设备显示
        mBinding.imgBindCar.setImageResource(R.drawable.othercar);
        mBinding.txtBindCarName.setText(BasicDataManager.getInstance().getBindCarInfo().getName());

        //非绑定列表设备显示
        mAdapter = new myListAdapter(otherCarList,R.layout.item_othercar_list,this);
        mBinding.listView.setAdapter(mAdapter);

        mBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CarManagerActivity.this, CarInfoDetailActivity.class);
                intent.putExtra("index", position + 1);
                try {
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void reSetBind() {
        getOtherList();
        mBinding.imgBindCar.setImageResource(R.drawable.othercar);
        mBinding.txtBindCarName.setText(BasicDataManager.getInstance().getBindCarInfo().getName());

        mAdapter = new myListAdapter(otherCarList,R.layout.item_othercar_list,this);
        mBinding.listView.setAdapter(mAdapter);
    }

    @Override
    public void reSetOther() {
        getOtherList();
        mAdapter = new myListAdapter(otherCarList,R.layout.item_othercar_list,this);
    }

    @Override
    public void showDeleteDialog() {
        CustomDialog.Builder customDialog = new  CustomDialog.Builder(this);
        customDialog.setTitle("删除").setMessage("您将删除当前车辆").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.delete();
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void setPresenter(CarManagerContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void addDevice() {
        Intent intent = new Intent(CarManagerActivity.this, ChooseBindActivity.class);
        startActivity(intent);
    }

    @Override
    public void gotoBindedCarInfo(int index) {
        Intent intent = new Intent(CarManagerActivity.this, CarInfoDetailActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    public void quit() {
        Intent intent = new Intent(CarManagerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        reSetBind();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    public  class myListAdapter extends BaseAdapter {

        private List<Map<String, Object>> otherCarList;//ListView显示的数据

        private int resource;//显示列表项的Layout

        private LayoutInflater inflater;//界面生成器

        private Context context;


        public myListAdapter(List<Map<String, Object>> otherCarList, int resource, Context context) {

            this.otherCarList = otherCarList;

            this.resource = resource;

            this.context = context;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return otherCarList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return otherCarList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }


        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            if (arg1 == null) {
                arg1 = inflater.inflate(resource, null);
            }

            Map<String, Object> carInfo = otherCarList.get(arg0);
            TextView carName = (TextView) arg1.findViewById(R.id.txt_car_name);
            carName.setText(carInfo.get(CarName).toString());

            ImageView delete = (ImageView)arg1.findViewById(R.id.delete_car);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.deleteDevice(1 + arg0);
                }
            });

            return arg1;

        }

    }
}
