package net.a6te.lazycoder.weatherapp.mvp.model;

import java.io.Serializable;

public class Weather implements Serializable{
    private String cityName;
    private String temperature;
    private String sky;
    private double latitude;
    private double longitude;
    private double humidity;
    private double windSpeed;
    private String maxTemp;
    private String minTemp;
    private String icon;

    public Weather(String cityName, String temperature, String sky, double latitude, double longitude, double humidity, double windSpeed, String maxTemp, String minTemp, String icon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.sky = sky;
        this.latitude = latitude;
        this.longitude = longitude;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.icon = icon;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
