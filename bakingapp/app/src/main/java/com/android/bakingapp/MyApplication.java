package com.android.bakingapp;

import android.app.Application;


/**
 * Created by Christos on 31/01/2018.
 */

public class MyApplication extends Application {
    private ApiComponent applicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public ApiComponent getApplicationComponent() {
        return applicationComponent;
    }
}
