package br.com.mm.adcertproj.bakeit;

import android.app.Application;

import timber.log.Timber;

public class BakeITApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Setting up Timber Logging
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
