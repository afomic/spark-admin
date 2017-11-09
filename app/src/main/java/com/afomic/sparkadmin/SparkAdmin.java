package com.afomic.sparkadmin;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by afomic on 11/9/17.
 */

public class SparkAdmin extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
