package net.a6te.lazycoder.weatherapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import net.a6te.lazycoder.weatherapp.mvp.model.Weather;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Weather weatherData;

    @BindView(R.id.cityNameTv)
    TextView cityNameTv;
    @BindView(R.id.skyTv)
    TextView skyTv;
    @BindView(R.id.humidityTv)
    TextView humidityTv;
    @BindView(R.id.windSpeedTv)
    TextView windSpeedTv;
    @BindView(R.id.maxTempTv)
    TextView maxTempTv;
    @BindView(R.id.minTempTv)
    TextView minTempTv;
    @BindView(R.id.temperatureTv)
    TextView temperatureTv;
    @BindView(R.id.iconIv)
    ImageView iconIv;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initializeAll();
        updateUi();
    }

    private void initializeAll() {
        try{
            weatherData = (Weather) getIntent().getSerializableExtra(GlobalVariables.WEATHER_DATA);
        }catch (Exception e){
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(onNavigationClick);

    }

    private void updateUi() {
        cityNameTv.setText(weatherData.getCityName());
        skyTv.setText(weatherData.getSky());
        humidityTv.setText("Humidity: "+weatherData.getHumidity());
        windSpeedTv.setText("Wind Speed: "+weatherData.getWindSpeed());
        maxTempTv.setText("Max.Temp.: "+weatherData.getMaxTemp());
        minTempTv.setText("Min.Temp.: "+weatherData.getMinTemp());

        temperatureTv.setText(weatherData.getTemperature());
        Picasso.with(this)
                .load(getApiUrl(weatherData.getIcon()))
                .placeholder(R.drawable.icon)
                .into(iconIv);
    }

    public String getApiUrl(String icon){
        return "http://openweathermap.org/img/w/"+icon+".png";
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(weatherData.getLatitude(), weatherData.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title(weatherData.getCityName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    View.OnClickListener onNavigationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
