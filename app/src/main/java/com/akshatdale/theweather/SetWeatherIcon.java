package com.akshatdale.theweather;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SetWeatherIcon {

    //icon image
    int icon =0;

    public int setIconCurrent(int iconId) {

        //set l'image selon l'iconId qu'on recuper de l'API
        if (iconId == 800) {
            icon = R.drawable.clear_day;

        } else if (iconId == 801) {

            icon = R.drawable.fewcloud_day;
        } else if (iconId == 802) {

            icon = R.drawable.scatteredcloud_day;
        } else if (iconId == 803 || iconId == 804) {

            icon = R.drawable.overcast_cloud;
        } else if (iconId >= 701 && iconId <= 781) {

            icon = R.drawable.mist_day;
        } else if (iconId >= 600 && iconId <= 622) {

            icon = R.drawable.snow;
        } else if (iconId == 500 || iconId == 501 || iconId == 520 || iconId == 521 || iconId == 522) {

            icon = R.drawable.lightrain_day;
        } else if (iconId == 502 || iconId == 503 || iconId == 504 || iconId == 531) {

            icon = R.drawable.heavy_rain;
        } else if (iconId == 511) {

            icon = R.drawable.freezing_rain;
        } else if (iconId >= 300 && iconId <= 321) {

            icon = R.drawable.drizziling;
        } else if (iconId == 200 || iconId == 201 || iconId == 202 || iconId == 221 || iconId == 230 || iconId == 231 || iconId == 232) {

            icon = R.drawable.thunderstrom_rain;
        } else if (iconId == 210 || iconId == 211) {

            icon = R.drawable.thunderstrom_day;
        } else if (iconId == 212) {

            icon = R.drawable.heavythunderstrom_day;
        } else {

            icon = R.drawable.weatherlogo;
        }

        return  icon;
    }




    //Set L'icon en fonction de si c le jour ou c la nuit
    public int setForecastIcon(int iconId,String timeForecastInUnix){

        //Convertion En Bonne format
        long unix_seconds = Long.parseLong(timeForecastInUnix);

        Date date = new Date(unix_seconds * 1000L);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("HH");

        String timeString = jdf.format(date);
        int time = Integer.parseInt(timeString);
        //=======================

        
        if (!timeString.equals("")) {
            try {


                if (time >= 6 && time < 18) {
                    if (iconId == 800) {
                        icon = R.drawable.clear_day;

                    } else if (iconId == 801) {

                        icon = R.drawable.fewcloud_day;
                    } else if (iconId == 802) {

                        icon = R.drawable.scatteredcloud_day;
                    } else if (iconId == 803 || iconId == 804) {

                        icon = R.drawable.overcast_cloud;
                    } else if (iconId >= 701 && iconId <= 781) {

                        icon = R.drawable.mist_day;
                    } else if (iconId >= 600 && iconId <= 622) {

                        icon = R.drawable.snow;
                    } else if (iconId == 500 || iconId == 501 || iconId == 520 || iconId == 521 || iconId == 522) {

                        icon = R.drawable.lightrain_day;
                    } else if (iconId == 502 || iconId == 503 || iconId == 504 || iconId == 531) {

                        icon = R.drawable.heavy_rain;
                    } else if (iconId == 511) {

                        icon = R.drawable.freezing_rain;
                    } else if (iconId >= 300 && iconId <= 321) {

                        icon = R.drawable.drizziling;
                    } else if (iconId == 200 || iconId == 201 || iconId == 202 || iconId == 221 || iconId == 230 || iconId == 231 || iconId == 232) {

                        icon = R.drawable.thunderstrom_rain;
                    } else if (iconId == 210 || iconId == 211) {

                        icon = R.drawable.thunderstrom_day;
                    } else if (iconId == 212) {

                        icon = R.drawable.heavythunderstrom_day;
                    } else {

                        icon = R.drawable.weatherlogo;

                    }
                } else {
                    if (iconId == 800) {

                        icon = R.drawable.clear_night;
                    } else if (iconId == 801) {

                        icon = R.drawable.fewcloud_night;
                    } else if (iconId == 802) {

                        icon = R.drawable.scatteredcloud_night;
                    } else if (iconId == 803 || iconId == 804) {

                        icon = R.drawable.overcast_cloud;
                    } else if (iconId >= 701 && iconId <= 781) {

                        icon = R.drawable.mist_night;
                    } else if (iconId >= 600 && iconId <= 622) {

                        icon = R.drawable.snow;
                    } else if (iconId == 500 || iconId == 501 || iconId == 520 || iconId == 521 || iconId == 522) {

                        icon = R.drawable.lightrain_night;
                    } else if (iconId == 502 || iconId == 503 || iconId == 504 || iconId == 531) {

                        icon = R.drawable.heavy_rain;
                    } else if (iconId == 511) {

                        icon = R.drawable.freezing_rain;
                    } else if (iconId >= 300 && iconId <= 321) {

                        icon = R.drawable.drizziling;
                    } else if (iconId == 200 || iconId == 201 || iconId == 202 || iconId == 221 || iconId == 230 || iconId == 231 || iconId == 232) {

                        icon = R.drawable.thunderstrom_rain;
                    } else if (iconId == 210 || iconId == 211) {

                        icon = R.drawable.thunderstrom_night;
                    } else if (iconId == 212) {

                        icon = R.drawable.heavythunderstrom_night;
                    } else {

                        icon = R.drawable.weatherlogo;
                    }
                }
            } catch (Exception e) {
                Log.i("error", e.toString());
            }
        }

        return  icon;
    }
}
