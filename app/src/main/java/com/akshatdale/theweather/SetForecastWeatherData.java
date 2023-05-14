package com.akshatdale.theweather;

import android.widget.ImageView;

//LES INFOS(de 15jours) Qui sont afficher en Boucle(jour par jour) en bas de la page
//chaque jour est un obget de cette Class
//ArrayList<SetForecastWeatherData>

public class SetForecastWeatherData {
     String time;
     String date;
     String temperature;
     int weatherIcon;

    public SetForecastWeatherData(String time,String date, String temperature,int weatherIcon) {
        this.time = time;
        this.date = date;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
    }
}
