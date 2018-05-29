package net.a6te.lazycoder.weatherapp.interfaces;

import net.a6te.lazycoder.weatherapp.mvp.model.CurrentWeatherData;
import net.a6te.lazycoder.weatherapp.mvp.model.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitApiCall {

    @GET()
    Call<WeatherData> getWeatherData(@Url String url);
    @GET()
    Call<CurrentWeatherData> getCurrentWeather(@Url String url);

}
