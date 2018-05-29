package net.a6te.lazycoder.weatherapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.a6te.lazycoder.weatherapp.R;
import net.a6te.lazycoder.weatherapp.interfaces.WeatherCallback;
import net.a6te.lazycoder.weatherapp.mvp.model.Weather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherRv extends RecyclerView.Adapter<WeatherRv.ViewHolder>{

    private Context mContext;
    private ArrayList<Weather> weathers;
    private WeatherCallback weatherCallback;

    public WeatherRv(Context mContext, ArrayList<Weather> weathers) {
        this.mContext = mContext;
        this.weathers = weathers;
        weatherCallback = (WeatherCallback) mContext;
    }

    @NonNull
    @Override
    public WeatherRv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRv.ViewHolder holder, final int position) {

        holder.cityNameTv.setText(weathers.get(position).getCityName());
        holder.forecastTv.setText(weathers.get(position).getSky());
        holder.temperatureTv.setText(weathers.get(position).getTemperature());

        holder.weatherItemRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherCallback.onItemClick(weathers.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cityNameTv) TextView cityNameTv;
        @BindView(R.id.forecastTv) TextView forecastTv;
        @BindView(R.id.temperatureTv)TextView temperatureTv;
        @BindView(R.id.weatherItemRV)
        RelativeLayout weatherItemRV;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
