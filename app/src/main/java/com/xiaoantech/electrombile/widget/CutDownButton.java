package com.xiaoantech.electrombile.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;


import javax.security.auth.callback.Callback;

/**
 * Created by yangxu on 2016/11/16.
 */

public class CutDownButton extends TextView {
    private Context mContext;
    private int maxCutDownTime;
    private Callback    callback;

    public CutDownButton(Context context){
        this(context,null);
    }

    public CutDownButton(Context context, AttributeSet attrs){
        super(context,attrs);
    }


    @Override
    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }


}
