package net.a6te.lazycoder.weatherapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class NetworkStateReceiver extends BroadcastReceiver {

    private Context context;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        this.context = context;
        if (!checkConnect()) {
            Log.d(context.getPackageName(), "onReceive: No internet connection");
            sendMessage(context.getString(R.string.internet_connection_required),GlobalVariables.NO_CONNECTION_CODE, false);

        }else if (!checkLocation()) {
            Log.d(context.getPackageName(), "onReceive: no location connection");
            sendMessage(context.getString(R.string.location_access_required),GlobalVariables.NO_CONNECTION_CODE, true);
        }else{
            sendMessage("All are connected",GlobalVariables.ALL_CONNECTED, true);
        }

    }

    /*
    * broad cast Network state change message
    * all registered receiver will receive this message
    * */
    public void sendMessage(String status,int statusCode, boolean dataConnectionEnable){
        Intent intent = new Intent(GlobalVariables.BROADCAST_CONNECTION_STATUS);
        Bundle bundle = new Bundle();
        bundle.putInt(GlobalVariables.STATUS_CODE,statusCode);
        bundle.putString(GlobalVariables.CONNECTION_STATUS,status);
        bundle.putBoolean(GlobalVariables.DATA_CONNECTION_ENABLE,dataConnectionEnable);
        intent.putExtras(bundle);

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private boolean checkConnect() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return true;
        }
        return false;
    }
    private boolean checkLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
