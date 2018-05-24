package com.example.islab_kc_319.weatherapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by islab-kc-319 on 2018/05/24.
 */

public class Weather {

    private static final String OPEN_WEATHER_MAP_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&units=metric";

    //Insert your weather API here
    private static final String OPEN_WEATHER_MAP_API = "f50d40c574309f50c0748f3696af3635";

    public interface AsyncResponse {
        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject>{

        public AsyncResponse delegate = null;

        public placeIdTask(AsyncResponse asyncResponse){
            delegate = asyncResponse;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonObjectWeather = null;
            try{
                jsonObjectWeather = getWeatherJSON(params[0], params[1]);
            }catch (Exception e){
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonObjectWeather;
        }

        protected void onPostExecute(JSONObject jsonObject){
            try{
                if(jsonObject != null){
                    JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = jsonObject.getJSONObject("main");
                    DateFormat dateFormat = DateFormat.getDateTimeInstance();
                    String city = jsonObject.getString("name").toUpperCase(Locale.JAPAN) + ", " + jsonObject.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.JAPAN);
                    String temperature = String.format("%.2f", main.getDouble("temp")) + "";
                    String humidity = main.getString("humidity") + "%";
                    String pressure = main.getString("pressure") + " hPa";
                    String updatedOn = dateFormat.format(new Date(jsonObject.getLong("dt")*1000));

                    delegate.processFinish(city, description, temperature, humidity, pressure, updatedOn);
                }
            }catch (Exception e){

            }
        }
    }

    public static JSONObject getWeatherJSON(String latitude, String longitude){
        try{
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, latitude, longitude));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while ((tmp=bufferedReader.readLine()) != null)
                json.append(tmp).append("\n");
            bufferedReader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("code") != 200){
                return  null;
            }

            return data;
        }catch (Exception e){
           return null;
        }
    }
}
