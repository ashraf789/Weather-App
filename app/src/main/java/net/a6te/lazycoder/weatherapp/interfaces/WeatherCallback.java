package net.a6te.lazycoder.weatherapp.interfaces;

import net.a6te.lazycoder.weatherapp.mvp.model.Weather;

public interface WeatherCallback {
    void onItemClick(Weather getWeatherData);
}
