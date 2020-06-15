package com.c.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    public void searchForCity(View view){
        String cityName ;
        EditText city = (EditText)findViewById(R.id.cityName);
        cityName =  city.getText().toString();
        if(cityName == "") {
            Toast.makeText(MainActivity.this , "Enter a Valid city Name" , Toast.LENGTH_LONG).show();
        }else {
            Log.i("button ", "Search for " + cityName);
            WeatherReport weatherReport = new WeatherReport();
            String url_ = "https://openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=439d4b804bc8187953eb36d2a8c26a02";
            weatherReport.execute(url_);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public class WeatherReport extends AsyncTask<String  , Void , String >{
        String result = "";
        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream in  = urlConnection.getInputStream() ;
                InputStreamReader ins = new InputStreamReader(in);
                int i ;
                while ( (i  = ins.read()) !=  -1){
                    result = result + (char)i;
                }
                return  result;
            }
            catch(MalformedURLException e) {
                e.printStackTrace();
                return  "Failed";
            }
            catch (IOException e){
                e.printStackTrace();
                return  "Failed";
            }



        }

        @Override
        protected void onPreExecute() {
            TextView finalResult = (TextView)findViewById(R.id.finalResult);
            super.onPreExecute();
            finalResult.setText("");

        }

        @Override
        protected void onPostExecute(String s) {

            if (result != "") {


                try {
                    TextView finalResult = (TextView)findViewById(R.id.finalResult);
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject everyobject = (JSONObject) jsonArray.get(i);
                        Log.i("main", everyobject.getString("main"));
                        Log.i("main", everyobject.getString("description"));

                        JSONObject mainObject = (JSONObject) jsonObject.get("main");
                        // temp , humidity , pressure in one object
                        Double temp = mainObject.getDouble("temp");
                        Double pressure = mainObject.getDouble("pressure");
                        Double humidity = mainObject.getDouble("humidity");

                        // wind  in one object
                        JSONObject windObject = (JSONObject) jsonObject.get("wind");
                        Double windSpeed = windObject.optDouble("swind", 0.00);
                        Double windDegree = windObject.getDouble("deg");
                        Log.i("Wind Speed:  ", windSpeed.toString());
                        Log.i("wind :  ", windDegree.toString());

                        finalResult.setText(
                                "MAIN : " + everyobject.getString("main") + "\n" +
                                        "DESCRIPTION : " + everyobject.getString("description") + "\n" +
                                        "TEMPEARTURE : " + temp + "\n" +
                                        "PRESSURE    : " + pressure + "\n" +
                                        "HUMIDITY     :" + humidity
                        );


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.i("error", "Something went Wrong , Here are the Details of the errors");
                    e.printStackTrace();
                }

            }
            else{
                Toast.makeText(MainActivity.this , "Cant Find name you Entered" , Toast.LENGTH_LONG).show();
            }



        }

    }

}
