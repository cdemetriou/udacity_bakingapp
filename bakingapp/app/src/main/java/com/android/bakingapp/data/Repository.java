package com.android.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.android.bakingapp.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Christos on 31/01/2018.
 */


public class Repository {

    private final static String TAG = "Repository";

    private DataService dataService;

    @Inject
    public Repository(DataService dataService){
        this.dataService = dataService;
    }

    public LiveData<List<Recipe>> getRecipes() {

        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();

        dataService.getRecipes().enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.code() != 200){
                    Log.d(TAG, "error reading data");
                    return;
                }
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "error reading data");
            }
        });
        return data;

    }
}
