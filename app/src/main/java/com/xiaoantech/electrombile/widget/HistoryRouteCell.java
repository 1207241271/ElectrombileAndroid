package com.xiaoantech.electrombile.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.model.GPSPointModel;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yangxu on 2016/11/25.
 */

public class HistoryRouteCell extends ConstraintLayout {
    private UnitTextView  mItineraryStv;
    private TextView mTimeStv;
    private UnitTextView  mSpeedStv;
    private TextView    mItineraryTv;
    private TextView    mTimeTv;
    private TextView    mSpeedTv;

    private int mItineray;
    private GPSPointModel mStartPoint;
    private GPSPointModel mEndPoint;

    private TextView    mTimeQuantumTv;

    public HistoryRouteCell(Context context, int itinerary, GPSPointModel startPoint, GPSPointModel endPoint){
        this(context);
        mItineray = itinerary;
        mStartPoint = startPoint;
        mEndPoint = endPoint;
        if (startPoint != null) {
            refreshLayout();
        }
    }

    public HistoryRouteCell(Context context){
        super(context);
        layoutInit(context);
    }

    public HistoryRouteCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInit(context);
    }

    private void layoutInit(Context context){
        LayoutInflater.from(context).inflate(R.layout.cell_historyroute,this);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
        mTimeQuantumTv = (TextView)findViewById(R.id.timeQuantum_tv);



        mItineraryStv = (UnitTextView) findViewById(R.id.itinerary_Stv);

        mTimeStv = (TextView)findViewById(R.id.time_Stv);
        String fontPath = "fonts/dincond-regular.ttf";
        mTimeStv.setTypeface(Typeface.createFromAsset(getContext().getAssets(),fontPath));
        mSpeedStv = (UnitTextView) findViewById(R.id.speed_Stv);

        mItineraryTv = (TextView)findViewById(R.id.itinerary_tv);
        mTimeTv = (TextView)findViewById(R.id.time_tv);
        mSpeedTv = (TextView)findViewById(R.id.speed_tv);

        setSign();
        setDefaultData();
        setNum();
        adjustTextViewWidth();
    }

    private void setDefaultData(){
        mEndPoint = new GPSPointModel(1479956127,114.5,34.1);
        mStartPoint = new GPSPointModel(1479954127,114.5,34.1);
        mItineray = 14556;
    }

    private void refreshLayout(){
        setNum();
        adjustTextViewWidth();
    }
    /*
    **调整数字下方textview宽度，使得文字与数字居中对齐
     */
    private void adjustTextViewWidth(){
//        mItineraryTv.setWidth(mItineraryStv.getNumTextWidth());
//        mSpeedTv.setWidth(mSpeedStv.getNumTextWidth());
    }

    private void setSign(){
        mItineraryStv.setSignText("km");
        mTimeStv.setText("");
        mSpeedStv.setSignText("km/h");
    }

    public void setNum(){
        String itinerary = String.format(Locale.CHINA,"%.1f",mItineray/1000.0);
        mItineraryStv.setNumText(itinerary);

        int timeInterval = (int)(mEndPoint.getTimestamp() - mStartPoint.getTimestamp());
        int minutes = timeInterval/60;
        int seconds = timeInterval%60;
        String minStr = String.format(Locale.CHINA,"%02d",minutes);
        String secStr = String.format(Locale.CHINA,"%02d",seconds);
        mTimeStv.setText(minStr + ":" + secStr);

        double hours = timeInterval/3600.0;
        double speed = mItineray/(hours*1000);
        mSpeedStv.setNumText(String.format(Locale.CHINA,"%.1f",speed));

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        String startMinute = sdf.format(new Date(mStartPoint.getTimestamp()*1000));
        String endMintute = sdf.format(new Date(mEndPoint.getTimestamp()*1000));
        String timeQuantum = startMinute + "-" + endMintute;
        mTimeQuantumTv.setText(timeQuantum);
    }

    @Override
    public String toString(){
        return mStartPoint.toString();
    }

    public JSONObject getTimeStamp(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start",mStartPoint.getTimestamp());
            jsonObject.put("end",mEndPoint.getTimestamp());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

}
