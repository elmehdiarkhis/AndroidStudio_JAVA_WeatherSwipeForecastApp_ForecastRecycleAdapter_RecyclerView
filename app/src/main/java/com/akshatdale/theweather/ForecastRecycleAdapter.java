package com.akshatdale.theweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//CETTE CLASS m'aide (adapter) a placer les infos(15 jours) dans les block
//qui vont etre afficher en boucle en bas de la page
//Comme le ListViewAdapter
public class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder> {


    Context context;
    ArrayList<SetForecastWeatherData> forecastWeatherDataArrayList;

    //On pass au Constructeur , l'arrrayList qui contient les infos qu'on recuper de l'API
    //un Array List de type > SetForecastWeatherData
      ForecastRecycleAdapter(Context context, ArrayList<SetForecastWeatherData> weatherDataArrayList) {
        this.context = context;
        this.forecastWeatherDataArrayList = weatherDataArrayList;
      }


      //Recuperer les element dans cette class ==========
    public class ViewHolder extends RecyclerView.ViewHolder {

          TextView time,date,temperature;
          ImageView weatherImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textviewForescastTime);
            date = itemView.findViewById(R.id.textviewForescastDate);
            temperature = itemView.findViewById(R.id.textviewForescastTemperature);
            weatherImage = itemView.findViewById(R.id.imageViewForecast);
        }
    }
    //============


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recuperer le petit costum layout
        View view = LayoutInflater.from(context).inflate(R.layout.forcast_layout,parent,false);
        //Recuperer les element de ce layout
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //REMPLIR les element avec les valeur de  ArrayList<SetForecastWeatherData> qu'on a passer dans le constructeur
MainActivity mainActivity = new MainActivity();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

          holder.time.setText(forecastWeatherDataArrayList.get(position).time);
          holder.date.setText(forecastWeatherDataArrayList.get(position).date);
          holder.temperature.setText(forecastWeatherDataArrayList.get(position).temperature+"°C");
          holder.weatherImage.setImageResource(forecastWeatherDataArrayList.get(position).weatherIcon);
    }

    @Override
    public int getItemCount() {
        return forecastWeatherDataArrayList.size();
    }


}
