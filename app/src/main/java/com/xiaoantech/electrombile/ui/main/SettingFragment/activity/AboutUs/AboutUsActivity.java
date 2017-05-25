package com.xiaoantech.electrombile.ui.main.SettingFragment.activity.AboutUs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaoantech.electrombile.BuildConfig;
import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.ui.main.SettingFragment.activity.MapDownLoad.MapDownloadActivity;

/**
 * Created by yangxu on 2016/12/14.
 */

public class AboutUsActivity extends Activity{

    Button returnBtn;
    Button feedbackBtn;
    TextView tv_appInfo;
    String versionName;
    private ImageView imageView;
    private int       clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        View view = (View)findViewById(R.id.navigation);
        ((TextView)view.findViewById(R.id.navigation_title)).setText("版本信息");
        ((RelativeLayout)view.findViewById(R.id.navigation_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.this.finish();
            }
        });



        returnBtn = (Button)findViewById(R.id.btn_returnFromFelp);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_appInfo = (TextView)findViewById(R.id.tv_appInfo);
        tv_appInfo.setText("版本信息：小安宝内测版" + 1.15);

        clickCount = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
