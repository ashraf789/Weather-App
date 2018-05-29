package net.a6te.lazycoder.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    private static final String APP_PREFS_NAME = "SharedPreference";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private static final String HOURS = "hours";
    private static final String MINTS = "mints";

    public SharedPreference(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void saveHours(int hours){
        prefsEditor.putInt(HOURS,hours);
        prefsEditor.commit();
    }
    public int getHours(){
        return appSharedPrefs.getInt(HOURS,6);
    }

    public void saveMints(int mints){
        prefsEditor.putInt(MINTS,mints);
        prefsEditor.commit();
    }
    public int getMints(){
        return appSharedPrefs.getInt(MINTS,47);
    }



}
