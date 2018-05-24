package com.example.islab_kc_319.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

   TextView cityField, detailsField, currentTemperatureField, humidityField, pressureField, updatedField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.update_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidityField = (TextView)findViewById(R.id.humidity_field);
        pressureField = (TextView)findViewById(R.id.pressure_field);

        Weather.placeIdTask asyncTask = new Weather.placeIdTask(new Weather.AsyncResponse() {
            @Override
            public void processFinish(String weatherCity, String weatherUpdateOn, String weatherDescription, String weatherTemperature, String weatherHumidity, String weatherPressure) {
                cityField.setText(weatherCity);
                updatedField.setText(weatherUpdateOn);
                detailsField.setText(weatherDescription);
                currentTemperatureField.setText(weatherTemperature);
                humidityField.setText(weatherHumidity);
                pressureField.setText(weatherPressure);
            }
        });
        asyncTask.execute("28.613900","77.209000");
    }
}
