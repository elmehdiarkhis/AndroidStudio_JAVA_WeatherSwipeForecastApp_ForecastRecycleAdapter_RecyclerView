package com.akshatdale.theweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    //View===========
    EditText cityEditText;
    TextView cityTextView, weatherConditionTextView, temperatureTextView, textViewSunriseTime, textViewSunsetTime, textViewWindSpeed, textViewHumidityPercentage , textViewPressureValue;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    LinearLayoutManager layoutManagerForRecycleView;
    RecyclerView recyclerViewForecast;
    //=========


    //Global Variables===========
    String  currentCity, currentTemperature, currentWindSpeed, currentWeatherDescriptionMain, currentHumidity,
            sunriseToday, sunsetToday;

        //Layout list horiwontale
        ArrayList<SetForecastWeatherData> setForecastWeatherDataArrayList;
        SwipeRefreshLayout swipeRefresh;

    int currentIconId,forecastIconId;
    //====================


    //Static Var=======
    private static final DecimalFormat df = new DecimalFormat("0.0");
    public static final int REQUEST_CODE = 100;
    public String cityName;
    //===================


    //Location==
    private LocationManager mLocationManagerGPS;
    private LocationListener mLocationListenerGPS;
    //=-=====

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Views======================================
        cityEditText = findViewById(R.id.cityEditText);
        cityTextView = findViewById(R.id.cityTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        textViewSunsetTime = findViewById(R.id.textViewSunsetTime);
        textViewSunriseTime = findViewById(R.id.textViewSunriseTime);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewHumidityPercentage = findViewById(R.id.textViewHumidityPercentage);
        textViewPressureValue = findViewById(R.id.textViewPressureValue);
        weatherImageView = findViewById(R.id.weatherImageView);
        linearLayoutFirst = findViewById(R.id.linearLayoutFirst);
        recyclerViewForecast = findViewById(R.id.recyclerViewForecast);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        //================================================


        //Layout list horiwontale set======
        layoutManagerForRecycleView = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewForecast.setLayoutManager(layoutManagerForRecycleView);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
        setForecastWeatherDataArrayList = new ArrayList<>();
        //===================================


        //Fonction en bas : pour set l'image du back selon le temps actuel
        setWeatherBackground();
        //===

        //Fonction en bas : permet de recuperer la localisation actuel (cityname)
        String currentLocation = getLocation();
        //=====

        // Fonction en bas : passer le nom de la ville a une fonction qui recupere les infos du weather  API
       getWeatherDataCallAPI(currentLocation);
       //===

        //Fonction en bas : REMPLIR LE LIST VIEW HORIZONTAL PAR LES INFOS du weather  API de 15 jours
        getForecastWeatherData(currentLocation);
        //===


        //ca permet de rafraichir tout les donner si on tire la page vers le base===========
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //RE-recuperer la localisation actuel (cityname)
                String currentLocation = getLocation();
                //=====

                //Re-passer le nom de la ville a une fonction qui Re-recupere les infos du weather  API
                getWeatherDataCallAPI(currentLocation);

                //RE-REMPLIR LE LIST VIEW HORIZONTAL PAR LES INFOS du weather  API de 15 jours
                getForecastWeatherData(currentLocation);

                swipeRefresh.setColorSchemeColors(Color.RED);
                swipeRefresh.setSoundEffectsEnabled(true);
                swipeRefresh.setRefreshing(false);
            }
        });
        //=====================================================================================

    }




    //Recuperer les information de Notre API
    public void getWeatherDataCallAPI(String cityName) {

        //pour Envoyer la requette
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        //=====================

        //API + cityName + apiKEY
        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=d53b3a5d1e217f708d42a887bbb02502&units=metric";
        //=====

        //preparer la REQUETTE GET vers l'API
        JsonObjectRequest jsonObjectRequestGetWeather = new JsonObjectRequest(Request.Method.GET,
                getWeatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    //GET=====
                    currentTemperature = response.getJSONObject("main").getString("temp");
                    double temperatureDouble = Double.parseDouble(currentTemperature);
                    String currentTemperatureDigit = df.format(temperatureDouble);
                    //SET
                    temperatureTextView.setText(String.format("%s°C ", currentTemperatureDigit));
                    //=======


                    //GET=====
                    currentHumidity = response.getJSONObject("main").getString("humidity");
                    //SET
                    textViewHumidityPercentage.setText(currentHumidity + "% ");
                    //=======


                    //GET=====
                    sunriseToday = response.getJSONObject("sys").getString("sunrise");
                    sunsetToday = response.getJSONObject("sys").getString("sunset");
                    //SET
                    textViewSunriseTime.setText(istTimeConverter(sunriseToday));
                    textViewSunsetTime.setText(istTimeConverter(sunsetToday));
                    //=======


                    //GET=====
                    currentCity = response.getString("name");
                    //SET
                    cityTextView.setText(currentCity + "  ");
                    //=======


                    //PRESSION GET
                    String currentPressure = response.getJSONObject("main").getString("pressure");
                    double currentPressuredouble = Double.parseDouble(currentPressure)/1013;
                    String currentPressureString = df.format(currentPressuredouble);
                    //SET
                    textViewPressureValue.setText(currentPressureString+" atm");
                    //========


                    // get infos de l'Objects Json weather
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                        //GET=====
                         currentWeatherDescriptionMain = jsonObject.getString("main");
                        //SET
                        weatherConditionTextView.setText(currentWeatherDescriptionMain + "  ");
                        //=======


                        //GET=====
                         currentIconId = jsonObject.getInt("id");
                        //SET
                         weatherImageView.setImageResource(new SetWeatherIcon().setIconCurrent(currentIconId));
                        //=======


                        //GET=====
                         currentWindSpeed = response.getJSONObject("wind").getString("speed");
                        double windSpeedKM = ((Double.parseDouble(currentWindSpeed)) * 3.6);
                        String windSpeed2Digit = df.format(windSpeedKM);
                        //SET
                        textViewWindSpeed.setText(windSpeed2Digit + " km/h");
                        //=======

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "quelque chose c'est mal passer lors du fetch de Data");
            }
        });

        //Envoyer la Requette
        requestQueue.add(jsonObjectRequestGetWeather);
        //=========
    }



    //Recuperer les infos (15jours)
    // qui vont servir a REMPLIR LE LIST VIEW HORIZONTAL PAR LES INFOS de 15 jours
    public void getForecastWeatherData(String cityName){

        //API + cityName + apiKey
        String forecastWeatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName+ "&appid=d53b3a5d1e217f708d42a887bbb02502&units=metric";
        //======

        //pour envoyer la requette===
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        //=====

        //Preparer la requette Get vers l'Api
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, forecastWeatherUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    //List des infos weather pour 15 jours
                    JSONArray jsonArray = response.getJSONArray("list");

                    //boucler dans la list pour recuper les infos
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String dateTime = jsonObject.getString("dt");
                        String temperature = jsonObject.getJSONObject("main").getString("temp");

                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                         int forecastIconId = jsonObjectWeather.getInt("id");

                        String dateIst = istDateConverter(dateTime);
                        String timeIst = istTimeConverter(dateTime);

                        //Remplir la list qui est de type SetForecastWeatherData (costum class)
                            //Passer les infromation a .setForecastIcon() ,
                            // une fonction dans la classe SetWeatherIcon qu'on creer ,
                            //qui retourne l'icone adequate selon selon les infos
                        setForecastWeatherDataArrayList.add(new SetForecastWeatherData(timeIst,dateIst,temperature,new SetWeatherIcon().setForecastIcon(forecastIconId,dateTime)));

                        //Passer la list a l'adapter
                        //qui va les afficher visuellement dans des block (costum layout)
                        //comme le concepte du ListView
                        ForecastRecycleAdapter forecastRecycleAdapter = new ForecastRecycleAdapter(MainActivity.this,setForecastWeatherDataArrayList);
                        recyclerViewForecast.setAdapter(forecastRecycleAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
            }
        });

        //Envoyer la requette==
        requestQueue.add(jsonObjectRequest);
        //=================
    }



    //TIME : COnvertir UNIX time format to IST TIme format : (utiliser en haut a plusieurs reprise)
    public String istTimeConverter(String seconds) {

        // Unix seconds
        long unix_seconds = Long.parseLong(seconds);
        //===

        // convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000L);
        //===

        // format of the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
        String time = jdf.format(date);
        //===

        return time;
    }
    //===============================



    //DATE : COnvertir UNIX time format to IST TIme format : (utiliser en haut a plusieurs reprise)
    public String istDateConverter(String seconds) {

        // Unix seconds
        long unix_seconds = Long.parseLong(seconds);
        //====

        // convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000L);
        //=====

        // format of the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("dd/MM");
        String dateOnly = jdf.format(date);
        //======

        return dateOnly;
    }



    //BUTTON CLICK SUR RESEARCH a CITY========================
    public void userEnterCity(View view){

        String city = cityEditText.getText().toString();

        if (city.isEmpty()){
            Toast.makeText(getApplicationContext(), "Entrer le nom d'une Ville", Toast.LENGTH_SHORT).show();
        }
        else{
            //Video La List d'infos
            setForecastWeatherDataArrayList.clear();
            //Rafraichir tout les infos en fonction de la nouvelle ville entrer
            getWeatherDataCallAPI(city);
            getForecastWeatherData(city);
        }
    }
    //=======================================================



    //RECUPERER Les information de Location via le GPS > Geocode > return cityname
    public String getLocation(){

        //Manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //=======

        //PERMISSIONS 1 : checker les location permission de l'app, si non proposer au User de Allow location permissions pour l'app
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        }
        //===================================================

        //utiliser le locationManager pour set une variable de type Location + Recuperer lat/long
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        String cityName = "Not Found";

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            //===============

            //GEOCODER : POUR PLUS D"INFOS SUR LA LOCATION (pays,ville,province...)=====================


            //geocoder constructeur
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            //=====

            try {
                List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 10);
                for (Address address : addressList) {
                    if (address != null) {

                        String city = address.getLocality();

                        if (city != null && !city.equals("")) {
                            cityName = city;
                        } else {
                            Toast.makeText(getApplicationContext(), "Ville non Trouvable", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            //===
            return cityName;
            //====
    }


    //PERMISSION 2 : cette fonction est obligatoire a Override si on utilsie le :  checkSelfPermission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //===========================



    //SET WEATHER ICON ON WEATHER IMAGE VIEW
    public void setWeatherBackground() {

        int time =0;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        time = Integer.parseInt(formatter.format(date));

        //background de jour
        if (time >= 6 && time < 18) {
            linearLayoutFirst.setBackgroundResource(R.drawable.day_background);
        }
        // background de nuit
        else {
            linearLayoutFirst.setBackgroundResource(R.drawable.night_background);
        }
    }
}