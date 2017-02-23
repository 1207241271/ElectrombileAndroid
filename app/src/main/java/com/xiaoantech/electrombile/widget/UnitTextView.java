package com.xiaoantech.electrombile.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;

/**
 * Created by yangxu on 2016/11/25.
 */

public class UnitTextView extends RelativeLayout {
    private TextView mNumTv;
    private TextView    mSignTv;
    public UnitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.textview_withunit,this);
        mNumTv = (TextView) findViewById(R.id.num_text);
        mSignTv = (TextView) findViewById(R.id.sign_text);
        String fontPath = "fonts/dincond-regular.ttf";
        mNumTv.setTypeface(Typeface.createFromAsset(getContext().getAssets(),fontPath));
    }

    public void setNumText(String numText){
        mNumTv.setText(numText);
    }

    public void setSignText(String signText){
        mSignTv.setText(signText);
    }

    public int getNumTextWidth(){
        int w = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mNumTv.measure(w,h);
        Log.d("width",String.valueOf(mNumTv.getMeasuredWidth()));
        return mNumTv.getMeasuredWidth();
    }
}
