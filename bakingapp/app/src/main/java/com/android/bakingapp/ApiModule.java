package com.android.bakingapp;

import android.arch.lifecycle.ViewModelProvider;

import com.android.bakingapp.data.Constants;
import com.android.bakingapp.data.DataService;
import com.android.bakingapp.data.Repository;
import com.android.bakingapp.main.CustomViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Christos on 31/01/2018.
 */

@Module
public class ApiModule {
    private final String mBaseUrl;

    public ApiModule() {
        this.mBaseUrl = Constants.BASE_URL;
    }


    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public DataService provideDataService(Retrofit retrofit) {
        return retrofit.create(DataService.class);
    }

    @Provides
    @Singleton
    public Repository provideRepository(DataService dataService){
        return new Repository(dataService);
    }

    @Provides
    @Singleton
    public ViewModelProvider.Factory provideViewModelFactory(Repository repository){
        return new CustomViewModelFactory(repository);
    }

}
