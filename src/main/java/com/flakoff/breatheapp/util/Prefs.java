package com.flakoff.breatheapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Prefs {
    private SharedPreferences preferences;

    public Prefs(Activity activity){
        this.preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public void setBreaths(int breaths) {
        preferences.edit().putInt("breaths", breaths).apply();
    }

    public int getBreaths() {
        return preferences.getInt("breaths", 0);
    }

    public void setSessions(int sessions) {
        preferences.edit().putInt("sessions", sessions).apply();
    }

    public int getSessions() {
        return preferences.getInt("sessions", 0);
    }

    public void setDate(long millis) {
        preferences.edit().putLong("millis", millis).apply();
    }

    public String getDate() {
        long millisDate = preferences.getLong("millis", 0);

        Date date = new Date(millisDate);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a", Locale.FRANCE);

        return sdf.format(date);
    }

    public long getDateInMillis() {
        return preferences.getLong("millis", 0);
    }
}
