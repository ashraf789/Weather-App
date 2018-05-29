package net.a6te.lazycoder.weatherapp.mvp.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.a6te.lazycoder.weatherapp.GlobalVariables;
import net.a6te.lazycoder.weatherapp.MapsActivity;
import net.a6te.lazycoder.weatherapp.NetworkStateReceiver;
import net.a6te.lazycoder.weatherapp.R;
import net.a6te.lazycoder.weatherapp.alarm.AlarmReceiver;
import net.a6te.lazycoder.weatherapp.alarm.NotificationScheduler;
import net.a6te.lazycoder.weatherapp.interfaces.WeatherCallback;
import net.a6te.lazycoder.weatherapp.mvp.MVPPresenter;
import net.a6te.lazycoder.weatherapp.mvp.MVPView;
import net.a6te.lazycoder.weatherapp.mvp.model.Weather;
import net.a6te.lazycoder.weatherapp.mvp.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MVPView.mainView,WeatherCallback{

    private MVPPresenter.MainPresenter mPresenter;

    @BindView(R.id.weatherRV)
    RecyclerView weatherRv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.warningTv)
    TextView warningTv;


    private NetworkStateReceiver networkStateReceiver;
    private IntentFilter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initializeAll();

        mPresenter.prepareAdapter();
        mPresenter.initializeAlarm();
    }

    private void initializeAll() {
        mPresenter = new MainPresenter(this);

        weatherRv.setHasFixedSize(true);
        weatherRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        setSupportActionBar(toolbar);
        networkStateReceiver = new NetworkStateReceiver();
        filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(connectionStatusReceiver
                ,new IntentFilter(GlobalVariables.BROADCAST_CONNECTION_STATUS));

        requestRuntimePermission();
    }

    @Override
    public void initializeRecyclerView(final RecyclerView.Adapter adapter){
        weatherRv.setAdapter(adapter);
    }
    @Override
    public void showInternetConnectionRequiredTv(){
        warningTv.setVisibility(View.VISIBLE);
    }

    public void hideInternetConnectionRequiredTv(){
        warningTv.setVisibility(View.GONE);
    }

    //update remainder
    @Override
    public void updateRemainder(int hours, int mints){
        NotificationScheduler.setReminder(MainActivity.this, AlarmReceiver.class, hours, mints);
    }
    //this method will be call when user click an item from listed weather
    @Override
    public void onItemClick(Weather getWeatherData) {
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra(GlobalVariables.WEATHER_DATA,getWeatherData);

        startActivity(intent);
    }

    /*
    * Broadcast message receiver
    * this receiver will receive all network state change message from NetworkStateChnageReceiver class
    * */
    BroadcastReceiver connectionStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.getBoolean(GlobalVariables.DATA_CONNECTION_ENABLE)){
                mPresenter.prepareAdapter();
                hideInternetConnectionRequiredTv();
            }else{
                showInternetConnectionRequiredTv();
            }
        }
    };


    /*
    * when app start we register network state receiver
    * */
    @Override
    protected void onStart() {
        registerReceiver(networkStateReceiver,filter);
        super.onStart();
    }
    /*
     * when app destroy we don't need to check network status so we are unRegistering broadCast Receiver
     * */
    @Override
    protected void onDestroy() {
        unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }

    /*
    * android 5 =< need runtime permission so we call this method for take runtime permission from user
    * */
    public void requestRuntimePermission() {

        final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,
                    PERMISSIONS,
                    GlobalVariables.MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            //permission already granted
            //like android version < 5(kitkat) don,t need runtime permission
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GlobalVariables.MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //permission granted
                    }

                } else {
                    permissionDenied();
                }
                return;
            }

        }
    }

    private void permissionDenied() {
        Toast.makeText(this, R.string.permission_denied,Toast.LENGTH_SHORT).show();
    }



}
