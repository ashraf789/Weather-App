package net.a6te.lazycoder.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckInternetConnection {

    //checking internet connection if internet is connected then it will return true
    // otherwise it will return false

    public static boolean isNetworkEnable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();

        boolean isConnected = (nInfo != null && nInfo.isConnectedOrConnecting());
        return isConnected;
    }

}
