package com.android.bakingapp;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Christos on 31/01/2018.
 */

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule (Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
    }


}
