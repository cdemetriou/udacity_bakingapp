package com.android.bakingapp.modules.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.bakingapp.data.Repository;
import com.android.bakingapp.model.Recipe;

import java.util.List;

/**
 * Created by Christos on 31/01/2018.
 */

public class MainViewModel extends ViewModel {

    private LiveData<List<Recipe>> recipeList = new MutableLiveData<>();

    private Repository repository;


    public MainViewModel(Repository repository){
        this.repository = repository;
    }


    public void init() {
        if (repository != null) Log.e("Repo", String.valueOf(repository));
        recipeList = repository.getRecipes();
    }

    public  LiveData<List<Recipe>> getRecipes() {
        return recipeList;
    }

}
