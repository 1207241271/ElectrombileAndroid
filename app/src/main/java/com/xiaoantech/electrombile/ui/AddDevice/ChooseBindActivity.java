package com.xiaoantech.electrombile.ui.AddDevice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseAcitivity;
import com.xiaoantech.electrombile.ui.AddDevice.InputIMEI.InputIMEIActivity;

/**
 * Created by yangxu on 2017/1/24.
 */

public class ChooseBindActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_bind);
        initView();
    }

    private void initView(){
        ConstraintLayout constraintLayout_scan = (ConstraintLayout)findViewById(R.id.constraintLayout_scan);
        ConstraintLayout constraintLayout_input = (ConstraintLayout)findViewById(R.id.constraintLayout_input);

        constraintLayout_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBindActivity.this,CaptureActivity.class);
                startActivity(intent);
            }
        });

        constraintLayout_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBindActivity.this, InputIMEIActivity.class);
                startActivity(intent);
            }
        });
    }
}
