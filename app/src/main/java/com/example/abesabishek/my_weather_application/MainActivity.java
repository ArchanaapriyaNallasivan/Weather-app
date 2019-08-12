package com.example.abesabishek.my_weather_application;


import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //OpenWeather API key
    public static String OPEN_WEATHER_MAP_API = "1b4b558b9cca34619f92e6b00daeeaf4";

    TextView emptyTextView, cityField, detailsField, currentTemperatureField, humidity_field, clouds_field, weatherIcon, updatedField, min_temperature, max_temperature, mainWeatherField;

    Typeface weatherFont;

    private EditText citySearch;
    private String cityName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test); //setting the layout


        citySearch = (EditText) findViewById(R.id.editText);
        ImageButton imgSearch = (ImageButton) findViewById(R.id.searchImageButton);
        final ImageView maxTempImg, minTempImg, cloudsImg, humidityImg;

        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/weathericons_regular_webfont.ttf");

        emptyTextView = (TextView) findViewById(R.id.emptyTextView);
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        clouds_field = (TextView)findViewById(R.id.clouds_field);
        min_temperature = (TextView) findViewById(R.id.min_temperature);
        max_temperature = (TextView) findViewById(R.id.max_temperature);
        mainWeatherField = (TextView) findViewById(R.id.main_weather_field);
        minTempImg = (ImageView) findViewById(R.id.minTempImg);
        maxTempImg = (ImageView) findViewById(R.id.maxTempImg);
        cloudsImg = (ImageView) findViewById(R.id.cloudsImg);
        humidityImg = (ImageView) findViewById(R.id.humidityImg);


        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        // setting zero and null value to the elements of the application
        minTempImg.setImageResource(0);
        maxTempImg.setImageResource(0);
        cloudsImg.setImageResource(0);
        humidityImg.setImageResource(0);
        cityField.setText("");
        updatedField.setText("");
        detailsField.setText("");
        currentTemperatureField.setText("");
        humidity_field.setText("");
        clouds_field.setText("");
        min_temperature.setText("");
        max_temperature.setText("");
        mainWeatherField.setText("");
        weatherIcon.setText(Html.fromHtml(""));


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityName = citySearch.getText().toString();

                placeIdTask asyncTask =new placeIdTask(new AsyncResponse() {
                    public void processFinish(String weather_minTemp, String weather_maxTemp, String weather_mainWeather, String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_clouds, String weather_updatedOn, String weather_iconText, String sun_rise) {

                        if (weather_city != "") {
                            //setting value received from the response to the elements in the application
                            minTempImg.setImageResource(R.drawable.min_temp);
                            maxTempImg.setImageResource(R.drawable.max_temp);
                            cloudsImg.setImageResource(R.drawable.clouds);
                            humidityImg.setImageResource(R.drawable.humidity);
                            cityField.setText(weather_city);
                            updatedField.setText(weather_updatedOn);
                            detailsField.setText(weather_description);
                            currentTemperatureField.setText(weather_temperature);
                            humidity_field.setText("Humidity: " + weather_humidity);
                            clouds_field.setText("Clouds: " + weather_clouds);
                            min_temperature.setText("Min Temp: " + weather_minTemp);
                            max_temperature.setText("Max Temp: " + weather_maxTemp);
                            mainWeatherField.setText(weather_mainWeather);
                            weatherIcon.setText(Html.fromHtml(weather_iconText));
                            Toast.makeText(MainActivity.this, "Weather updated for city: "+weather_city, Toast.LENGTH_SHORT).show();
                    }
                    else{
                            minTempImg.setImageResource(0);
                            maxTempImg.setImageResource(0);
                            cloudsImg.setImageResource(0);
                            humidityImg.setImageResource(0);
                            cityField.setText("");
                            updatedField.setText("");
                            detailsField.setText("");
                            currentTemperatureField.setText("");
                            humidity_field.setText("");
                            clouds_field.setText("");
                            min_temperature.setText("");
                            max_temperature.setText("");
                            mainWeatherField.setText("");
                            weatherIcon.setText(Html.fromHtml(""));
                            Toast.makeText(MainActivity.this, "City Not Found or Invalid characters entered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                asyncTask.execute(cityName); // calling function of asyncTask
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }
    //setting the weather icon based on the id received from the response
    public static String setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch (id) {
                case 2:
                    icon = "&#xf01e;";
                    break;
                case 3:
                    icon = "&#xf01c;";
                    break;
                case 7:
                    icon = "&#xf014;";
                    break;
                case 8:
                    icon = "&#xf013;";
                    break;
                case 6:
                    icon = "&#xf01b;";
                    break;
                case 5:
                    icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }


    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8, String output9, String output10, String output11);
    }


    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null; //Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse; //Assigning call back interface through constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;
        }

        //receiving the values from the JSON object and setting it to the String variables
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    JSONObject clouds = json.getJSONObject("clouds");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.2f", main.getDouble("temp")) + "°C";
                    String humidity = main.getString("humidity") + "%";
                    String clouds_percentage = clouds.getString("all") + " %";
                    String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
                    String minTemperature = main.getString("temp_min") + "°C";
                    String maxTemperature = main.getString("temp_max") + "°C";
                    String mainWeather = details.getString("main");
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);

                    //returning the values to the called function
                    delegate.processFinish(minTemperature, maxTemperature, mainWeather, city, description, temperature, humidity, clouds_percentage, updatedOn, iconText, "" + (json.getJSONObject("sys").getLong("sunrise") * 1000));

                }

                else{
                    delegate.processFinish("","","","","","","","","","","");
                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }


        }
    }


    public static JSONObject getWeatherJSON(String cityName) {

        try {
            URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&APPID=1b4b558b9cca34619f92e6b00daeeaf4&units=metric"));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (Exception e) {
            return null;
        }
    }


}
