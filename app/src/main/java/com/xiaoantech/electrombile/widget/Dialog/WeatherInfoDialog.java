package com.xiaoantech.electrombile.widget.Dialog;

import android.app.Dialog;
import android.app.WallpaperInfo;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangxu on 2017/2/5.
 */

public class WeatherInfoDialog extends Dialog {
    public WeatherInfoDialog(Context context) {
        super(context);
    }

    public WeatherInfoDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private SimpleDialog simpleDialog;
        private Context context;
        private JSONObject weatherInfo;
        private String  location;

        public Builder(Context context) {
            this.context = context;
        }

        public WeatherInfoDialog.Builder setMessage(String location) {
            this.location = location;
            return this;
        }

        public WeatherInfoDialog.Builder setWeatherInfo(JSONObject weatherInfo){
            this.weatherInfo = weatherInfo;
            return this;
        }
        public WeatherInfoDialog.Builder setPlaceInfo(String placeInfo){
            this.location = placeInfo;
            return this;
        }


        public SimpleDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            simpleDialog = new SimpleDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.content_weather, null);
            simpleDialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if (location != null){
                ((TextView)layout.findViewById(R.id.txt_location)).setText(location);
            }

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd,HH:mm");
            String dateToday = dateFormat.format(date);
            ((TextView)layout.findViewById(R.id.txt_date_today)).setText(dateToday);
            ((Button)layout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    simpleDialog.dismiss();
                }
            });

            if (location != null){
                ((TextView)layout.findViewById(R.id.txt_location)).setText(location);
            }

            if (weatherInfo != null){
                try {
                    JSONObject data = weatherInfo.getJSONObject("data");
                    //当前温度
                    String currentTempture = data.getString("wendu");
                    ((TextView)layout.findViewById(R.id.txt_today_temperature)).setText(currentTempture);

                    String fontPath = "fonts/dincond-regular.ttf";
                    ((TextView)layout.findViewById(R.id.txt_today_temperature)).setTypeface(Typeface.createFromAsset(context.getAssets(),fontPath));
                    //设置今日天气
                    JSONArray forecast = data.getJSONArray("forecast");
                    JSONObject todayForecast = forecast.getJSONObject(0);
                    String todayweather = todayForecast.getString("type");
                    setImageView((ImageView)layout.findViewById(R.id.img_weather_today),todayweather);

                    Date secondDay = new Date(date.getTime() + 86400000);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEEE MM-dd");

                    //设置明日信息
                    ((TextView)layout.findViewById(R.id.txt_date_second_day)).setText(dateFormat1.format(secondDay));
                    JSONObject secondForecast = forecast.getJSONObject(1);
                    //设置明日天气
                    String secondWeather = secondForecast.getString("type");
                    setImageView((ImageView)layout.findViewById(R.id.img_weather_second_day),secondWeather);
                    //设置明日气温
                    String highTempture = secondForecast.getString("high");
                    highTempture = highTempture.substring(3,highTempture.length() - 1);
                    String lowTempture = secondForecast.getString("low");
                    lowTempture = lowTempture.substring(3);
                    String secondTempture = highTempture + "~" +lowTempture;

                    ((TextView)layout.findViewById(R.id.txt_temperature_second_day)).setText(secondTempture);

                    Date thirdDate = new Date(date.getTime() + 2*86400000);
                    ((TextView)layout.findViewById(R.id.txt_date_third_day)).setText(dateFormat1.format(thirdDate));
                    JSONObject thirdForecast = forecast.getJSONObject(2);
                    //设置明日天气
                    String thirdWeather = thirdForecast.getString("type");
                    setImageView((ImageView)layout.findViewById(R.id.img_weather_third_day),thirdWeather);
                    //设置明日气温
                    String highTempture1 = thirdForecast.getString("high");
                    highTempture1 = highTempture1.substring(3,highTempture1.length() - 1);
                    String lowTempture1 = thirdForecast.getString("low");
                    lowTempture1 = lowTempture1.substring(3);
                    String thirdTempture = highTempture1 + "~" +lowTempture1;

                    ((TextView)layout.findViewById(R.id.txt_temperature_third_day)).setText(thirdTempture);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }



            simpleDialog.setContentView(layout);
            return simpleDialog;
        }

        private void setImageView(ImageView imageView,String weather){
            if (weather.contains("云")||weather.contains("阴")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.img_weather_cloudy));
            }else if (weather.contains("晴")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.img_weather_sunny));
            }else if (weather.contains("雨")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.img_weather_rainy));
            }else if (weather.contains("雪")){
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.img_weather_snowy));
            }
        }


        public SimpleDialog getSimpleDialog(){
            return simpleDialog;
        }
    }
}
