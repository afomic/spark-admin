package com.afomic.sparkadmin;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by afomic on 10/2/17.
 */

public class SparkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
