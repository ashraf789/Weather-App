package net.a6te.lazycoder.weatherapp.alarm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import net.a6te.lazycoder.weatherapp.GlobalVariables;
import net.a6te.lazycoder.weatherapp.R;
import net.a6te.lazycoder.weatherapp.SharedPreference;
import net.a6te.lazycoder.weatherapp.interfaces.RetrofitApiCall;
import net.a6te.lazycoder.weatherapp.mvp.model.CurrentWeatherData;
import net.a6te.lazycoder.weatherapp.mvp.view.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.LOCATION_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {

    private String TAG = "AlarmReceiver";
    private Context mContext;
    private SharedPreference sharedPreference;
    private Location location; // location
    private LocationManager locationManager;
    private RetrofitApiCall apiClient;
    private String currentWeatherApi;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        initializeAll();


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, AlarmReceiver.class, sharedPreference.getHours(), sharedPreference.getMints());
                return;
            }
        }
        downloadCurrentTemperature();
    }

    private void initializeAll() {
        sharedPreference = new SharedPreference(mContext);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalVariables.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient = retrofit.create(RetrofitApiCall.class);

    }

    public void setNotification(String message){
        NotificationScheduler.showNotification(mContext, MainActivity.class,
                mContext.getString(R.string.app_name), "Current Temperature: " + message);
    }

    /*
    * we already took the permission when app first time open so we are ignoring this
    * */
    @SuppressLint("MissingPermission")
    public void downloadCurrentTemperature() {
        if (!checkConnect() || !checkLocation()){
            setNotification( "GPS and data connection required!");
            return;
        }else {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            currentWeatherApi = GlobalVariables.CURRENT_WEATHER_API
                    +"lat="+location.getLatitude()
                    +"&lon="+location.getLatitude()
                    +"&appid="+GlobalVariables.APP_ID;

            Call<CurrentWeatherData> apiCall = apiClient.getCurrentWeather(currentWeatherApi);
            apiCall.enqueue(new Callback<CurrentWeatherData>() {
                @Override
                public void onResponse(Call<CurrentWeatherData> call, Response<CurrentWeatherData> response) {
                    if (response.isSuccessful()){
                        Double temperature = response.body().getMain().getTemp();
                        setNotification(String.valueOf((int)(temperature/10))+"ºC");

                    }
                }
                @Override
                public void onFailure(Call<CurrentWeatherData> call, Throwable t) {
                    t.printStackTrace();
                    setNotification( "GPS and data connection required!");
                }
            });
        }

    }
    /*
    * checking network and location connection status
    * */
    private boolean checkConnect() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;
        }
        return false;
    }
    private boolean checkLocation() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}


