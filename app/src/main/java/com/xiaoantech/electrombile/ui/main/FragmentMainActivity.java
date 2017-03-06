package com.xiaoantech.electrombile.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.ui.main.InfoFragment.InfoFragment;
import com.xiaoantech.electrombile.ui.main.MainFragment.MainFragment;
import com.xiaoantech.electrombile.ui.main.SettingFragment.SettingFragment;

import java.util.ArrayList;

/**
 * Created by yangxu on 2016/11/1.
 */

public class FragmentMainActivity extends FragmentActivity {
    private ViewPager   mViewPager;
    private RadioButton mainBtn,infoBtn,settingBtn;
    private ArrayList<Fragment> fragmentArrayList;
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        initRadioBtn();
        initFragmentArray();
        initViewPager();
    }

    private void initRadioBtn(){
        mainBtn = (RadioButton)findViewById(R.id.radioBtn_main);
        infoBtn = (RadioButton)findViewById(R.id.radioBtn_info);
        settingBtn = (RadioButton)findViewById(R.id.radioBtn_settings);

        mainBtn.setOnClickListener(new radioBtnListener(0));
        infoBtn.setOnClickListener(new radioBtnListener(1));
        settingBtn.setOnClickListener(new radioBtnListener(2));


    }

    private void initFragmentArray(){
        fragmentArrayList = new ArrayList<>();
        MainFragment mainFragment = new MainFragment();
        mainFragment.mContext = FragmentMainActivity.this;

        InfoFragment infoFragment = new InfoFragment();
        infoFragment.mContext = FragmentMainActivity.this;

        SettingFragment settingFragment = new SettingFragment();
        settingFragment.mContext = FragmentMainActivity.this;
        fragmentArrayList.add(mainFragment);
        fragmentArrayList.add(infoFragment);
        fragmentArrayList.add(settingFragment);
    }

    public class radioBtnListener implements View.OnClickListener{
        private  int index = 0;
        public radioBtnListener(int i){
            index = i;
        }
        @Override
        public void onClick(View view){
            mViewPager.setCurrentItem(index);
        }
    }

    private void initViewPager(){
        mViewPager = (ViewPager)findViewById(R.id.viewPager_Fragment);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragmentArrayList));
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        mViewPager.setCurrentItem(0);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if ((System.currentTimeMillis() - exitTime) > 500)
            {
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
