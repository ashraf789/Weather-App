package net.a6te.lazycoder.weatherapp.mvp.presenter;

import android.content.Context;
import android.util.Log;

import net.a6te.lazycoder.weatherapp.CheckInternetConnection;
import net.a6te.lazycoder.weatherapp.GlobalVariables;
import net.a6te.lazycoder.weatherapp.SharedPreference;
import net.a6te.lazycoder.weatherapp.adapters.WeatherRv;
import net.a6te.lazycoder.weatherapp.interfaces.RetrofitApiCall;
import net.a6te.lazycoder.weatherapp.mvp.MVPPresenter;
import net.a6te.lazycoder.weatherapp.mvp.MVPView;
import net.a6te.lazycoder.weatherapp.mvp.model.Weather;
import net.a6te.lazycoder.weatherapp.mvp.model.WeatherData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPresenter implements MVPPresenter.MainPresenter {
    private Context mContext;
    private MVPView.mainView mvpView;
    private RetrofitApiCall apiClient;
    private CheckInternetConnection internetConnection;
    private SharedPreference preference;

    public MainPresenter(Context mContext) {
        this.mContext = mContext;
        mvpView = (MVPView.mainView) mContext;

        internetConnection = new CheckInternetConnection();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalVariables.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient = retrofit.create(RetrofitApiCall.class);
        preference = new SharedPreference(mContext);
    }

    //prepare recyclerView adapter
    @Override
    public void prepareAdapter(){
        downloadWeatherData();
    }

    //initialize alarm manager(remainder)
    @Override
    public void initializeAlarm(){
            int hour = preference.getHours();
            int mint = preference.getMints();

            mvpView.updateRemainder(hour, mint);
    }

    //downloading weather data from openweathermap
    private void downloadWeatherData() {
        //internet connection checking
        if (!internetConnection.isNetworkEnable(mContext)){
            mvpView.showInternetConnectionRequiredTv();
            return;
        }

        Call<WeatherData> weatherData = apiClient.getWeatherData(GlobalVariables.WEATHER_API+GlobalVariables.APP_ID);
        weatherData.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if (response.isSuccessful())
                    updateUi(response.body());
                else {
                    Log.d(mContext.getPackageName(), "onResponse: "+response.message());
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    /*
    * retrive data and call for update UI
    * */
    private void updateUi(WeatherData data) {

        ArrayList<Weather> weatherData = new ArrayList();
        String temp;
        String weather;
        double lat;
        double lon;
        double humidity;
        double windSpeed;
        String maxTemp;
        String minTemp;
        String icon;


        for (WeatherData.List list: data.getList()) {
            temp = String.valueOf((int) (list.getMain().getTemp()/10))+"ºC";//converting kelvin to celsius
            weather = list.getWeather().get(0).getMain()+"";
            lat = list.getCoord().getLat();
            lon = list.getCoord().getLon();
            humidity = list.getMain().getHumidity();
            maxTemp = String.valueOf((int) (list.getMain().getTempMax()/10))+"ºC";
            minTemp = String.valueOf((int) (list.getMain().getTempMin()/10))+"ºC";
            windSpeed = list.getWind().getSpeed();
            icon = list.getWeather().get(0).getIcon();

            weatherData.add(new Weather(list.getName(),temp,weather,lat,lon,humidity,windSpeed,maxTemp,minTemp,icon));
        }
        WeatherRv adapter = new WeatherRv(mContext,weatherData);
        mvpView.initializeRecyclerView(adapter);
    }

}
