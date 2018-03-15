package com.android.bakingapp;

import android.app.Application;

import com.android.bakingapp.widget.WidgetManager;


/**
 * Created by Christos on 31/01/2018.
 */

public class MyApplication extends Application {
    private ApiComponent applicationComponent;
    public static WidgetManager recipeWidgetManager;

    @Override
    public void onCreate() {
        super.onCreate();
        recipeWidgetManager = new WidgetManager(this);


        applicationComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();

    }

    public ApiComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static WidgetManager getWidgetManager(){
        return recipeWidgetManager;
    }
}
